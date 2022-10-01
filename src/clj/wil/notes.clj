(ns wil.notes
 (:require
  [clojure.tools.logging :as log]
  [wil.db.core :as db]
  [ring.util.http-response :as response]))

(defn create-note!
  [{params :params}]
  (log/info "create-note" params)
  (try
   (db/create-note! params)
   (response/ok {:ok "created"})
   (catch Exception e (throw (.getMessage e)))))

(defn find-note
 [{{:keys [login date]} :path-params}]
 (response/ok (db/find-note {:login login :date date})))

(defn user-notes
  [{{:keys [login]} :path-params}]
  (response/ok (db/user-notes {:login login})))

(defn date-notes
  [{{:keys [date]} :path-params}]
  (response/ok (db/date-notes {:date date})))

