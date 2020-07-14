(ns basics
  (:require [cljs.pprint :refer [pprint]]
            [devcards.core :refer-macros [defcard defcard-rg]]
            [core]
            [app]
            [fork.re-frame :as fork]))

(defn form-in-container
  "return a fork form inside a box with a border"
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
               [:p "The :header component can display data from local state (as opposed to app-db/subs)"]
               [:p [:span "Validation is "]
                [:a {:href   "https://github.com/stevebuik/fork-malli-ideas/blob/master/src/app.cljs#L15-L20"
                     :target "source"}
                 "defined here."]
                [:span " The Malli schemas can use idiomatic keywords even though Fork maps use String keys.
                This is because the validation fn in this project used Malli 'Value Transformation' to translate the keys
                and use the intended map shapes for both libraries."]]

               [form-in-container
                (assoc app/form-config :header core/form-header)
                (core/validator-for-humans app/malli-schema)
                (->> (:editing @re-frame-sub)
                     (get @re-frame-sub)
                     core/fork-map)]

               ])
            app/app-db
            {:inspect-data false})

(defcard-rg value-coercion
            (fn [re-frame-sub _]
              [:div {:style {:margin-bottom 30}}

               [:p "Coercion of integer values from 'number' inputs. The input returns a string value but the schema expects an integer. Try making the follower count negative or a alpha-numeric"]

               [form-in-container
                app/form-config2
                (core/validator-for-humans app/malli-schema)
                (->> (:editing @re-frame-sub)
                     (get @re-frame-sub)
                     core/fork-map)]

               ])
            app/app-db
            {:inspect-data false})

(defcard-rg switching-records
            (fn [re-frame-sub _]
              [:div {:style {:margin-bottom 30}}

               [:p "Demo of loading different values into a form. Solution was not obvious initially."]
               [:p [:span "The footer fn can use the Fork handler fns. In this case, "]
                [:a {:href   "https://github.com/stevebuik/fork-malli-ideas/blob/master/src/basics.cljs#L74-L83"
                     :target "source"} "the 'reset' handler."]]
               [:p "Each form has its own local state i.e. form above is unaffected by changes to the app-db/sub"]

               [form-in-container
                (merge app/form-config
                       {:header (fn spacer [_] [:p])
                        :footer (fn [{:keys [reset]}]
                                  [:div {:style {:margin-bottom "1rem"}}
                                   [:button {:onClick (fn [_]
                                                        (reset {:values  (core/fork-map (:record2 @re-frame-sub))
                                                                :touched #{}})
                                                        (swap! app/app-db assoc :editing :record2))}
                                    "Load Tommi"]
                                   [:button {:onClick (fn [_]
                                                        (reset {:values  (core/fork-map (:record1 @re-frame-sub))
                                                                :touched #{}})
                                                        (swap! app/app-db assoc :editing :record1))}
                                    "Load Lucio"]])})
                (core/validator-for-humans app/malli-schema)
                (->> (:editing @re-frame-sub)
                     (get @re-frame-sub)
                     core/fork-map)]])
            app/app-db
            {:inspect-data true})
