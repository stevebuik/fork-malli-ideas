(ns bulma
  (:require [cljs.pprint :refer [pprint]]
            [devcards.core :refer-macros [defcard defcard-rg]]
            [core]
            [app]
            [fork.re-frame :as fork]
            [fork.bulma :as b]))

(defn form-in-container
  [validator initial-values]
  [:div {:style {:border  "1px solid lightgrey"
                 :padding "0 0 20px 20px"}}
   [fork/form {:initial-values initial-values
               :validation     validator}
    (fn [{:keys [errors] :as props}]
      [:div {:style {:padding "1rem"}}

       [b/input props
        {:name        "name"
         :label       "Name"
         :placeholder "Enter a Name"
         :type        "text"
         :class       (str "input"                          ; << TODO breaks Bulma input but needed for is-danger to work
                           (when (seq (get errors "name"))
                             " is-danger"))}]
       (core/input-errors (get errors "name"))

       ])]])

(defcard-rg bulma-inputs-with-validation
            (fn [re-frame-sub _]
              [:div {:style {:margin-bottom 30}}

               [:p "Form created from pre-baked Bulma inputs"]

               [:p
                [:span "Important: to load the Bulma css "]
                [:a {:href "/index-bulma.html#!/bulma"} "click here"]]

               [form-in-container
                (core/validator-for-humans app/malli-schema)
                (get @re-frame-sub (:editing @re-frame-sub))]

               (app/questions "bulma"
                              "why does the Bulma field look weird when the 'input' class is included?")

               ])
            app/app-db
            {:inspect-data false})



