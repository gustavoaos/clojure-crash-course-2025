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

; ### Chapter 4 Core Functions in Depth - Exercises ###

; 1. Turn the result of your glitter filter into a list of names.
(defn glitter-filter
  [minimum-glitter records]
   (map :name (filter #(>= (:glitter-index %) minimum-glitter) records)))

; 2. Write a function, append, which will append a new suspect to your list of suspects.
(defn append
  "Appends a new suspect into current suspect list"
  [string]                                                  ; Alice Cullen,8
  (let [suspects (mapify (parse (slurp filename)))]
    (into suspects (mapify (parse string)))
    ))

; 3. Write a function, validate, which will check that :name and :glitter-index are present when you append.
;    The validate function should accept two arguments: a map of keywords to validating functions,
;    similar to conversions, and the record to be validated.

(def validators {:name (complement empty?)
                 :glitter-index integer?})
(defn validate
  "Validate input against key-mapping rules"
  [validators-keys record]
  (println "record " record)
  (reduce (fn [valid? validator-key]
            ;(println "valid? " valid?)
            ;(println "validator-key " validator-key (validator-key record))
            ;(println "validated? " ((get validators validator-key) (validator-key record)))
            ;(println "test: " (and valid? ((get validators validator-key) (validator-key record))))
            (and valid? ((get validators validator-key) (validator-key record))))
          true
          validators-keys))

(defn append
  "Appends a new suspect into current suspect list"
  [string]                                                  ; Alice Cullen,8
  (let [suspects (mapify (parse (slurp filename)))
        suspect (first (mapify (parse string)))]
    (if (true? (validate vamp-keys suspect))
      (into suspects [suspect])
      suspects)
    ))

; 4. Write a function that will take your list of maps and convert it back to a CSV string.
;    You'll need to use the clojure.string/join function.

(defn reduce-row [item] (reduce (fn [row-map vamp-key] (into row-map [(vamp-key item)]))
                                    []
                                    vamp-keys))
(defn parse-row [row-item] (clojure.string/join "," (reduce-row row-item)))

(defn parse-to-csv
  "Convert a list into a single string line on CSV format"
  [items]                                                    ; ({:name "Edward Cullen", :glitter-index 10})
  (clojure.string/join "\n" (map (fn [item] (parse-row item)) items)))

(def suspects (mapify (parse (slurp filename))))
(parse-to-csv suspects)
; => "Edward Cullen,10\nBella Swan,0\nCharlie Swan,0\nJacob Black,3\nCarlisle Cullen,6"

; ### Chapter 5 Functional Programming - Exercises ###

; 1. You used (comp :intelligence :attributes) to create a function that returns a character's intelligence.
;    Create a new function, attr, that you can call like (attr :intelligence), and that does the same thing.
(def character
  { :name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})

(defn attr
  "Get the value for a specific attribute"
  [key]
  (comp key :attributes))

; 2. Implement the comp function.
(defn c-comp
  "Custom comp function"
  [& fns]
  (fn [& args]
    (reduce (fn [acc f] (f acc))                            ; reduce function
            (apply (last fns) args)                         ; init accumulator with applying the first (last) function
            (reverse (butlast fns)))                        ; collection to reduce
    ))

; 2. Implement the assoc-in function.
;    Hint: use the assoc function and define its parameters as [m [k & ks] v].
(assoc-in character [:attributes :age] 12)
; => {:name "Smooches McCutes", :attributes {:intelligence 10, :strength 4, :dexterity 5, :age 12}}

(defn c-assoc-in
  "Custom assoc-in function"
  [m [k & ks] v]                                            ; destruct the list of functions to apply into k and rest
  (if (empty? ks)                                           ; if there are no more attributes to put into m,
    (assoc m k v)                                           ;   then apply value v into m
    (assoc m k (c-assoc-in (get m k {}) ks v)))             ; ex.: (c-assoc-in character [:attributes :age] 12)
                                                            ;   assoc character :attributes
                                                            ; c-assoc-in
                                                            ;  get character :attributes {} ({} if get response is nil, returns {})
                                                            ;   {:intelligence 10, :strength 4, :dexterity 5, :age 12}
  )

; 4. Look up and use the update-in function.
; (update-in m ks f & args)
(update-in character [:attributes :intelligence] inc)
; => {:name "Smooches McCutes", :attributes {:intelligence 11, :strength 4, :dexterity 5}}

; 5. Implement the update-in function.
(defn c-update-in
  "Custom update-in function"
  [m [k & ks] fn & args]
  (println m)
  (if (empty? ks)
    (apply update m k fn args)
    (assoc m k (apply c-update-in (get m k {}) ks fn args)))
  )
(c-update-in character [:attributes :intelligence] inc)
(c-update-in {:attributes {:items [1 2]}} [:attributes :items] conj 3)
