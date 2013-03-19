(ns edgekite.static
  (:require [clojure.string :as str]))

(defn page-id [req]
  (name (first (req :route))))

(defn markup-line [line]
  (let [bullet? (= \* (first (str/trim line)))]
    [(if bullet? :div.bullet :div) line]))

(defn markup [content]
  (let [lines (str/split-lines content)
        tags (map markup-line lines)]
    tags))
