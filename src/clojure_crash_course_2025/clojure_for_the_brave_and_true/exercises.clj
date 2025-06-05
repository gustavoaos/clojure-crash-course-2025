(ns clojure-crash-course-2025.clojure_for_the_brave_and_true.exercises)

; ### Chapter 3 Do Things - Exercises ###
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

