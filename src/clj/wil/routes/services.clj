(ns wil.routes.services
  (:require
   [clojure.tools.logging :as log]
   [wil.notes :refer [create-note notes-self notes-date]]
   [wil.middleware :as middleware]))

(defn services-routes []
  ["/api"
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/note" {:post create-note}]
   ["/notes/:login" {:get notes-self}]
   ["/notes/:login/:date" {:get notes-date}]])