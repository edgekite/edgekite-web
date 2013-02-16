(ns edgekite.routing
  (:use [clojure.string :only [split]]))

(defn ggu [url-map]
  (fn [id]
    (url-map id)))

(defn wrap-segment-uri [handler]
  (fn [request]
    (let [segs (rest (split (:uri request) #"/"))
          req (assoc request :segments segs)]
      (handler req))))

(defn router [routes]
  (fn [req]
    (let [u (:uri req)
          d (:default routes)
          r (routes u d)]
      (r req))))
