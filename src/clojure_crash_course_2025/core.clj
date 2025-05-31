(ns clojure-crash-course-2025.core
  (:gen-class)
  (:require [clojure-crash-course-2025.common.utils :as utils]))

(defn -main
  []
  (println "Clojure Crash Course:"
           (utils/add 2000 25)))

; Global variables & primitives
(def my-var 1)

(def my-fn (fn [x y]
             (println (+ x y))
             "calculated"))

(defn add [x y]
  (println (+ x y))
  "calculated")

; Scopes vars with `let` (attention to nested scope)
(let [arg1 1
      arg2 2
      result (add arg1 arg2)]
  (println "Result of adding" arg1 "and" arg2 "equals" result))