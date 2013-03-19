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

(defn wrap-route [handler]
  (fn [req]
    (let [url     (:uri req)
          route   (du url)
          new-req (assoc req :route route)]
      (handler new-req))))

(defn router [handlers]
  (fn [req]
    (let [route   (:route req)
          default (handlers :default)
          handler (handlers route)]
      (or (and handler
               (handler req))
          (default req)))))
