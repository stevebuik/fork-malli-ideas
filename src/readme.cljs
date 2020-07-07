(ns readme
  (:require [cljs.pprint :refer [pprint]]
            [devcards.core :refer [defcard]]))

(defcard hello-world
         {:goals     {1 "demo forms using Fork with validation using Malli"
                      2 "show abstraction from CSS lib used i.e. supports Bulma and others"
                      3 "sample (and discuss) composability of fns / design"}
          :non-goals {}
          :extras    {1 "figure out how to get markdown working in devcards"}})




