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

; Data structures
[1 2 3]                                                     ; vector
{ "a" [1 2], "b" [3 4], 12 5}                               ; map
#{1 2 3}                                                    ; set

; Clojure functions
; fn we are defining here anonymous functions
(def vector [1 2 3 4 5])

; map
(map (fn [element]
       (+ element 1)) vector)

; filter
(filter (fn [element]
          (odd? element)) vector)

; remove
(remove (fn [element]
          (odd? element)) vector)

; reduce
(reduce (fn [acc elem]
          (+ acc elem)) vector)

(reduce (fn [acc elem]
          (println (+ acc elem)) (+ acc elem)
          ) vector)

(reduce (fn [acc elem]
          (let [result (+ acc elem)]
            (println result) result)
          ) vector)

; group-by
(group-by even? vector)                                     ; returns a map

; frequencies
(frequencies vector)
