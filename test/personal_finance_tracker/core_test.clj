(ns personal-finance-tracker.core-test
  (:require [midje.sweet :refer :all]
            [clojure.test :refer :all]
            [personal-finance-tracker.core :refer :all]))

(fact "valid-amount? ensures only positive numbers are valid"
      (valid-amount? 100) => true
      (valid-amount? -50) => false
      (valid-amount? 0) => false
      (valid-amount? "not a number") => false)
