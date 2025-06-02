(ns clojure-crash-course-2025.brave-and-true)

; if operator
(if true
  "By Zeus's hammer!"                                       ; then-form
  "By Aquaman's trident!")                                  ; optional-else-form

; to do more than one expression/form on if/else conditionals
; use do operator to wrap up multiple expressions
(if false
  (do (println "Success!")
      "By Zeus's hammer!")
  (do (println "Failure!")
      "By Aquaman's trident!"))

; when operator is a combination of if and do, but with no else branch
; used when for do multiples things when a condition is true, and returns nil otherwise
(when true
  (println "Success!")
  "By Zeus's hammer!")

; or operator, returns the first truthy value or the last value
(or false nil :large_I_mean_venti :why_cant_I_just_say_large) ; => :large_I_mean_venti

; and operator, returns the first falsy value or the last truthy value (if no values are falsey)
(and :free_wifi :free_hot_coffee)                           ; => :free_hot_coffee

; naming values with def
; functional programming paradigm, we're binding a value here, not assigning
(def failed-protagonist-names
  ["Larry Potter" "Doreen the Explorer" "The Incredible Bulk"])

; data structures - numbers strings maps
(def ds-numbers [1 2.0 1/5])
(def ds-strings "Sora")
(def ds-maps {:first-name "Larry" :last-name "Potter"})
(hash-map :first-name "Larry" :last-name "Potter")

; keywords could be used as functions
(get (hash-map :first-name "Larry" :last-name "Potter") :first-name)
(:first-name (hash-map :first-name "Larry" :last-name "Potter"))
(:first-name (hash-map :first-name "Larry" :last-name "Potter") "default value")

; functions
; higher-order-functions: functions that can either take a function as an argument
;  or returns a function
; first-class functions: programming languages who support higher-order functions;
;  treat functions as values in the same way you treat data types like numbers and vectors
(defn function-name
  "docstring describing the function (optional)"
  []                                                        ; parameters
  ()                                                        ; function body
  )

; overloading
(defn x-chop
  "Describes the kind of chop you're inflicting on someone"
  ([name chop-type]
   (str "I " chop-type " chop " name "! Take that!"))
  ([name]
   (x-chop name "karate"))
  )

; rest parameters: treated as a list
(defn attack
  "Describes the attacks used by the Pokémon"
  ([attack]
   (str "Pikachu use " attack))
  ([attack & attacks]
   (str "Pikachu, use " attack ", then " (clojure.string/join ", " attacks)))
  )

; destructuring: instruct Clojure on how to associate names with values in lists, maps, sets, vectors.
(defn attack
  "Describes the attacks used by the Pokémon"
  [[first-attack second-attack & other-attacks]]
   (str "Pikachu, start with " first-attack ", then use " second-attack ". "
    "If necessary you can use " (clojure.string/join ", " other-attacks))
  )

; anonymous functions
(fn [x] (* x 3))
#(* % 3)