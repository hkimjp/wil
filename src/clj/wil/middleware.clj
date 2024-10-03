(ns wil.middleware
  (:require
   [buddy.auth :refer [authenticated?]]
   [buddy.auth.accessrules :refer [restrict]]
   [buddy.auth.backends.session :refer [session-backend]]
   [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
   [clojure.tools.logging :as log]
   [muuntaja.middleware :refer [wrap-format wrap-params]]
   [ring.adapter.undertow.middleware.session :refer [wrap-session]]
   [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
   [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
   [ring.middleware.flash :refer [wrap-flash]]
   [wil.config :refer [env]]
   [wil.env :refer [defaults]]
   [wil.layout :refer [error-page]]
   [wil.middleware.formats :as formats]))

(defn wrap-internal-error [handler]
  (fn [req]
    (try
      (handler req)
      (catch Throwable t
        (log/error t (.getMessage t))
        (error-page {:status 500
                     :title "Something very bad has happened!"
                     :message "Ask hkimura."})))))

(defn wrap-csrf [handler]
  (wrap-anti-forgery
   handler
   {:error-response
    (error-page
     {:status 403
      :title "Invalid anti-forgery token"})}))

(defn wrap-formats [handler]
  (let [wrapped (-> handler wrap-params (wrap-format formats/instance))]
    (fn [request]
      ;; disable wrap-formats for websockets
      ;; since they're not compatible with this middleware
      ((if (:websocket? request) handler wrapped) request))))

(defn on-error [request response]
  (error-page
   {:status 403
    :title (str "Access to " (:uri request) " is not authorized")}))

(defn wrap-restricted [handler]
  (restrict handler {:handler authenticated?
                     :on-error on-error}))

(defn wrap-auth [handler]
  (let [backend (session-backend)]
    (-> handler
        (wrap-authentication backend)
        (wrap-authorization backend))))

(defn wrap-base [handler]
  (-> ((:middleware defaults) handler)
      wrap-auth
      wrap-flash
      (wrap-session {:cookie-attrs {:http-only true}})
      (wrap-defaults
       (-> site-defaults
           (assoc-in [:security :anti-forgery] false)
           (dissoc :session)))
      wrap-internal-error))

(defn- remote-ip [req]
  (or
   (when-let [cf-connecting-ip (get-in req [:headers "cf-connecting-ip"])]
     cf-connecting-ip)
   (when-let [x-real-ip (get-in req [:headers "x-real-ip"])]
     x-real-ip)
   (:remote-addr req)))

;; deny access from BAN_IP env var.
(defn wrap-ip [handler]
  (fn [request]
    ;; log/info?
    ;; (log/info :ban-ip (env :ban-ip))
    (log/info "remote-ip" (remote-ip request))
    (if (re-matches  (re-pattern (env :ban-ip))
                     (remote-ip request))
      (error-page
       {:status 403
        :title (str "Access to " (:uri request) " is restricted.")})
      (handler request))))
