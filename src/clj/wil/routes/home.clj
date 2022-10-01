(ns wil.routes.home
 (:require
  [buddy.hashers :as hashers]
  [clojure.tools.logging :as log]
  [hato.client :as hc]
  [ring.util.http-response :as response]
  [wil.layout :as layout]
  [wil.middleware :as middleware]))

(defn home-page
  [request]
  (if (get-in request [:session :identity])
    (layout/render request "home.html")
    (response/found "/login")))

(def api-user "https://l22.melt.kyutech.ac.jp/api/user/")
(defn get-user
  "retrieve str login's info from API.
   note: parameter is a string. cf. (db/get-user {:login login})"
  [login]
  (let [ep   (str api-user login)
        resp (hc/get ep {:as :json})]
    (log/info "get-user" (get-in resp [:body :login]))
    (:body resp)))

(defn login-page [request]
  (layout/render request "login.html" {:flash (:flash request)}))

(defn login-post [{{:keys [login password klass]} :params}]
  (let [user (get-user login)]
    (if (and (seq user)
             (= (:login user) login)
             (hashers/check password (:password user)))
      (do
        (log/info "login success" login "klass" klass)
        (-> (response/found "/")
            (assoc-in [:session :identity] login)
            (assoc-in [:session :klass] klass)))
      (do
        (log/info "login faild" login)
        (-> (response/found "/login")
            (assoc :flash "login failure"))))))

(defn logout [_]
  (-> (response/found "/login")
      (assoc :session {})))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/login" {:get  login-page
              :post login-post}]
   ["/logout" {:get logout}]])

