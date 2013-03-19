(ns edgekite.web
  (:use [edgekite.routing]
        [edgekite.logging]
        [edgekite.static]
        [edgekite.view]
        [ring.middleware.resource]
        [ring.middleware.file-info]
        [ring.middleware.params]
        [ring.util.response]
        [ring.handler.dump])
  (:require [gudu]
            [clojure.java.io :as io]))

(defn ok-html [body]
  (-> (response body)
      (content-type "text/html")))

(defn home [req]
  (ok-html (page nil [:div "Welcome to edgekite."])))

(defn static [req]
  (if-let [id (page-id req)]
    (let [content (-> (str id ".txt")
                      io/resource
                      slurp
                      markup)]
      (ok-html (page id content)))
    nil))

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
  (-> (page "Four, Oh! Four." "Errrm...")
      not-found
      (content-type "text/html")))

(defn map->table [m]
  [:table
   (map (fn [[k v]] do [:tr [:td k] [:td v]]) (seq m))])

(defn log [req]
  (let [render-log-by (fn [id] [:div [:h3 (name id)] (map->table (get-log-by id))])
        body (page "log" (map render-log-by [:ip :url :date]))]
    (ok-html body)))

(def handlers
  {[:home]  home
   [:about] static
   [:hello] hello
   [:log]   log
   [:debug] handle-dump
   :default four-oh-four})

(def handler
  (-> (router handlers)
      wrap-route
      (wrap-resource "public")
      wrap-file-info
      wrap-params
      wrap-log))
