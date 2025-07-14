(ns personal-finance-tracker.core-test
  (:require [midje.sweet :refer :all]
            [clojure.test :refer :all]
            [personal-finance-tracker.core :refer :all]))

(fact "valid-amount? ensures only positive numbers are valid"
      (valid-amount? 100) => true
      (valid-amount? -50) => false
      (valid-amount? 0) => false
      (valid-amount? "not a number") => false)

(fact "valid-date? ensures the date is in yyyy-MM-dd format"
      (valid-date? "2024-12-08") => true
      (valid-date? "08-12-2024") => false
      (valid-date? "invalid-date") => false
      (valid-date? "") => false)

(fact "total-income calculates the sum of income amounts"
      (with-redefs [read-db (fn [] {:income [{:amount 100} {:amount 200} {:amount 300}]})]
        (total-income)) => 600)

(fact "total-expenses calculates the sum of expense amounts"
      (with-redefs [read-db (fn [] {:expenses [{:amount 50} {:amount 70} {:amount 80}]})]
        (total-expenses)) => 200)

(fact "remaining-budget calculates the difference between income and expenses"
      (with-redefs [read-db (fn [] {:income [{:amount 500} {:amount 300}]
                                    :expenses [{:amount 200} {:amount 100}]})]
        (remaining-budget)) => 500)

(fact "budget-warning triggers when budget is below 10% of total income"
      (with-redefs [total-income (fn [] 1000)
                    remaining-budget (fn [] 50)]
        (budget-warning)) => "⚠️ WARNING: Your remaining budget is below 10% of your total income! ⚠️")

(fact "budget-warning triggers when budget is negative"
      (with-redefs [total-income (fn [] 1000)
                    remaining-budget (fn [] -100)]
        (budget-warning) => "⚠️ WARNING: You are currently over budget. Review your spending to avoid further deficit! ⚠️"))

(fact "budget-warning does not trigger when budget is above 10% of total income"
      (with-redefs [total-income (fn [] 1000)
                    remaining-budget (fn [] 200)]
        (budget-warning)) => nil)

(fact "reset-db clears the database and sets the values to default"
      (with-redefs [write-db (fn [data] data)]
        (reset-db) => {:income [] :expenses []}))
