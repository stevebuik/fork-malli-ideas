(ns basics
  (:require [cljs.pprint :refer [pprint]]
            [devcards.core :refer-macros [defcard defcard-rg]]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [core]
            [fork.re-frame :as fork]))

(defonce app-db (r/atom {:record1 {"id"       "1234"
                                   "name"     "Lucio"
                                   "location" "London"}
                         :record2 {"id"       "5678"
                                   "name"     "Tommi"
                                   "location" "Tampere"}}))

;;; READ / DISPLAY

(defn- display
  [re-frame-sub _]
  [:div {:style {:margin-bottom 30}}

   [:p "Basic form: both fields have validation. try clearing the name or changing the location"]

   [:div {:style {:border  "1px solid lightgrey"
                  :padding 20}}
    [fork/form {:initial-values (:record1 @re-frame-sub)
                :validation     (core/validator-for-humans [:map
                                                            ["name" [:string {:min 2 :max 50}]]
                                                            ["location" [:enum {:error/message "Must be London or Tampere"}
                                                                         "London" "Tampere"]]])}
     (core/multi-row-form {:header      core/form-header
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
                                          :class  ""}]})]]

   [:p "Questions:"
    [:ol
     [:li "can the validation fn be pre-compiled and still used with m/explain"]]]
   ])

(defcard-rg form-hello
            display
            app-db
            {:inspect-data true})

