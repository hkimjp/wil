(ns wil.notes
 (:require
  [wil.db.core :as db]
  [ring.util.http-response :as response]))

(defn create-note
  [request]
  (response/ok request))

(defn notes-self
  [{{:keys [login]} :path-params}]
  (response/ok {:login login}))

(defn notes-date
  [{{:keys [login date]} :path-params}]
  (response/ok {:notes-date {:login login
                             :date date}}))
