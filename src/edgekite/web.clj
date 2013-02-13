(ns edgekite.web
  (:use [clojure.string :only [split]]
        [ring.middleware.resource]
        [ring.middleware.params]
        [hiccup.core]
        [hiccup.page]))

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
   :hello  "/hello"
   :log    "/log"
   :debug  "/debug"
   :style  "/style.css"})

(defn gu [id]
  "Generate URL"
  (url-map id))

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
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (page "edgekite" [:div (str "Welcome to edgekite.")])})

(def hello-form
  [:form {:action (gu :hello)
          :method :post}
   [:label "Name:"]
   [:input {:name "name"}]
   [:input {:type "submit"}]])

(defn hello [req]
  (let [name (get-in req [:params "name"] "Unknown")]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (page "hello" [:div (str "Hello " name) hello-form])}))

(defn debug [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (page "debug" (debug-req req))})

(defn four-oh-four [req]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (page "Four, Oh! Four." "Errrm...")})

(defn wrap-segment-uri [handler]
  (fn [request]
    (let [segs (rest (split (:uri request) #"/"))
          req (assoc request :segments segs)]
      (handler req))))

(def state (atom {}))

(defn store [k v]
  (swap!
   state
   (fn [m]
     (let [x (m k {:first v :count 0})
           c (x :count)
           y (assoc x :last v :count (inc c))]
       (assoc m k y)))))

(defn log [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (page "log" [:div (map->table @state)])})

(defn wrap-log [handler]
  (fn [req]
    (let [ip (:remote-addr req)]
    (store ip (java.util.Date.)))
    (handler req)))

(def routes
  {(gu :home)   home
   (gu :hello)  hello
   (gu :log)    log
   (gu :debug)  debug
   :default     four-oh-four})

(defn router [req]
  (let [u (:uri req)
        d (:default routes)
        r (routes u d)]
    (r req)))

(def handler
  (-> router
      (wrap-resource "public")
      wrap-params
      wrap-segment-uri
      wrap-log))
