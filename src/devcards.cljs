(ns devcards
  (:require [cljs.pprint :refer [pprint]]
            [devcards.util.markdown]
            [devcards.core :refer [start-devcard-ui!]
             :refer-macros [defcard defcard-doc deftest dom-node reagent]]
            [readme]
            [basics]))

(enable-console-print!)

(defn ^:export init []
  (start-devcard-ui!))