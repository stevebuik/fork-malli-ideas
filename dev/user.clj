(ns user
  (:require [shadow.cljs.devtools.api :as shadow]))

(comment

  ; start the local server
  (require '[shadow.cljs.devtools.server :as server])
  (server/start!)
  (require '[shadow.cljs.devtools.api :as shadow])
  (shadow/watch :devcards)

  ; build for github pages
  (shadow/release :devcards)

  ; stop the local watch
  (shadow/stop-worker :devcards)

  )