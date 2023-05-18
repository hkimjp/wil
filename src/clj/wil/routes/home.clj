(ns wil.routes.home
 (:require
  [buddy.hashers :as hashers]
  [clojure.tools.logging :as log]
  [hato.client :as hc]
  [ring.util.http-response :as response]
  [wil.env]
  [wil.layout :as layout]
  [wil.middleware :as middleware]
  #_[wil.notes :refer [list-notes]]
  ))

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
    (log/info "api-user" body)
    body))

(defn login-page [request]
  (layout/render request "login.html" {:flash (:flash request)}))

(comment
  wil.env/dev?
  :rcf)

(defn login-post [{{:keys [login password]} :params}]
  (if wil.env/dev?
    (do
      (log/info "login-post dev")
      (-> (response/found "/")
          (assoc-in [:session :identity] login)
          (assoc-in [:session :klass] "*")))
    (let [user (get-user login)]
      (if (and (seq user)
               (= (:login user) login)
               (hashers/check password (:password user)))
        (do
          (log/info "login" login)
          (-> (response/found "/")
              (assoc-in [:session :identity] login)
              (assoc-in [:session :klass] (:uhour user))))
        (do
          (log/info "login faild" login)
          (-> (response/found "/login")
              (assoc :flash "login failure")))))))

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

;; (defn list-texts
;;   "admin 専用メソッド。引数 date の wil 一覧"
;;   [{{:keys [date]} :path-params}]
;;   (let [body (:body (hc/get (str "/api/list/" date) {:as :json}))]
;;     (for [b body]
;;       (:text b))))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/"        {:get home-page}]
   ["/login"   {:get  login-page :post login-post}]
   ["/logout"  {:get logout}]
   ["/profile" {:get profile-page}]
   ;; no use
   #_["/list/:date" {:get list-notes}]
   ])
