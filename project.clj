(defproject edgekite-web "0.1.0-SNAPSHOT"
  :description "edgekite web site"
  :url "http://edgekite.com/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ring "1.2.0-beta1"]
                 [hiccup "1.0.2"]
                 [gudu "0.1.0-SNAPSHOT"]]
  :plugins [[lein-immutant "0.17.1"]]
  :ring {:handler edgekite.web/handler}
  :immutant {:context-path "/"
             ;;:nrepl-port 7575
             })
