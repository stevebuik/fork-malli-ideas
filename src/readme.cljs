(ns readme
  (:require [cljs.pprint :refer [pprint]]
            [devcards.core :refer [defcard]]
            [sablono.core :refer [html]]))

(defcard hello
         (html
           [:div
            [:p "what is this?"]
            [:p "an example of how to build forms using Re-frame, Fork and Malli"]
            [:p "look at the other cards to find the samples. Start with basics."]
            [:p "what can you learn?"]
            [:ul
             [:li "how to connect the 3 libs together"]
             [:li "how to design composable util functions for as many form variations as possible"]
             [:li "how/when/where state is updated i.e. data flows"]]
            [:p "how can you help?"]
            [:ul
             [:li "pose/answer questions below each sample form"]
             [:li "engage/discuss in the Re-Frame or Malli forums on the interwebs"]
             [:li "pull requests with enhancements demonstrating ideas / improvements"]]]))

(defcard readme-driven-development
         {:goals     {1 "demo forms using Fork with validation using Malli"
                      2 "show abstraction from CSS lib used i.e. supports Bulma and others"
                      3 "sample (and discuss) composability of fns / design"}
          :non-goals {1 "how to build Re-Frame apps"}
          :extras    {1 "figure out how to get markdown working in devcards"}})




