(ns edgekite.web
  (:use [clojure.string :only [split]]
        [ring.middleware.resource]
        [ring.middleware.params]
        [hiccup.core]
        [hiccup.page]))

(def state (atom {}))

(defn store [k v]
  (swap!
   state
   (fn [m]
     (let [x (m k {:first v :count 0})
           c (x :count)
           y (assoc x :last v :count (inc c))]
       (assoc m k y)))))

(defn map->table [m]
  [:table
     (map #(do [:tr [:td (first %)] [:td (str (second %))]]) (seq m))])

(defn debug-req [req]
  (let [headers (:headers req)
        req (dissoc req :headers)]
    [:div
     [:h2 "Request Details"]
     [:h3 "Properties"]
     (map->table req)
     [:h3 "Headers"]
     (map->table headers)]))

(def url-map
  {:home   "/"
   :guests "/guests"
   :style  "style.css"})

(defn gu [id]
  "Generate URL"
  (url-map id))

(def guest-form
  [:form {:action (gu :home)}
   [:input {:name "name"}]
   [:input {:type "submit"}]])

(defn page [title body]
  (html (include-css (gu :style))
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
     :body (page "edgekite" [:div (str "Welcome " name " to edgekite.") guest-form (debug-req req)])}))

(defn guests [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (page
          "guests"
          (map->table @state))})

(defn four-oh-four [req]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (page "Four, Oh! Four." "Errrm...")})

(def routes
  {(gu :home)   home
   (gu :guests) guests
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
