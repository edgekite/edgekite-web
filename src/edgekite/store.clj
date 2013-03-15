(ns edgekite.store)

(def state (atom []))

(defn store [& args]
  (swap! state #(conj % args)))
