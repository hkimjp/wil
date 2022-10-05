(ns wil.goods
  (:require
   [clojure.tools.logging :as log]
   [wil.db.core :as db]
   [ring.util.http-response :as response]))

(defn create-good-bad!
  [{{:keys [from to condition]} :params}]
  (let [kind (if (= condition "good") 1 -1)]
    (try
      (db/create-good-bad!
       {:from_ from :to_ to :kind kind})
      (response/ok "inserted")
      (catch Exception e (throw (Exception. (.getMessage e)))))))