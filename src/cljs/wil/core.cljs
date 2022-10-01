(ns wil.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as rdom]
   [goog.events :as events]
   [goog.history.EventType :as HistoryEventType]
   [markdown.core :refer [md->html]]
   [wil.ajax :as ajax]
   [ajax.core :refer [GET POST]]
   [reitit.core :as reitit]
   [clojure.string :as string]
   [cljs-time.core :refer [day-of-week]]
   [cljs-time.format :refer [formatter unparse]]
   [cljs-time.local :refer [local-now]])
  (:import goog.History))

(def ^:private version "0.3.1")

(defonce session (r/atom {:page :home}))
(defonce note    (r/atom ""))
(defonce notes   (r/atom nil))

(defn today
  "returns yyyy-MM-dd"
  []
  (unparse (formatter "yyyy-MM-DD") (local-now)))


(defn nav-link [uri title page]
  [:a.navbar-item
   {:href   uri
    :class (when (= page (:page @session)) "is-active")}
   title])

(defn navbar []
  (r/with-let [expanded? (r/atom false)]
    [:nav.navbar.is-info>div.container
     [:div.navbar-brand
      [:a.navbar-item {:href "/" :style {:font-weight :bold}} "WIL"]
      [:span.navbar-burger.burger
       {:data-target :nav-menu
        :on-click #(swap! expanded? not)
        :class (when @expanded? :is-active)}
       [:span] [:span] [:span]]]
     [:div#nav-menu.navbar-menu
      {:class (when @expanded? :is-active)}
      [:div.navbar-start
       #_[nav-link "#/" "Home" :home]
       [nav-link "https://py99.melt.kyutech.ac.jp" "Py99"]
       [nav-link "https://qa.melt.kyutech.ac.jp" "QA"]
       [nav-link "#/about" "About" :about]
       [nav-link "/logout" "Logout"]]]]))

(defn about-page []
  [:section.section>div.container>div.content
   [:img {:src "/img/warning_clojure.png"}]
   [:p "version " version]])

(defn notes-component []
  [:div
   [:h4 "過去ノート"]
   [:ul
    (for [[i note] (map-indexed vector @notes)]
      [:li {:key i} (:date note) (:note note)])]])

(def ^:private wd
  {"mon" 1, "tue" 2, "wed" 3, "thr" 4, "fri" 5, "sat" 6, "sun" 7})

(defn today-is-klass-day?
  [klass]
  (= (day-of-week (local-now)) (wd klass)))

(defn send-note
  [login date note]
  (POST "/api/note"
    {:params {:login login :date date :note note}
     :handler #(swap! session assoc :page :home)
     :error-handler
     (fn [^js/Event e] (js/alert (str "送信失敗。" (.getMessage e))))}))



(defn done-todays?
  []
  (seq (filter #(= (today) (:date %)) @notes)))

(defn home-page []
  [:section.section>div.container>div.content
   [:h3 js/login " (" js/klass ")"]
   [notes-component]
   (when (and (today-is-klass-day? (subs js/klass 0 3))
              (not (done-todays?)))
     [:button
      {:on-click #(swap! session assoc :page :new-note)}
      "今日の内容メモ"])])

(defn new-note-compoment []
  [:div
   [:div
    [:textarea
     {:id "note"
      :value @note
      :on-change #(reset! note (-> % .-target .-value))}]]
   [:div
    [:button
     {:on-click #(send-note js/login (today) @note)
      :class "button is-primary"}
     "submit"]]])

(defn new-note-page []
  [:section.section>div.container>div.content
   [:h3 js/login "markdown OK"]
   [new-note-compoment]])

(def pages
  {:home #'home-page
   :new-note #'new-note-page
   :about #'about-page})

(defn page []
  [(pages (:page @session))])

;; -------------------------
;; Routes

(def router
  (reitit/router
   [["/" :home]
    ["/about" :about]]))

(defn match-route [uri]
  (->> (or (not-empty (string/replace uri #"^.*#" "")) "/")
       (reitit/match-by-path router)
       :data
       :name))
;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     HistoryEventType/NAVIGATE
     (fn [^js/Event.token event]
       (swap! session assoc :page (match-route (.-token event)))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn ^:dev/after-load mount-components []
  (rdom/render [#'navbar] (.getElementById js/document "navbar"))
  (rdom/render [#'page] (.getElementById js/document "app")))

(defn get-notes
  [login]
  (GET (str "/api/notes/login/" login)
    {:handler #(reset! notes %)
     :error-handler (fn [^js/Event e] (js/alert (.getMessage e)))}))

;; FIXME: rewrite using get-notes.

;; (defn done-todays?
;;   [login date]
;;   (GET "/api/note"
;;     {:params {:login login :date date}
;;      :handler #(reset! submit? (seq %))
;;      :error-handler (fn [^js/Event e] (js/alert (.getMessage e)))}))

(defn init! []
  (ajax/load-interceptors!)
  (hook-browser-navigation!)
  (get-notes js/login)
  (mount-components))
