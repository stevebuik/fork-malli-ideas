(ns user
  (:require [shadow.cljs.devtools.api :as shadow]))

(comment

  (require '[shadow.cljs.devtools.server :as server])
  (server/start!)
  (require '[shadow.cljs.devtools.api :as shadow])
  (shadow/watch :devcards)


  (shadow/stop-worker :devcards)

  )