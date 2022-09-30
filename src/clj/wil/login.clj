(ns wil.login
  (:require
   [buddy.hashers :as hashers]
   [clojure.tools.logging :as log]
   [hato.client :as hc]
   [ring.util.response :refer [redirect]]
   [wil.layout :as layout]
   [wil.middleware :as middleware]))

(def ^:private l22 "https://l22.melt.kyutech.ac.jp")

(defn get-user
  "retrieve str login's info from API.
   note: parameter is a string. cf. (db/get-user {:login login})"
  [login]
  (let [ep (str l22 "/api/user/" login)
        resp (hc/get ep {:as :json})]
    (log/info "login" (get-in resp [:body :login]))
    ;; (log/debug "(:body resp)" (:body resp))
    (:body resp)))

(defn login [request]
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
