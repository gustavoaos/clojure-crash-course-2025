(ns clojure-crash-course-2025.common.utils)

(defn add
  [x y]
  (+ x y))

(defn str->int
  "Convert string to integer value"
  [str]
  (Integer. str))