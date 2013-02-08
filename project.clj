(defproject edgekite-web "0.1.0-SNAPSHOT"
  :description "edgekite web site"
  :url "http://edgekite.com/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ring "1.1.8"]
                 [hiccup "1.0.2"]]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler edgekite.web/handler
         :port 2000})
