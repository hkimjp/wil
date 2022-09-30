(ns wil.routes.home
  (:require
   [hato.client :as hc]
   [clojure.tools.loggins :as log]
   [wil.layout :as layout]
   #_[wil.db.core :as db]
   #_[clojure.java.io :as io]
   [wil.middleware :as middleware]
   [ring.util.response :refer [redirect]]
   [ring.util.http-response :as response]))

(defn home-page [request]
  (layout/render request "home.html"))

(def api-user "https://l22.melt.kyutech.ac.jp/api/user/")
(defn get-user
  "retrieve str login's info from API.
   note: parameter is a string. cf. (db/get-user {:login login})"
  [login]
  (let [ep   (str api-user login)
        resp (hc/get ep {:as :json})]
    (log/info "login" (get-in resp [:body :login]))
    ;; (log/debug "(:body resp)" (:body resp))
    (:body resp)))

(defn login-page [request]
  (layout/render request "login.html" {:flash (:flash request)}))

(defn login-post [{{:keys [login password]} :params}]
  (let [user (get-user login)]
    (if (and (seq user)
             (= (:login user) login)
             (hashers/check password (:password user)))
      (do
        (log/info "login success" login)
        (-> (redirect "/")
            (assoc-in [:session :identity] (keyword login))))
      (do
        (log/info "login faild" login)
        (-> (redirect "/login")
            (assoc :flash "login failure"))))))

(defn logout [_]
  (-> (redirect "/")
      (assoc :session {})))

(defn login-page
  [request]
  (layout/render request "login.html"))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get (fn [_] (response/ok {:status "under construction"}))}]
   ["/login" {:get  login-page
              :post login-post}]
   ["/logout" {:get logout}]])

