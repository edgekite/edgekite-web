(ns edgekite.view
  (:use [edgekite.routing]
        [hiccup.core]
        [hiccup.page]))

(def ^:dynamic *timestamp?* false)

(def ^:dynamic *autoreload?* false)

(defn page [title & body]
  (html
   (html5
    [:html
     [:head
      [:title "edgekite"]
      (include-css (gu :style))]
     [:body
      [:div#wrapper
       [:div#content
        [:div#top-bar
         [:h1 "edgekite"]]
        (if title [:h2 title])
        body
        (if *timestamp?* [:div (java.util.Date.)])]]
      (if *autoreload?* [:script "setTimeout(\"location = location\", 1000)"])]])))
