(ns app
  (:require [reagent.core :as r]))

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

