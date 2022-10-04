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
   [clojure.string :as str]
   [cljs-time.core :refer [day-of-week]]
   [cljs-time.format :refer [formatter unparse]]
   [cljs-time.local :refer [local-now]])
  (:import goog.History))

(def ^:private version "0.5.6")

(defonce session (r/atom {:page :home}))
(defonce notes   (r/atom nil))
(defonce params  (r/atom nil))

;; -------------------------
;; misc functions

(defn get-notes
  "get the notes list from `/api/notes/login/:login`,
   set it in r/atom `notes`."
  []
  (GET (str "/api/notes/login/" js/login)
    {:handler  (fn [ret]  (reset! notes ret))
     :error-handler (fn [^js/Event e] (js/alert (.getMessage e)))}))

(defn today
  "returns yyyy-MM-dd string."
  []
  (unparse (formatter "yyyy-MM-DD") (local-now)))

;; -------------------------
;; navbar

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
       [nav-link "https://l22.melt.kyutech.ac.jp" "L22"]
       [nav-link "https://py99.melt.kyutech.ac.jp" "Py99"]
       [nav-link "https://qa.melt.kyutech.ac.jp" "QA"]
       [nav-link "#/about" "About" :about]
       [nav-link "/logout" "Logout"]]]]))

;; -------------------------
;; about page

(defn about-page []
  [:section.section>div.container>div.content
   [:img {:src "/img/warning_clojure.png"}]
   [:p "version " version]])

;; -------------------------
;; 今日のノート

(defonce note (r/atom ""))
(defn send-note
  [note]
  (POST "/api/note"
    {:params {:login js/login :date (today) :note note}
     :handler #(constantly nil)
     :error-handler
     (fn [^js/Event e] (js/alert (str "送信失敗。" (.getMessage e))))}))

(defn new-note-page []
  [:section.section>div.container>div.content
   [:p "送信は１日一回です。マークダウン OK."]
   [:div
    [:textarea
     {:id "note"
      :value @note
      :on-change #(reset! note (-> % .-target .-value))}]]
   [:div
    [:button
     {:on-click (fn [_]
                  (send-note @note)
                  (swap! session assoc :page :home))
      :class "button is-primary"}
     "送信"]]])

;; -------------------------
;; view note page

(defn view-note-page
  "r/atom notes から id を拾って表示。"
  []
  (let [note (first (filter #(= (:id @params) (str (:id %))) @notes))]
    [:section.section>div.container>div.content
     [:h2 (:login note) ", " (:date note)]
     [:div {:dangerouslySetInnerHTML
            {:__html (md->html (:note note))}}]]))

;; -------------------------
;; home page
;; 過去ノート一覧
;; * 日付から他の人のノート(markdown, add good/bad)
;; * 1st から自分のノート(markdown, view goods/bads)

(defn notes-component []
  (get-notes)
  [:div
   [:p "内容が更新されてない時は再読み込み。"]
   [:ol
    (for [[i note] (map-indexed vector @notes)]
      [:li
       {:key i}
       [:a {:href (str "/#/view/" (:id note))} (:date note)]
       " "
       (-> (:note note) str/split-lines first)])]])

(defn done-todays?
  []
  (seq (filter #(= (today) (:date %)) @notes)))

(def ^:private wd
  {"mon" 1, "tue" 2, "wed" 3, "thr" 4, "fri" 5, "sat" 6, "sun" 7})

(defn today-is-klass-day?
  []
  (or (= js/klass "*")
      (= (day-of-week (local-now)) (wd (subs js/klass 0 3)))))

(defn home-page []
  [:section.section>div.container>div.content
   [:h3 js/login "(" js/klass "), What I Learned?"]
   [notes-component]
   (when (or (= js/klass "*")
             (and (today-is-klass-day?) (not (done-todays?))))
      [:button
       {:on-click (fn [_]
                    (reset! note "")
                    (swap! session assoc :page :new-note))}
       "本日の内容を追加"])])

;; -------------------------
;; pages

(def pages
  {:home #'home-page
   :about #'about-page
   :new-note #'new-note-page
   :view #'view-note-page})

(defn page []
  [(pages (:page @session))])

;; -------------------------
;; Routes

(def router
  (reitit/router
   [["/" :home]
    ["/about" :about]
    ;; FIXME: coerce to int
    ["/view/:id" :view]]))

(defn path-params [match]
  (when-let [p (:path-params match)]
    (when (seq p)
      (reset! params p))
    match))

(defn match-route [uri]
  (->> (or (not-empty (str/replace uri #"^.*#" "")) "/")
       (reitit/match-by-path router)
       path-params
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

(defn init! []
  (ajax/load-interceptors!)
  (hook-browser-navigation!)
  (get-notes)
  (mount-components))
