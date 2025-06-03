(ns clojure-crash-course-2025.smacking-hobbits)

; The hobbit model
; argument: vector of maps, which map has the name of the body part, and it's relative size
(def asym-hobbit-body-parts
  [{:name "head" :size 3}
   {:name "left-eye" :size 1}
   {:name "left-ear" :size 1}
   {:name "mouth" :size 1}
   {:name "nose" :size 1}
   {:name "neck" :size 2}
   {:name "left-shoulder" :size 3}
   {:name "left-upper-arm" :size 3}
   {:name "chest" :size 10}
   {:name "back" :size 10}
   {:name "left-forearm" :size 3}
   {:name "abdomen" :size 6}
   {:name "left-kidney" :size 1}
   {:name "left-hand" :size 2}
   {:name "left-knee" :size 2}
   {:name "left-thigh" :size 4}
   {:name "left-lower-leg" :size 3}
   {:name "left-achilles" :size 1}
   {:name "left-foot" :size 2}])

; fix the missing right body parts on hobbit's body
(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-") ; regular-expression: replace strings starting
                                                                  ;  with "left-" for "right-"
   :size (:size part)})

(defn symmetrize-body-parts
  "Expects a sequence of maps that have a :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts               ; remaining-asym-parts = asym-body-parts
         final-body-parts []]                               ; final-body-parts = [] (empty vector)
    (if (empty? remaining-asym-parts)
      final-body-parts                                      ; then-form
      (let [[part & remaining] remaining-asym-parts]        ; else-form
                                                            ; let creates a new scope
                                                            ; destructuring: remaining-asym-parts will destruct as
                                                            ;  part: first element on remaining-asym-parts
                                                            ;  remaining: (rest parameters) rest of elements on remaining-asym-parts
                                                            ;   will be bound here as a list
        (recur remaining                                    ; jumps back to the begging of the loop, rebinding the values
               (into final-body-parts                       ; into function adds the set result into the vector final-body-parts
                     (set [part (matching-part part)]))))))); creates a set of part, and it's matching part (unique elements)

; refactor with reduce function
; reduce abstracts the task process a collection and build a result
(defn symmetrize-body-parts
  "Expects a sequence of maps that have a :name and :size"
  [asym-body-parts]
  (reduce
    (fn [final-body-parts part] (into final-body-parts (set [part (matching-part part)]))) ; function
    ; the function takes as arguments the two first items of the collection
    ; or the first item and the initial value of the accumulator when passed on
    []                                                      ; initial accumulator value
    asym-body-parts))                                       ; collection to reduce from