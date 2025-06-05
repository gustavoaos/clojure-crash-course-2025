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

; let into set
(set [1 2 3 4])                                             ; => #{1 4 3 2}
(into [] (set [1 2 3 4]))                                   ; => [1 4 3 2]

(def pokemon-team [])                                       ; bind [] to pokemon-team
(into pokemon-team (set ["Pikachu" "Bulbasaur"]))           ; add the elements returned by the set function (Pikachu, Bulbasaur)
                                                            ; into the pokemon-team vector
                                                            ; => ["Bulbasaur" "Pikachu"]
                                                            ; notice that pokemon-team vector are still an empty vector [] (ds are immutable in Clojure)
                                                            ; So the into function will get all elements present in pokemon-team, add the new elements
                                                            ; returned by the set function, and then return the new vector ["Bulbasaur" "Pikachu"]
                                                            ; In this particular case, (into [] pokemon-team] (set ["Pikachu" "Bulbasaur"])) would have
                                                            ; the same result, since pokemon-team is an empty vector

; loop
(loop [iteration 0]                                         ; for (int iteration = 0
  (println (str "Iteration " iteration))
  (if (> iteration 3)                                       ; iteration <= 3
    (println "Goodbye!")
    (recur (inc iteration))))                               ; iteration += 1)

; Chapter 3 Do Things - Exercises

; 2. Write a function that takes a number and adds 100 to it.
(defn inc100 [num] (+ num 100))

; 3. Write a function, dec-maker, that works exactly like the function inc-maker except with subtraction.
; (def dec9 (dec-maker 9))
; (dec9 10)
(defn dec-maker
  "Creates a custom decrementor"
  [dec-by]
  (fn [x] (- x dec-by)))                   ; dec-maker is like a factory, that produces another function
                                           ; whom subtracts num from anything I give it.
                                           ; where dec9 uses it to create a function who subtracts 9 (num) from anything pass on it.

(def dec9 (dec-maker 9))                                    ; create a function and binds to dec9
                                                            ;  Use def to store a function returned from another function (like a higher-order function).
(defn dec9 [num] ((dec-maker 9) num))                       ; every time that we call dec9, dec-maker will be called underneath,
                                                            ;  so it is less efficient.
                                                            ;  Use defn to define a new function from scratch.

; 4. Write a function, mapset, that works like map except the return value is a set.
; (mapset inc [1 1 2 2])
; => #{2 3}
(defn mapset                                                ; does NOT work
  [f & values]
  (map (apply f values) values))

(defn mapset                                                ; ALSO does not work
  [f & values]
  (loop [remaining-values values
         final-set #{}]
    (if (empty? remaining-values)
      final-set
      (let [[value & remaining] remaining-values]
        (recur remaining
               (into final-set
                     (apply f value)))))))

; 5. Create a function that's similar to symmetrize-body-parts except that it has to work with weird space
;    aliens with radial symmetry. Instead of two eyes, arms, legs and so on, they have five.
(defn matching-alien-part
  [part]
  (let [alien-matching-parts ["first-" "second-" "third-" "fourth-" "fifth-"]]
    (reduce
      (fn [final-body-parts alien-part]
        (into final-body-parts (set [part
                                     {:name (clojure.string/replace (:name part) #"^first-" alien-part)
                                      :size (:size part)}
                                     ])))
      #{}
      alien-matching-parts)))

(matching-alien-part {:name "mouth" :size 1})
;=> {:name "mouth", :size 1}
(matching-alien-part {:name "first-eye" :size 1})
;=>
;#{{:name "first-eye", :size 1}
;  {:name "second-eye", :size 1}
;  {:name "third-eye", :size 1}
;  {:name "fourth-eye", :size 1}
;  {:name "fifth-eye", :size 1}}

; 6. Create a function that generalizes symmetrize-body-parts and the function you created in Exercise 5.
;    The new function should take a collection of body parts and the number of matching body parts to add.


; Programming to Abstractions
; Clojure defines some core functions in terms of sequence abstraction, not in terms of specific data structures.
;   abstraction: collection of operations (similar to an interface?)
;   sequence functions (seq functions): defined in terms of sequence abstraction, suing first, rest and cons.

(defn titleize
  [topic]
  (str topic " for the Brave and True"))

(map titleize ["Hamsters" "Ragnarok"])
; => ("Hamsters for the Brave and True" "Ragnarok for the Brave and True")

(map titleize {:uncomfortable-thing "Winking"})
; => ("[:uncomfortable-thing \"Winking\"] for the Brave and True")

(map (fn [args] (titleize (second args))) {:uncomfortable-thing "Winking"}) ; anonymous function
(map #(titleize (second %)) {:uncomfortable-thing "Winking"})               ; anonymous function literal
(map (fn [[k v]] (titleize v)) {:uncomfortable-thing "Winking"})            ; destructuring into a key-value pair
; => ("Winking for the Brave and True")

; Abstraction through Indirection
;   polymorphism: Clojure applies indirection by polymorphism, dispatching to different function bodies
;                 based on the type of the argument (that's why sequence functions works with maps, lists, vectors).
;   multiple-arity : Similar, multiple-arity functions dispatch to different function bodies
;                 based on the number of arguments.
; Clojure sequence functions use indirections by applying seq function into his arguments.

; Implement map using reduce
(reduce (fn [arr value] (into arr [(inc value)]))
        []
        [1 3 5 7])
(defn map-reduce
  [values]
  (reduce (fn [arr value] (into arr [(inc value)]))
          []
          values))

; Lazy Seqs
(def pokedex-database
  {0 {:has-won-vgc? false, :name "Bulbasaur"}
   1 {:has-won-vgc? false, :name "Pikachu"}
   2 {:has-won-vgc? true, :name "Pachirisu"}
   3 {:has-won-vgc? false, :name "Charmander"}})

(defn pokemon-related-details
  [pokedex-entry]
  (Thread/sleep 1000)
  (get pokedex-database pokedex-entry))

(defn identify-champions
  [pokedex-entries]
  (first (filter :has-won-vgc? (map pokedex-database pokedex-entries))))

(time (pokemon-related-details 0))
; "Elapsed time: 1005.476041 msecs"
; => {:has-won-vgc? false, :name "Bulbasaur"}

(time (def mapped-details (map pokemon-related-details (range 0 999999))))
; Almost instantly, cause range and map returns a lazy seq. map will only evaluate his value when
;  you try to access the mapped element.
; "Elapsed time: 0.310458 msecs"
; => #'clojure-crash-course-2025.brave-and-true/mapped-details

(time (first mapped-details))
; Clojure chunks its lazy sequences, when try to realize some element, realizes some of the next elements as well,
;  and then, cached it.
; "Elapsed time: 32104.534625 msecs"
; => {:has-won-vgc? false, :name "Bulbasaur"}