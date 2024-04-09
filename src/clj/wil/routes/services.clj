(ns wil.routes.services
  (:require
   #_[clojure.tools.logging :as log]
   [wil.goods :refer [create-good-bad! good-bad goods-bads good-bad-sent]]
   [wil.notes :refer [create-note! notes-login get-note date-notes-randomly
                      notes-all]]
   [wil.middleware :as middleware]))

(defn services-routes []
  ["/api"
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-restricted
                 middleware/wrap-formats]}
   ["/note"     {:post create-note!}]
   ["/note/:id" {:get get-note}]
   ["/notes/:login"   {:get notes-login}]
   ["/notes-all"      {:get notes-all}]
   ["/notes/:date/:n" {:get date-notes-randomly}]
   #_["/list/:date"     {:get list-notes}]
   ["/good" {:post create-good-bad!
             :get good-bad}]
   ["/good-sent" {:get good-bad-sent}]
   ["/goods-bads/:date" {:get goods-bads}]])
