(ns wil.corona-absents
  (:require
   #_[hyperfiddle.rcf :refer [tests]]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [wil.db.core :as db]))

;; (hyperfiddle.rcf/enable!)

(def absents (-> (io/file "resources/data/corona-absents.txt")
                 slurp
                 (str/split-lines)))

(defn sid-dates [line]
  (let [[sid _ _ & dates] (str/split line #"\s")]
    [sid dates]))

;; (tests
;;   (count(map sid-dates absents)) := 23
;;   )

(defn clear-corona!
  "clear corona table"[]
  (db/clear-corona!))

(defn insert-corona!
  [sid date]
  (log/info "insert-corona!" sid date)
  (db/insert-corona! {:sid sid :date date}))

;; (tests
;;   (clear-corona!)
;;   (insert-corona! "hkim" "2022-12-28")
;;   (let [ret (db/list-corona)]
;;     (count ret) := 1
;;     (:sid (first ret)) := "hkim"
;;     (:date (first ret)) := "2022-12-28"))

(defn ->date
  "not a generic way"
  [s]
  (let [[mm dd] (str/split s #"/")]
    (str "2020-" mm "-" dd)))

(defn insert-absents
  [s]
  (clear-corona!)
  (doseq [[sid dates] (map sid-dates s)]
    ;; (log/debug sid dates)
    (doall (map #(insert-corona! sid (->date %)) dates))))

;; (tests
;;  (insert-absents absents)
;;  (let [ret (db/list-corona)]
;;    (count ret) := 25))
