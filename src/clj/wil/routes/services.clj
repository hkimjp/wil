(ns wil.routes.services
  (:require
   #_[clojure.tools.logging :as log]
   [wil.notes :refer [create-note! find-note user-notes date-notes]]
   [wil.middleware :as middleware]))

(defn services-routes []
  ["/api"
   {:middleware [;; middleware/wrap-restricted
                 ;; middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/note" {:post create-note!
             :get find-note}]
   ["/notes/login/:login" {:get user-notes}]
   ["/notes/date/:date"  {:get date-notes}]])
