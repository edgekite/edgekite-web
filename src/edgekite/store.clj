(ns edgekite.store)

(def state (atom {}))

(defn store [k v]
  (swap!
   state
   (fn [m]
     (let [x (m k {:first v :count 0})
           c (x :count)
           y (assoc x :last v :count (inc c))]
       (assoc m k y)))))
