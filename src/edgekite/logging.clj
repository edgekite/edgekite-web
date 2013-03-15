(ns edgekite.logging
  (:use [edgekite.store]))

(defn remap [f m]
  "Remap the values of m with f"
  (println m)
  (into {} (for [[k v] m] [k (f v)])))

(def id->f {:ip first
            :url last
            :date (fn [[_ d _]] (.format (java.text.DateFormat/getDateInstance) d))})

(defn get-log-by [id] (remap count (group-by (id->f id) @state)))

(defn wrap-log [handler]
  (fn [req]
    ;; if behind load balancer we want the forwarded IP
    (let [fwd-ip (get-in req [:headers "x-forwarded-for"])
          ip (or fwd-ip (:remote-addr req))
          u (:uri req)]
      (store ip (java.util.Date.) u))
    (handler req)))
