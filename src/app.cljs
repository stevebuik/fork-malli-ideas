(ns app
  (:require [reagent.core :as r]))

(defonce app-db (r/atom {:editing :record1
                         :record1 {:id               "1234"
                                   :name             "Lucio"
                                   :location         "London"
                                   :github-followers 18}
                         :record2 {:id               "5678"
                                   :name             "Tommi"
                                   :location         "Tampere"
                                   :github-followers 153}}))

; notice schemas use keywords, even though Fork forms use string keys
(def malli-schema [:map
                   [:id :string]
                   [:name [:string {:min 2 :max 50}]]
                   [:location [:enum {:error/message "Must be London or Tampere"}
                               "London" "Tampere"]]
                   [:github-followers pos-int?]])

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
                                           :field-name   "name" ; string keys (see Fork docs)
                                           :place-holder "Enter name"
                                           :label        "Name"}]
                                 :class  ""}
                                {:inputs [{:type         "text"
                                           :field-name   "location"
                                           :place-holder "Enter location"
                                           :label        "Location"}]
                                 :class  ""}]})

(def form-config2 {:form-class  ""
                   :inner-class ""
                   :input-rows  [{:inputs [{:type         "text"
                                            :field-name   "name" ; string keys (see Fork docs)
                                            :place-holder "Enter name"
                                            :label        "Name"}]
                                  :class  ""}
                                 {:inputs [{:type         "number"
                                            :field-name   "github-followers"
                                            :place-holder "Enter follower count"
                                            :label        "Github followers"}]
                                  :class  ""}]})

(def form-config3 [{:inputs [{:type         "text"
                              :field-name   :name           ; keyword keys
                              :place-holder "Enter name"
                              :label        "Name"}]
                    :class  ""}
                   {:inputs [{:type         "number"
                              :field-name   :github-followers
                              :place-holder "Enter follower count"
                              :label        "Github followers"}]
                    :class  ""}
                   {:inputs [{:type         "text"
                              :field-name   :location
                              :place-holder "Enter location"
                              :label        "Location"}]
                    :class  ""}])

