(ns edgekite.logging
  (:use [edgekite.store]))

(defn get-log [] @state)

(defn wrap-log [handler]
  (fn [req]
    ;; if behind load balancer we want the forwarded IP
    (let [fwd-ip (get-in req [:headers "x-forwarded-for"])
          ip (or fwd-ip (:remote-addr req))]
      (store ip (java.util.Date.)))
    (handler req)))
