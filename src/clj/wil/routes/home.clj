(ns wil.routes.home
  (:require
   [buddy.hashers :as hashers]
   [clojure.tools.logging :as log]
   [hato.client :as hc]
   [ring.util.http-response :as response]
   [ring.middleware.cors :refer [wrap-cors]]
   [wil.config]
   [wil.layout :as layout]
   [wil.middleware :as middleware]
   [wil.notes :as notes]))

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
  (let [body (:body (hc/get (str api-user login) {:as :json}))]
    ;; (log/info "api-user" body)
    body))

(defn login-page [request]
  (layout/render request "login.html" {:flash (:flash request)}))

(defn- remote-addr
  "check cf-connecting-ip x-real-ip remote-addr in request header
   in order."
  [req]
  ;; (log/debug "remote-addr" req)
  (or
   (get-in req [:headers "cf-connecting-ip"])
   (get-in req [:headers "x-real-ip"])
   (get req :remote-addr)))

(defn login-post! [{{:keys [login password]} :params :as request}]
  (let [remote-addr (remote-addr request)]
    (if (wil.config/env :dev)
      (do
        (log/info "login-post! dev mode, from" remote-addr)
        (-> (response/found "/")
            (assoc-in [:session :identity] login)
            (assoc-in [:session :klass] "*")))
      (let [user (get-user login)]
        (if (and (seq user)
                 (= (:login user) login)
                 (hashers/check password (:password user)))
          (do
            (log/info "login success" login "from" remote-addr)
            (-> (response/found "/")
                (assoc-in [:session :identity] login)
                (assoc-in [:session :klass] (:uhour user))))
          (do
            (log/info "login faild" login)
            (-> (response/found "/login")
                (assoc :flash "login failure"))))))))

(defn logout [_]
  (-> (response/found "/login")
      (assoc :session {})))

(defn profile-page [request]
  (if-let [login (get-in request [:session :identity])]
    {:status 200
     :headers {"content-type" "text/html"}
     :body "under construction"}
    (-> (response/found "/login")
        (assoc :flash "please login"))))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-ip
                 middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/"        {:get home-page}]
   ["/login"   {:get login-page :post login-post!}]
   ["/logout"  {:get logout}]
   ["/profile" {:get profile-page}]
   ["/last/:login"
    {:middleware
     [#(wrap-cors
        %
        :access-control-allow-origin  [#".*\.melt\.kyutech\.ac\.jp.*"
                                       #".*localhost.*"]
        :access-control-allow-methods [:get :post])]}
    ["" {:get notes/last-note}]]])

