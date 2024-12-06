(ns personal-finance-tracker.core
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]))

(def db-path "src/personal_finance_tracker/my-budget.json")

(defn read-db []
  (with-open [reader (io/reader db-path)]
    (json/parse-string (slurp reader) true)))

(defn write-db [data]
  (with-open [writer (io/writer db-path)]
    (.write writer (json/generate-string data))))

(defn -main []
  (println "Welcome to Personal Finance Tracker!")
  (println "Database content: " (read-db)))