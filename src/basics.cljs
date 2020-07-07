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

(def form-config {:header      core/form-header
                  :form-class  ""
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

(defcard-rg simple-validations
            (fn [re-frame-sub _]
              [:div {:style {:margin-bottom 30}}

               [:p "Basic form: both fields have validation. try clearing the name or changing the location"]

               [:div {:style {:border  "1px solid lightgrey"
                              :padding 20}}
                [fork/form {:initial-values (:record1 @re-frame-sub)
                            :validation     (core/validator-for-humans [:map
                                                                        ["name" [:string {:min 2 :max 50}]]
                                                                        ["location" [:enum {:error/message "Must be London or Tampere"}
                                                                                     "London" "Tampere"]]])}
                 (core/multi-row-form form-config)]]

               [:p "Questions:"]
               [:ol
                [:li "can the validation fn be pre-compiled and still used with m/explain"]]
               ])
            app-db
            {:inspect-data false})

(defcard-rg switching-records
            (fn [re-frame-sub _]
              [:div {:style {:margin-bottom 30}}

               [:p "Demo of loading different maps into a form. Did not work in early impls."]

               [:p (str "Editing: " (get-in @re-frame-sub [(:editing @re-frame-sub) "name"]))]

               [:div {:style {:margin-bottom "1rem"}}
                [:button {:onClick (fn [_]
                                     (swap! app-db assoc :editing :record2))}
                 "Load Tommi"]
                [:button {:onClick (fn [_]
                                     (swap! app-db assoc :editing :record1))}
                 "Load Lucio"]]

               [:div {:style {:border  "1px solid lightgrey"
                              :padding 20}}
                [fork/form {:initial-values (get @re-frame-sub (:editing @re-frame-sub))
                            :validation     (core/validator-for-humans [:map
                                                                        ["name" [:string {:min 2 :max 50}]]
                                                                        ["location" [:enum {:error/message "Must be London or Tampere"}
                                                                                     "London" "Tampere"]]])}
                 (core/multi-row-form form-config)]]

               [:p "Questions:"]
               [:ol
                [:li "why does Tommi not load in the form?"]]
               ])
            app-db
            {:inspect-data true})

