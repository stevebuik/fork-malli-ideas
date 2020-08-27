(ns devcards
  (:require [cljs.pprint :refer [pprint]]
            [devcards.core :refer [start-devcard-ui!]]
            [readme]
            [basics]
            [re-frame]
            [bulma]))

(enable-console-print!)

(defn ^:export init [] (start-devcard-ui!))