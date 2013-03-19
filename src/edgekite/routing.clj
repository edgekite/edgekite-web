(ns edgekite.routing
  (:require [gudu]))

(def routes
  {:home   []
   :about  ["about"]
   :hello  ["hello"]
   :log    ["log"]
   :debug  ["debug"]
   :style  ["style.css"]})

(def gu (gudu/gu routes))

(def du (gudu/du routes))

(defn router [handlers]
  (fn [req]
    (let [route   (:route req)
          default (handlers :default)
          handler (handlers route)]
      (or (and handler
               (handler req))
          (default req)))))
