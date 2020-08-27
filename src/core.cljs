(ns core
  (:require [malli.core :as m]
            [malli.error :as me]
            [malli.transform :as mt]
            [fork.re-frame :as fork]))

; define chain of Malli transform from Fork/form data back to clj maps
(def malli-transforms (mt/transformer
                        (mt/key-transformer {:decode keyword}) ; keys back to keywords
                        mt/string-transformer               ; and strings from inputs back to integers
                        ))

(defn validator-for-humans
  "HOF returning a Fork compatible validation fn from a schema."
  [schema]
  (fn [v]
    (let [; pre-compile schema for best performance
          explain (m/explainer schema)]
      (->> (m/decode schema v malli-transforms)             ; coerce using transforms above
           ; validate using keyword based schema
           explain
           ; return a map of error messages suitable for human UIs
           me/humanize))))

(defn input-errors
  "display a seq of errors"
  [errors]
  (when (seq errors)
    [:div.errors {}
     (map-indexed (fn [i e]
                    [:p.error {:style {:color "red"}
                               :key   (str "error-" i)} e])
                  errors)]))

(defn fork-input
  "return a form input, dispatching on the type value in the config arg"
  [{:keys [values handle-change handle-blur send-server-request] :as fork-form-data}
   {:keys [type field-name place-holder global-change-handler]}]
  (let [fire-server-event (fn []
                            (send-server-request
                              {:name     field-name
                               :debounce 1000}
                              (fn update-gql-field
                                [fork-form-data]
                                (when (and (nil? (:errors fork-form-data))
                                           (seq (:dirty fork-form-data)))
                                  (global-change-handler fork-form-data)))))]
    (case type
      ("text" "number") [:input {:id          field-name
                                 :type        type
                                 :class       ""
                                 :name        field-name
                                 :value       (values field-name)
                                 :placeholder place-holder
                                 :on-change   (fn [event]
                                                (handle-change event)
                                                (when global-change-handler
                                                  (fire-server-event)))
                                 :on-blur     handle-blur}])))

(defn fork-map
  "return a map with all keys as strings, following the design suggested by Fork"
  [value]
  (reduce-kv (fn [m k v]
               (assoc m (name k) v))
             {}
             value))

(defn form-header
  "a component that displays the latest Fork form state, even when invalid"
  [{:keys [values]}]
  [:p (values "name")])

(defn multi-row-form
  "HOF returning a Fork compatible fn providing a form with an (optional) headers, footers and N x M inputs in rows.
   css classes can be provided for the form and row inputs"
  [{:keys [header footer] :as config}]
  (fn [{:keys [errors] :as props}]
    [:div
     (when header
       [header props])
     (let [{:keys [form-class inner-class input-rows]} config]
       [:form {:class form-class}
        [:div {:class inner-class}
         (for [row input-rows]
           (let [{:keys [inputs class]} row]
             (map-indexed (fn [i {:keys [field-name label] :as config}]
                            [:div {:key   (str "row-" i)
                                   :class class}
                             [:label {:for field-name}
                              [:span label]]
                             (fork-input props config)
                             (->> (keyword field-name)      ; Malli errors use keywords
                                  (get errors)
                                  input-errors)])
                          inputs)))]])
     (when footer
       [footer props])]))

(defn form-in-container
  "return a fork form inside a box with a border. keeps devcards dry."
  [config validator initial-values]
  [:div {:style {:border  "1px solid lightgrey"
                 :padding "0 0 20px 20px"}}
   [fork/form {:initial-values initial-values
               :validation     validator}
    (multi-row-form config)]])




