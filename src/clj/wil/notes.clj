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

(defn login-notes
  [{{:keys [login]} :path-params}]
  (response/ok (db/login-notes {:login login})))

(defn get-note
  [{{:keys [id]} :params}]
  (response/ok (db/get-note {:id id})))

;; retrieve `date` note randomly `n`
(defn date-notes-randomly
  [{{:keys [date n]} :path-params}]
  (response/ok (db/date-notes-randomly
                {:date date :n (Integer/parseInt n)})))