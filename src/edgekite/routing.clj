(ns edgekite.routing
  (:use [gudu.core])
  (:require [gudu]))

(def routes
  {:home   root
   :hello  "hello"
   :log    "log"
   :debug  "debug"
   :style  "style.css"
   :static ["content" string-segment]})

(def gu (gudu/gu routes))

(def du (gudu/du routes))
