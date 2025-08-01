(defproject personal-finance-tracker "0.1.0-SNAPSHOT"
  :description "An application designed to track and manage personal finances."
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [midje "1.10.9"]
                 [cheshire "5.10.0"]
                 [ring "1.9.0"] 
                 [compojure "1.6.2"] 
                 [ring/ring-json "0.5.0"]
                 [ring-cors "0.1.13"]]
  :plugins [[lein-midje "3.2.1"]]
  :main personal-finance-tracker.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

