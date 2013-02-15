(ns edgekite.web
  (:use [clojure.string :only [split]]
        [ring.middleware.resource]
        [ring.middleware.file-info]
        [ring.middleware.params]
        [ring.util.response]
        [ring.handler.dump]
        [hiccup.core]
        [hiccup.page]))

(defn map->table [m]
  [:table
     (map #(do [:tr [:td (first %)] [:td (str (second %))]]) (seq m))])

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
         [:h1 title]]
        body
        (comment [:div (java.util.Date.)])]]
      (comment [:script "setTimeout(\"location = location\", 1000)"])]])))

(defn ok-html [body]
  (-> (response body)
      (content-type "text/html")))

(defn home [req]
  (ok-html (page "edgekite" [:div (str "Welcome to edgekite.")])))

(def hello-form
  [:form {:action (gu :hello)
          :method :post}
   [:label "Name:"]
   [:input {:name "name"}]
   [:input {:type "submit"}]])

(defn hello [req]
  (let [name (get-in req [:params "name"] "Unknown")
        body (page "hello" [:div (str "Hello " name) hello-form])]
    (ok-html body)))

(defn four-oh-four [req]
  (-> (not-found (page "Four, Oh! Four." "Errrm..."))
      (content-type "text/html")))

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
  (let [body (page "log" [:div (map->table @state)])]
    (ok-html body)))

(defn wrap-log [handler]
  (fn [req]
    (let [ip (:remote-addr req)]
    (store ip (java.util.Date.)))
    (handler req)))

(def routes
  {(gu :home)   home
   (gu :hello)  hello
   (gu :log)    log
   (gu :debug)  handle-dump
   :default     four-oh-four})

(defn router [req]
  (let [u (:uri req)
        d (:default routes)
        r (routes u d)]
    (r req)))

(def handler
  (-> router
      (wrap-resource "public")
      wrap-file-info
      wrap-params
      wrap-segment-uri
      wrap-log))
