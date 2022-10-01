(ns wil.seed
  (:require
   [clojure.string :as str]
   [java-time.api :as jt]
   [wil.db.core :as db]))


(defn str->date [s]
  (->> (str/split s #"-")
       (map #(Integer/parseInt %))
       (apply jt/local-date)))

(defn days-between [from to]
  (let [first-day (str->date from)
        last-day (str->date to)]
    (take-while #(jt/before? % last-day)
                (iterate #(jt/plus % (jt/days 1)) first-day))))

(defn weekdays [from to wd?]
  (->> (days-between from to)
       (filter wd?)
       (map str)))

;; insert tuesdays
(doseq [date (weekdays "2022-10-01" "2023-02-14" jt/tuesday?)]
  (db/create-date! {:wday "tue" :date date}))

;; thursdays
(doseq [date (weekdays "2022-10-01" "2023-02-14" jt/thursday?)]
  (db/create-date! {:wday "thr" :date date}))

;; saturday
(doseq [date (weekdays "2022-10-01" "2023-02-14" jt/saturday?)]
  (db/create-date! {:wday "sat" :date date}))


;; sunday
(doseq [date (weekdays "2022-10-01" "2023-02-14" jt/sunday?)]
  (db/create-date! {:wday "sun" :date date}))
