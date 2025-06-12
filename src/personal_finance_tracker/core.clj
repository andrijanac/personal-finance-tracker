(ns personal-finance-tracker.core
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.util.response :as response]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.resource :refer [wrap-resource]]))

(def db-path "src/personal_finance_tracker/my-budget.json")

(defn read-db []
  (with-open [reader (io/reader db-path)]
    (json/parse-string (slurp reader) true)))

(defn write-db [data]
  (with-open [writer (io/writer db-path)]
    (.write writer (json/generate-string data {:pretty true}))))

(defn valid-amount? [amount]
  (try
    (let [amt (if (string? amount)
                (Double/parseDouble amount)
                amount)]
      (and (number? amt) (pos? amt)))
    (catch Exception _ false)))

(defn valid-date? [date]
  (try
    (java.time.LocalDate/parse date)
    true
    (catch Exception _ false)))

(defn add-income-to-db [category amount date]
  (let [amt (if (string? amount) (Double/parseDouble amount) amount)]
    (when-not (valid-amount? amt)
      (throw (ex-info "Invalid amount! Amount must be a positive number." {:amount amount})))
    (when-not (valid-date? date)
      (throw (ex-info "Invalid date! Date must be in yyyy-MM-dd format." {:date date})))
    (let [data (read-db)
          new-income {:category category :amount amt :date date}
          updated-data (update data :income #(conj % new-income))]
      (write-db updated-data))))

(defn add-expense-to-db [category amount date]
  (let [amt (if (string? amount) (Double/parseDouble amount) amount)]
    (when-not (valid-amount? amt)
      (throw (ex-info "Invalid amount! Amount must be a positive number." {:amount amount})))
    (when-not (valid-date? date)
      (throw (ex-info "Invalid date! Date must be in yyyy-MM-dd format." {:date date})))
    (let [data (read-db)
          new-expense {:category category :amount amt :date date}
          updated-data (update data :expenses #(conj % new-expense))]
      (write-db updated-data))))

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

(defroutes app-routes
  (POST "/add-income" request
    (let [{:keys [category amount date]} (:body request)]
      (try
        (add-income-to-db category amount date)
        (response/response {:status "success" :message "Income added."})
        (catch Exception e
          (response/response {:status "error" :message (.getMessage e)})))))

  (POST "/add-expense" request
    (let [{:keys [category amount date]} (:body request)]
      (try
        (add-expense-to-db category amount date)
        (response/response {:status "success" :message "Expense added."})
        (catch Exception e
          (response/response {:status "error" :message (.getMessage e)})))))

  (GET "/summary" []
    (let [db (read-db)]
      (response/response {:income (:income db)
                          :expenses (:expenses db)
                          :total-income (total-income)
                          :total-expenses (total-expenses)
                          :remaining-budget (remaining-budget)
                          :budget-warning (budget-warning)})))

  (GET "/reset-db" []
    (reset-db)
    (response/response {:status "success" :message "Database reset."}))

  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true})
      wrap-json-response
      (wrap-cors :access-control-allow-origin [#".*"]
                 :access-control-allow-methods [:get :post])
      (wrap-resource "public")))

(defn -main []
  (println "Server running on http://localhost:3000")
  (run-jetty app {:port 3000}))
