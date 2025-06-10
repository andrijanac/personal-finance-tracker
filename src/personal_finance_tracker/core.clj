(ns personal-finance-tracker.core
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]))

(def db-path "src/personal_finance_tracker/my-budget.json")

(defn read-db []
  (with-open [reader (io/reader db-path)]
    (json/parse-string (slurp reader) true)))

(defn write-db [data]
  (with-open [writer (io/writer db-path)]
    (.write writer (json/generate-string data {:pretty true}))))

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

(defn reset-db []
  (let [empty-data {:income [] :expenses []}]
    (write-db empty-data)
    (println "Database has been reset.")
    empty-data))

(defn -main []
  (loop []
    (println "Welcome to Personal Finance Tracker!")
    (println "Choose an option:")
    (println "1. View summary")
    (println "2. Add income")
    (println "3. Add expense")
    (println "4. Reset database")
    (println "5. Exit")
    (print "Enter choice: ") (flush)
    (let [choice (read-line)]
      (case choice
        "1" (do
              (println "\nCurrent Financial Summary:")
              (println "Total Income: " (total-income))
              (println "Total Expenses: " (total-expenses))
              (println "Remaining Budget: " (remaining-budget))
              (when-let [warning (budget-warning)]
                (println warning))
              (println) (recur))
        "2" (do
              (print "Enter income category: ") (flush)
              (let [category (read-line)]
                (print "Enter amount: ") (flush)
                (let [amount (Double/parseDouble (read-line))]
                  (print "Enter date (yyyy-MM-dd): ") (flush)
                  (let [date (read-line)]
                    (add-income category amount date)
                    (println "Income added.\n"))))
              (recur))
        "3" (do
              (print "Enter expense category: ") (flush)
              (let [category (read-line)]
                (print "Enter amount: ") (flush)
                (let [amount (Double/parseDouble (read-line))]
                  (print "Enter date (yyyy-MM-dd): ") (flush)
                  (let [date (read-line)]
                    (add-expense category amount date)
                    (println "Expense added.\n"))))
              (recur))
        "4" (do
              (reset-db)
              (println)
              (recur))
        "5" (println "Thank you for using Personal Finance Tracker!")
        (do
          (println "Invalid choice. Please try again.\n")
          (recur))))))




