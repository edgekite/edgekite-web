(ns edgekite.web
  (:use [hiccup.core]
        [hiccup.page]))

(defn css [req]
  {:status 200
   :headers {"Content-Type" "text/css"}
   :body "body { font-family: monospace; margin: 0; padding: 0; color: #6f6950; }
   h1 { padding: 0; margin: 0; }
   #top-bar { padding: 5px 0; }
   #wrapper { margin: 0 auto; width: 800px; }
   #content { background: #d5d0cc; padding: 0 5px; min-height: 500px; }"})

(defn page [title body]
  (html (include-css "/style.css")
   (html5
    [:html
     [:head [:title "edgekite"]]
     [:body
      [:div#wrapper
       [:div#content
        [:div#top-bar
         [:h1 title]]
        body
        (comment [:div (java.util.Date.)])]]
      (comment [:script "setTimeout(\"location = location\", 1000)"])]])))

(defn home [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (page "edgekite" "Welcome to edgekite.")})

(defn four-oh-four [req]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (page "Four, Oh! Four." "Errrm...")})

(def routes
  {"/" home
   "/style.css" css})

(defn handler [req]
  (let [r (routes (:uri req) four-oh-four)]
    (r req)))
