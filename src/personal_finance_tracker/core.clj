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

(defn valid-amount? [amount]
  (and (number? amount) (pos? amount)))

(defn valid-date? [date]
  (try
    (java.time.LocalDate/parse date)
    true
    (catch Exception _ false)))

(defn add-income [category amount date]
  (when-not (valid-amount? amount)
    (throw (ex-info "Invalid amount! Amount must be a positive number." {:amount amount})))
  (when-not (valid-date? date)
    (throw (ex-info "Invalid date! Date must be in yyyy-MM-dd format." {:date date})))
  (let [data (read-db)
        new-income {:category category :amount amount :date date}
        updated-data (update data :income #(conj % new-income))]
    (write-db updated-data)))

(defn add-expense [category amount date]
  (when-not (valid-amount? amount)
    (throw (ex-info "Invalid amount! Amount must be a positive number." {:amount amount})))
  (when-not (valid-date? date)
    (throw (ex-info "Invalid date! Date must be in yyyy-MM-dd format." {:date date})))
  (let [data (read-db)
        new-expense {:category category :amount amount :date date}
        updated-data (update data :expenses #(conj % new-expense))]
    (write-db updated-data)))

(defn total-income []
  (reduce + (map :amount (:income (read-db)))))

(defn total-expenses []
  (reduce + (map :amount (:expenses (read-db)))))

(defn remaining-budget []
  (let [income (total-income)
        expenses (total-expenses)]
    (- income expenses)))

(defn budget-warning []
  (let [income (total-income)
        budget (remaining-budget)]
    (when (< budget (* 0.1 income))
      "⚠️ WARNING: Your remaining budget is below 10% of your total income! ⚠️")))

(defn -main []
  (println "Welcome to Personal Finance Tracker!")
  (println "Here is your current financial summary:")
  (println "---------------------------------------")
  (println "Total Income: " (total-income))
  (println "Total Expenses: " (total-expenses))
  (println "Remaining Budget: " (remaining-budget)) 
  (when-let [warning (budget-warning)] 
  (println warning))
  (println "---------------------------------------")
  (println "Thank you for using Personal Finance Tracker!"))

