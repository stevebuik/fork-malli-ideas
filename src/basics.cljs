(ns basics
  (:require [cljs.pprint :refer [pprint]]
            [devcards.core :refer-macros [defcard defcard-rg]]
            [core]
            [app]
            [fork.re-frame :as fork]))

(defn form-in-container
  [config validator initial-values]
  [:div {:style {:border  "1px solid lightgrey"
                 :padding "0 0 20px 20px"}}
   [fork/form {:initial-values initial-values
               :validation     validator}
    (core/multi-row-form config)]])

(defcard-rg simple-validations
            (fn [re-frame-sub _]
              [:div {:style {:margin-bottom 30}}

               [:p "Basic form: both fields have validation. try clearing the name or changing the location"]
               [:p [:span "Validation is "]
                [:a {:href "https://github.com/stevebuik/fork-malli-ideas/blob/master/src/basics.cljs#L17"}
                 "defined here"]]

               [form-in-container
                (-> app/form-config
                    (assoc :header core/form-header))
                (core/validator-for-humans app/malli-schema)
                (get @re-frame-sub (:editing @re-frame-sub))]

               [app/questions "simple"
                "can the validation fn be pre-compiled and still used with m/explain?"]])
            app/app-db
            {:inspect-data false})

(defcard-rg switching-records
            (fn [re-frame-sub _]
              [:div {:style {:margin-bottom 30}}

               [:p "Demo of loading different values into a form. Solution was not obvious initially."]
               [:p [:span "The footer fn can use the Fork handler fns. In this case, "]
                [:a {:href "https://github.com/stevebuik/fork-malli-ideas/blob/master/src/basics.cljs#L80"} "the 'reset' handler."]]
               [:p "Each form has its own local state i.e. form above is unaffected by changes to the app-db/sub"]

               [form-in-container
                (merge app/form-config
                       {:header (fn spacer [_] [:p])
                        :footer (fn [{:keys [reset]}]
                                  [:div {:style {:margin-bottom "1rem"}}
                                   [:button {:onClick (fn [_]
                                                        (reset {:values  (:record2 @re-frame-sub)
                                                                :touched #{}})
                                                        (swap! app/app-db assoc :editing :record2))}
                                    "Load Tommi"]
                                   [:button {:onClick (fn [_]
                                                        (reset {:values  (:record1 @re-frame-sub)
                                                                :touched #{}})
                                                        (swap! app/app-db assoc :editing :record1))}
                                    "Load Lucio"]])})
                (core/validator-for-humans app/malli-schema)
                (get @re-frame-sub (:editing @re-frame-sub))]

               ])
            app/app-db
            {:inspect-data true})
