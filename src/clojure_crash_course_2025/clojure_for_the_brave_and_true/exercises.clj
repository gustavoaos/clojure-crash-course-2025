(ns clojure-crash-course-2025.clojure_for_the_brave_and_true.exercises)

; ### Chapter 3 Do Things - Exercises ###
; 2. Write a function that takes a number and adds 100 to it.
(defn inc100
  "Increments 100 to number"
  [number]
  (+ 100 number))

; Using partial
(defn inc-by
  "Creates a custom increment function"
  [num]
  (partial + num))
(def inc100 (inc-by 100))

; 3. Write a function, dec-maker, that works exactly like the function inc-maker except with subtraction.
; (def dec9 (dec-maker 9))
; (dec9 10)
(defn dec-maker
  "Creates a custom decrement function"
  [dec-by]
  (fn [num] (- num dec-by)))

; 4. Write a function, mapset, that works like map except the return value is a set.
; (mapset inc [1 1 2 2])
; => #{2 3}
(defn mapset
  "Apply a function to a vector of values and returns as a set"
  [fn coll]
  (set (map fn coll)))

; Version using reduce instead of map function
(defn mapset
  "Apply a function to a vector of values and returns as a set"
  [func coll]
  (set (reduce
         (fn [new-vector item] (into new-vector [(func item)]))
         []
         coll)))

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

; 6. Create a function that generalizes symmetrize-body-parts and the function you created in Exercise 5.
;    The new function should take a collection of body parts and the number of matching body parts to add.
(defn matching-alien-part
  [part it]
  {:name (str it "-"  (:name part))
   :size (:size part)})

(defn symmetrize-body-parts
  "Expects a sequence of maps that have a :name and :size"
  [asym-body-parts quantity]
  (reduce
    (fn [final-body-parts part]
      (fn [final-body-parts part] (into final-body-parts (set [part (matching-part part)])))
      ; (into final-body-parts (take quantity (repeat (matching-part part)))))
      ; (into final-body-parts (take quantity (repeatedly #(matching-part part)))))
      (into final-body-parts (map #(matching-alien-part part %) (range quantity))))
    []
    asym-body-parts))
