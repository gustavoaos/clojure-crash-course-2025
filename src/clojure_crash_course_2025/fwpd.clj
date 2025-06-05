(ns clojure-crash-course-2025.fwpd
  (:require [clojure-crash-course-2025.common.utils :as utils]))

; Forks Washington Police Department FWPD
;  Analyze suspects for potential vampires

(def filename "resources/suspects.csv")
(def vamp-keys [:name :glitter-index])

; Conversions define a map based on vamp-keys. When you pass :name as an argument.
;   conversions will return the associated function to this key.
;   :name -> return function identity
;   :glitter-index -> return function utils/str->int
; So it's basically an alias to those functions based on vamp-keys
(def conversions {:name identity
                  :glitter-index utils/str->int})

; Convert uses the map defined at conversions to apply the returned function to value
(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))
; (convert :glitter-index "3") -> (utils/str->int "3")
;   => 3
; (convert :name "Bella Swan") -> (identity "Bella Swan")
;   => "Bella Swan"

; Since clojure.string/split returns a vector of values, when splitting by collum,
;  for splitting each row into a vector of values, we use map function,
;  which will receive each string row as a value.
(defn parse
  "Convert a CSV into rows of columns"
  [string]                                                  ; "Edward Cullen,10\nBella Swan,0\nCharlie Swan,0\nJacob Black,3\nCarlisle Cullen,6"
  (map #(clojure.string/split % #",")                       ; ["Edward Cullen" 10]
       (clojure.string/split string #"\n")                  ; ["Edward Cullen,10" "Bella Swan,0"]
       ))
; => (["Edward Cullen" "10"] ["Bella Swan" "0"] ["Charlie Swan" "0"] ["Jacob Black" "3"] ["Carlisle Cullen" "6"])

(defn parse-columns [string] (clojure.string/split string #"\n"))
; (parse-columns (slurp filename))
; => ["Edward Cullen,10" "Bella Swan,0" "Charlie Swan,0" "Jacob Black,3" "Carlisle Cullen,6"]

(defn parse-row [string] (clojure.string/split string #","))
; (parse-row "Edward Cullen,10")
; => ["Edward Cullen" "10"]
;

(parse (slurp filename))

(defn assoc-model
  "Return a map like {:name \"Edward Cullen\", :glitter-index \"10\"}"
  [[name glitter-index]]
  (assoc {} :name (convert :name name)
            :glitter-index (convert :glitter-index glitter-index)))
; (assoc-model ["Edward Cullen" "10"])
; => {:name "Edward Cullen", :glitter-index 10}

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\", :glitter-index \"10\"}"
  [rows]                                                    ; (["Edward Cullen" "10"])
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                   {}
                   (map vector vamp-keys unmapped-row)))    ; => ([:name "Edward Cullen"] [:glitter-index "10"])
         rows
    ))

; (mapify (parse (slurp filename)))
; =>
; ({:name "Edward Cullen", :glitter-index 10}
;  {:name "Bella Swan", :glitter-index 0}
;  {:name "Charlie Swan", :glitter-index 0}
;  {:name "Jacob Black", :glitter-index 3}
;  {:name "Carlisle Cullen", :glitter-index 6})

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))
(glitter-filter 3 (mapify (parse (slurp filename))))

; How mapify works
(mapify [["Edward Cullen" "10"]])                           ; => ({:name "Edward Cullen", :glitter-index 10})
; rows [["Edward Cullen" "10"]]
; map iterates over each row on rows
;   unmapped-row ["Edward Cullen" "10"]
; reduce
;  function
;    row-map: accumulator initial value
;    [vamp-key value]: each item on the collection, destructured as vamp-key value
;                      [:name "Edward Cullen"]
;                      vamp-key  :name
;                      value     "Edward Cullen"
;    assoc function will associate each vector as an attribute on the map pass on accumulator argument
;     vamp-key will be used as a key
;     value will converted by convert function
;     1 - {}
;     2 - {:name "Edward Cullen"}
;     3 - {:name "Edward Cullen" :glitter-index 10}
;  {} initial value of accumulator
;  collection to reduce from
;    (map vector vamp-keys unmapped-row)
;    We pass two collections are arguments to this map function
;      [:name :glitter-value] ["Edward Cullen" "10"]
;      this map will iterate on each "collum" and group them as a vector
;    ([:name "Edward Cullen"] [:glitter-index "10"])