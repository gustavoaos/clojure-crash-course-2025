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
(defn build-body-parts
  [final-body-parts part]
  (into final-body-parts (set [part (matching-part part)])))

(defn symmetrize-body-parts
  "Expects a sequence of maps that have a :name and :size"
  [asym-body-parts]
  (reduce
    build-body-parts                                        ; function
                                                            ; could also use anonymous function as
                                                            ;  (fn [final-body-parts part] (into final-body-parts (set [part (matching-part part)])))
    ; the function takes as arguments the two first items of the collection
    ; or the first item and the initial value of the accumulator when passed on
    []                                                      ; initial accumulator value
    asym-body-parts))                                       ; collection to reduce from

(defn hit
  [asym-body-parts]
  (let [sym-parts (symmetrize-body-parts asym-body-parts)
        body-part-size-sum (reduce + (map :size sym-parts)) ; sum sizes of all sym-parts
        target (rand body-part-size-sum)]                   ; choose a random value to hit from 0 to the total size
    (println (str "target: " target))
    (loop [[part & remaining] sym-parts                     ; destructured sym-parts
                                                            ;  part: first element on sym-parts
                                                            ; remaining: remaining elements on sym-parts
           accumulated-size (:size part)]                   ; starts with the size of the first part on sym-parts
                                                            ;  evaluated only on the first iteration of the loop
      (println (str "current part: " part))
      (println (str "accumulated-size: " accumulated-size))
      (if (> accumulated-size target)
        part                                                ; then-form, returns part
        (recur remaining (+ accumulated-size (:size (first remaining)))))) ; recurs jumps to the loop rebinding new values
                                                                           ;  [part & remaining] remaining
                                                                           ;  accumulated-size (+ accumulated-size (:size (first remaining)))
    ))