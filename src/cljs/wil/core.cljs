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

(def ^:private version "0.6.1")

(defonce session (r/atom {:page :home}))
(defonce notes   (r/atom nil))
(defonce params  (r/atom nil))

;; -------------------------
;; misc functions

(defn reset-notes!
  "get the notes list from `/api/notes/:login`,
   set it in r/atom `notes`."
  []
  (GET (str "/api/notes/" js/login)
    {:handler  (fn [ret]  (reset! notes ret))
     :error-handler (fn [^js/Event e] (js/alert (.getMessage e)))}))

(defonce others (r/atom nil))



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
     :handler #(reset-notes!)
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
;; view notes

(defn my-note
  "r/atom notes から id を拾って表示。"
  []
  (let [note (first (filter #(= (:id @params) (str (:id %))) @notes))]
    [:section.section>div.container>div.content
     [:h2 (:login note) ", " (:date note)]
     [:div {:dangerouslySetInnerHTML
            {:__html (md->html (:note note))}}]]))

;; FIXME: id str? int?
(defn send-good-bad
  [stat mark id]
  [:button {:on-click
            (fn [_]
              (POST (str "/api/" stat)
                {:params {:from js/login :to id}
                 :handler #(js/alert (str "sent " stat))
                 :error-handler (fn [^js/Event e]
                                  (js/alert (.getMessage e)))}))}
           mark])

(defn others-notes-page
  "/api/notes/:date/:n から notes を取得。"
  []
  [:section.section>div.container>div.content
   [:h3 "他の人のノートも参考にしましょう。"]
   [:p "wil は感想じゃない。項目を箇条書きにするんじゃなく、
        自分が今日の授業で何を学んだか、その内容を具体的に書く。"]
   [:hr]
   (for [[i note] (map-indexed vector @others)]
     [:div {:key i}
      [:div
       {:dangerouslySetInnerHTML
        {:__html (md->html (:note note))}}]
      [send-good-bad "good" "👍" (:id note)]
      " "
      [send-good-bad "bad"  "👎" (:id note)]
      [:hr]])])

;; -------------------------
;; home page
;; 過去ノート一覧
;; * 日付から他の人のノート(markdown, add good/bad)
;; * 1st から自分のノート(markdown, view goods/bads)

(defn reset-others!
  [date]
  (GET (str "/api/notes/" date "/5")
    {:handler #(reset! others %)
     :error-handler #(js/alert "get /api/notes error")}))

(defn notes-component []
  (fn []
    ;;(reset-notes!)
    [:div
     [:p "内容が更新されてない時は再読み込み。"]
     [:ol
      (for [[i note] (map-indexed vector @notes)]
        [:p
         {:key i}
         [:button {:on-click (fn [_]
                               (reset-others! (:date note))
                               (swap! session assoc :page :others))}
          (:date note)]
         ", "
         [:a {:href (str "/#/my/" (:id note))}
          (-> (:note note) str/split-lines first)]])]]))

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
  (fn []
    [:section.section>div.container>div.content
     [:h3 js/login "(" js/klass "), What I Learned?"]
     [notes-component]
     (when (or (= js/klass "*")
               (and (today-is-klass-day?) (not (done-todays?))))
       [:button
        {:on-click (fn [_]
                     (reset! note "")
                     (swap! session assoc :page :new-note))}
        "本日の内容を追加"])]))

;; -------------------------
;; pages

(def pages
  {:home #'home-page
   :about #'about-page
   :new-note #'new-note-page
   :my #'my-note
   :others #'others-notes-page})

(defn page []
  [(pages (:page @session))])

;; -------------------------
;; Routes

(def router
  (reitit/router
   [["/" :home]
    ["/about" :about]
    ;; FIXME: coerce to int
    ["/my/:id" :my]
    ["/others/:date" :others]]))

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
  (reset-notes!)
  (mount-components))
