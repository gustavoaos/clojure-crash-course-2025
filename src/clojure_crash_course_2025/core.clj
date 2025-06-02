(ns clojure-crash-course-2025.core
  (:gen-class)
  (:require [clojure-crash-course-2025.common.utils :as utils]))

(defn -main
  []
  (println "Clojure Crash Course:"
           (utils/add 2000 25)))

; Global variables & primitives
(defn add [x y]
  (println (+ x y))
  "calculated")

(fn [name] (str "Hello, " name "!"))

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
(def v [1 2 3 4 5])

; map
(map (fn [element]
       (+ element 1)) v)

; filter
(filter (fn [element]
          (odd? element)) v)

; remove
(remove (fn [element]
          (odd? element)) v)

; reduce
(reduce (fn [acc elem]
          (+ acc elem)) v)

(reduce (fn [acc elem]
          (println (+ acc elem)) (+ acc elem)
          ) v)

(reduce (fn [acc elem]
          (let [result (+ acc elem)]
            (println result) result)
          ) v)

; group-by
(group-by even? v)                                     ; returns a map

; frequencies
(frequencies v)

; Threads and concurrency
(defn ft [name]
  (future
    (Thread/sleep 1000)
    (println "Hi from" name)))

(do
  (ft "gus")
  (println "Hi from now"))

; atomic operations
(def a (atom 0))
(do
  (future (swap! a inc))
  (future (swap! a inc)))

(deref a)
