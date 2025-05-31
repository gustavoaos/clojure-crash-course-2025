(ns clojure-crash-course-2025.core-test
  (:require [clojure.test :refer :all]
            [clojure-crash-course-2025.core :refer :all]))

(deftest a-test
  (testing "FIXED."
    (is (= 1 1))))
