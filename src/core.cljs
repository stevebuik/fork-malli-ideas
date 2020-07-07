(ns core
  (:require [malli.core :as m]
            [malli.error :as me]
            [reagent.core :as r]))

(defn validator-for-humans
  "HOF returning a Fork compatible validation fn from a schema."
  ; TODO The schema is transformed into a Malli validator for best performance. Can this work with explain/humanize?
  [schema]
  (fn [v]
    (->> v
         (m/explain schema)
         me/humanize)))

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
  [{:keys [values handle-change handle-blur] :as props}
   {:keys [type field-name place-holder] :as config}]
  (case type
    "text" [:input {:id          field-name
                    :type        type
                    :class       ""
                    :name        field-name
                    :value       (values field-name)
                    :placeholder place-holder
                    :on-change   handle-change
                    :on-blur     handle-blur}]))

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
                             (input-errors (get errors field-name))])
                          inputs)))]])
     (when footer
       [footer props])]))




