(ns basics
  (:require [cljs.pprint :refer [pprint]]
            [devcards.core :refer-macros [defcard defcard-rg]]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [core]
            [fork.re-frame :as fork]))

(defonce app-db (r/atom {:editing :record1
                         :record1 {"id"       "1234"
                                   "name"     "Lucio"
                                   "location" "London"}
                         :record2 {"id"       "5678"
                                   "name"     "Tommi"
                                   "location" "Tampere"}}))

(def malli-schema [:map
                   ["name" [:string {:min 2 :max 50}]]
                   ["location" [:enum {:error/message "Must be London or Tampere"}
                                "London" "Tampere"]]])

(defn questions
  [loc & qs]
  [:div
   [:p "Questions:"]
   [:ol
    (map-indexed (fn [i q]
                   [:li {:key (str loc "-question-" i)} q])
                 qs)]])

(def form-config {:form-class  ""
                  :inner-class ""
                  :input-rows  [{:inputs [{:type         "text"
                                           :field-name   "name"
                                           :place-holder "Enter name"
                                           :label        "Name"}]
                                 :class  ""}
                                {:inputs [{:type         "text"
                                           :field-name   "location"
                                           :place-holder "Enter location"
                                           :label        "Location"}]
                                 :class  ""}]})

(defn form-in-container
  [config validator value]
  [:div {:style {:border  "1px solid lightgrey"
                 :padding "0 0 20px 20px"}}
   [fork/form {:initial-values value
               :validation     validator}
    (core/multi-row-form config)]])

(defcard-rg simple-validations
            (fn [re-frame-sub _]
              [:div {:style {:margin-bottom 30}}

               [:p "Basic form: both fields have validation. try clearing the name or changing the location"]

               [form-in-container
                (-> form-config
                    (assoc :header core/form-header))
                (core/validator-for-humans malli-schema)
                (get @re-frame-sub (:editing @re-frame-sub))]

               [questions "simple"
                "can the validation fn be pre-compiled and still used with m/explain?"]])
            app-db
            {:inspect-data false})

(defcard-rg switching-records
            (fn [re-frame-sub _]
              [:div {:style {:margin-bottom 30}}

               [:p "Demo of loading different values into a form. Solution was not obvious initially."]
               [:p "The footer fn can use the Fork handler fns. In this case, the 'reset' handler."]
               [:p "Each form has its own local state i.e. form above is unaffected by changes to the app-db/sub"]

               [form-in-container
                (merge form-config
                       {:header (fn spacer [_] [:p])
                        :footer (fn [{:keys [reset]}]
                                  [:div {:style {:margin-bottom "1rem"}}
                                   [:button {:onClick (fn [_]
                                                        (reset {:values  (:record2 @re-frame-sub)
                                                                :touched #{}})
                                                        (swap! app-db assoc :editing :record2))}
                                    "Load Tommi"]
                                   [:button {:onClick (fn [_]
                                                        (reset {:values  (:record1 @re-frame-sub)
                                                                :touched #{}})
                                                        (swap! app-db assoc :editing :record1))}
                                    "Load Lucio"]])})
                (core/validator-for-humans malli-schema)
                (get @re-frame-sub (:editing @re-frame-sub))]

               ])
            app-db
            {:inspect-data true})

