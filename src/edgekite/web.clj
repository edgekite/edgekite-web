(ns edgekite.web
  (:use [hiccup.core]
        [hiccup.page]))

(def css "body { font-family: monospace; margin: 0; padding: 0; color: #6f6950; }
          h1 { padding: 0; margin: 0; }
          #top-bar { padding: 5px 0; }
          #wrapper { margin: 0 auto; width: 800px; }
          #content { background: #d5d0cc; padding: 0 5px; min-height: 500px;  }")

(defn page [body]
  (html (html5
         [:html
          [:head
           [:title "edgekite"]
           [:style css]]
          [:body
           [:div#wrapper
            [:div#content
             [:div#top-bar
              [:h1 "edgekite"]]
             body]]]])))

(defn handler [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (page "Welcome to edgekite.")})
