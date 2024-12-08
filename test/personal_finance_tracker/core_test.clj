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
