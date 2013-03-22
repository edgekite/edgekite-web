(ns edgekite.routing
  (:require [gudu]))

(def routes
  {:home   []
   :hello  ["hello"]
   :log    ["log"]
   :debug  ["debug"]
   :static [gudu/string-segment]})

(def gu (gudu/gu routes))

(def du (gudu/du routes))
