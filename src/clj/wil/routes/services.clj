(ns wil.routes.services
  (:require
   #_[clojure.tools.logging :as log]
   [wil.goods :refer [create-good-bad! good-bad]]
   [wil.notes :refer [create-note! login-notes get-note date-notes-randomly]]
   [wil.middleware :as middleware]))
()
(defn services-routes []
  ["/api"
   {:middleware [;; middleware/wrap-csrf
                 ;; middleware/wrap-restricted
                 middleware/wrap-formats]}
   ["/note" {:post create-note!}]
   ["/note/:id" {:get get-note}]
   ["/notes/:login" {:get login-notes}]
   ["/notes/:date/:n"  {:get date-notes-randomly}]
   ["/good" {:post create-good-bad!
             :get good-bad}]])
