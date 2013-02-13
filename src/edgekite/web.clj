(ns edgekite.web
  (:use [clojure.string :only [split]]
        [ring.middleware.resource]
        [ring.middleware.params]
        [hiccup.core]
        [hiccup.page]))

(def state (atom {}))

(defn store [k v]
  (swap! state #(assoc % k v)))

(defn css [req]
  {:status 200
   :headers {"Content-Type" "text/css"}
   :body ""})

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
  (let [name ((:params req) "name" "")]
    (store name (java.util.Date.))
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (page "edgekite" (str "Welcome " name " to edgekite."))}))

(defn guests [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (page "guests" (map #(do [:div (first %) " - " (second %)]) (seq @state)))})

(defn four-oh-four [req]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (page "Four, Oh! Four." "Errrm...")})

(def routes
  {"/"          home
   "/guests"    guests
   :default     four-oh-four})

(defn router [req]
  (let [u (:uri req)
        d (:default routes)
        r (routes u d)]
    (r req)))

(defn wrap-segment-uri
  [handler]
  (fn [request]
    (let [segs (rest (split (:uri request) #"/"))
          req (assoc request :segments segs)]
      (handler req))))

(def handler
  (-> router
      (wrap-resource "public")
      wrap-params
      wrap-segment-uri))
