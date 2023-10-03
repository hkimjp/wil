(ns wil.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [wil.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[wil started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[wil has shut down successfully]=-"))
   :middleware wrap-dev})

;; use wil.config/env instead
;; (def dev? true)
