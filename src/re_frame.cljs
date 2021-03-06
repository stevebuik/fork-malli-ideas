(ns re-frame
  (:require [cljs.pprint :refer [pprint]]
            [devcards.core :refer-macros [defcard defcard-rg]]
            [core :refer [form-in-container malli-transforms]]
            [app]
            [re-frame.core :as rf]
            [malli.core :as m]))

(rf/reg-event-fx ::switch
                 (fn [_ [_ k]]
                   (swap! app/app-db assoc :editing k)
                   {}))

(defn update-reframe-app-db
  [fork-data]
  ; use Malli to coerce the form data back to the app db shape
  (let [app-db-record (m/decode app/malli-schema (:values fork-data) core/malli-transforms)]
    ; update the app db, which causes the (simulated) sub to invoke a reagent re-render
    (swap! app/app-db update :record1 merge app-db-record)))

(defcard-rg
  switching-records-via-subscription
  (fn [re-frame-sub _]
    (let [{:keys [editing] :as sub-data} @re-frame-sub
          subscribed-person (get sub-data editing)]

      [:div {:style {:margin-bottom 30}}

       [:p "Simulate using Fork and Malli in a re-frame env with circular data flowing"]
       [:p "Fork local state visible above the form, re-frame app-db/global state is below"]
       [:p "Load re-frame subscribed records into the form using the buttons below"]
       [:p "When you edit the input value:"]
       [:ol
        [:li "it instantly updates the form local state"]
        [:li "1 second later updates the app db/global state"]
        [:li "the sub re-renders the form"]]
       [:p "TODO how to stop the input from losing focus?"]

       [form-in-container
        {:form-class  ""
         :inner-class ""
         :input-rows  [{:inputs [{:type                  "text"
                                  :field-name            "name" ; string keys (see Fork docs)
                                  :place-holder          "Enter name"
                                  :label                 "Name"
                                  :global-change-handler update-reframe-app-db}]
                        :class  ""}]
         :header      (fn
                        ; NOTE: this is poor design i.e. handles change detection and UI rendering
                        ; it's a hack to illustrate the combination of re-frame and fork-local state and issues that arise
                        [{:keys [values reset]}]

                        (when (not= (:id subscribed-person)
                                    (get values "id"))
                          (reset {:values  (core/fork-map subscribed-person)
                                  :touched #{}}))

                        [:p (values "name")])}
        (core/validator-for-humans app/malli-schema malli-transforms)
        (core/fork-map subscribed-person)]

       ; external re-frame controls causing sub changes to flow into the fork form
       [:div {:style {:margin-bottom "1rem"}}
        [:p "events from re-frame app"]
        [:button {:onClick (fn [_]
                             (rf/dispatch [::switch :record2]))}
         "Load Tommi"]
        [:button {:onClick (fn [_]
                             (rf/dispatch [::switch :record1]))}
         "Load Lucio"]]

       ]))
  app/app-db
  {:inspect-data true})
