(ns wil.routes.services
  (:require
   #_[clojure.tools.logging :as log]
   [wil.goods :refer [create-good-bad! good-bad]]
   [wil.notes :refer [create-note! login-notes get-note date-notes-randomly
                      notes-all]]
   [wil.middleware :as middleware]))

(defn services-routes []
  ["/api"
   {:middleware [;; middleware/wrap-csrf
                 middleware/wrap-restricted
                 middleware/wrap-formats]}
   ["/note"     {:post create-note!}]
   ["/note/:id" {:get get-note}]
   ["/notes/:login"   {:get login-notes}]
   ["/notes-all"      {:get notes-all}]
   ["/notes/:date/:n" {:get date-notes-randomly}]
   #_["/list/:date"     {:get list-notes}]
   ["/good" {:post create-good-bad!
             :get good-bad}]])
