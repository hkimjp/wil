(ns wil.preview
  (:require [wil.layout :refer [render]]))

(defn preview [{{:keys [doc]} :params :as request}]
  (println "preview doc" doc)
  (render request "preview.html" {:html doc}))
