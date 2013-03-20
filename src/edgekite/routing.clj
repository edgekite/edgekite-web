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

(defn router [handlers]
  (fn [req]
    (let [route   (:route req)
          default (handlers :default)
          handler (handlers (first route))]
      (or (and handler
               (handler req))
          (default req)))))
