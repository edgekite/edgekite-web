(ns edgekite.routing
  (:require [gudu]))

(def routes
  {:home   []
   :hello  ["hello"]
   :log    ["log"]
   :debug  ["debug"]
   :style  ["style.css"]})

(def gu (gudu/gu routes))

(defn router [handlers]
  (let [du (gudu/du routes)]
    (fn [req]
      (let [u (:uri req)
            r (du u)
            d (handlers :default)
            h (handlers r d)]
        (h req)))))
