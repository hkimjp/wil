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

(def ^:private version "0.7.3")

;; -------------------------
;; r/atom

(defonce session (r/atom {:page :home}))
(defonce notes   (r/atom nil))
(defonce params  (r/atom nil))
(defonce others  (r/atom nil))
(defonce note    (r/atom ""))

(defn reset-notes!
  "get the notes from `/api/notes/:login`,
   set it in `notes` r/atom."
  []
  (GET (str "/api/notes/" js/login)
    {:handler #(reset! notes %)
     :error-handler (fn [^js/Event e] (js/alert (.getMessage e)))}))
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
;; misc functions

(defn today
  "returns yyyy-MM-dd string."
  []
  (unparse (formatter "yyyy-MM-DD") (local-now)))

;; -------------------------
;; about page

(defn about-page []
  [:section.section>div.container>div.content
   [:img {:src "/img/warning_clojure.png"}]
   [:p "version " version]])

;; -------------------------
;; 今日のノート

(defn send-note
  [note]
  (POST "/api/note"
    {:params {:login js/login :date (today) :note note}
     :handler #(reset-notes!)
     :error-handler
     (fn [^js/Event e] (js/alert (str "送信失敗。" (.getMessage e))))}))

(defonce count-key-up (r/atom 0))

(defn new-note-page []
  [:section.section>div.container>div.content
   [:p "WIL には自分が今日の授業で何を学んだか、その内容を具体的に書く。"
       [:br]
       "授業項目の箇条書きや感想文じゃないぞ。コピペは良くない。"]
   [:p "送信は１日一回です。マークダウン OK. "
    [:a {:href "https://github.com/yogthos/markdown-clj#supported-syntax"}
     "<https://github.com/yogthos/markdown-clj>"]]
   [:div
    [:textarea
     {:id "note"
      :value @note
      :on-key-up #(swap! count-key-up inc)
      :on-change #(reset! note (-> % .-target .-value))}]]
   [:div
    [:button.button.is-danger
     {:on-click
      (fn [_]
       (cond
         (< (count (str/split-lines @note)) 7)
         (js/alert "もうちょっと詳しく書いた方が良くないか？")
         (or (< @count-key-up 10)
             (< @count-key-up (count @note)))
         (js/alert (str "コピペはだめだ。タイプしよう。"))
         :else (do
                 (send-note @note)
                 (swap! session assoc :page :home))))}
     "送信"]]])

;; -------------------------
;; view notes

(defn good-bad
  [coll]
  (let [goods (-> (filter #(pos? (:kind %)) coll) count)
        bads  (-> (filter #(neg? (:kind %)) coll) count)]
    (str "you have 👍 " goods ", 👎 " bads ".")))

(defn my-note
  "r/atom notes から id を拾って表示。good/bad は js/alert で。"
  []
  (let [note (first (filter #(= (:id @params) (str (:id %))) @notes))]
    ;; ここで呼んだらダメ。前もって reset! しとかなくちゃ。
    ;; (goods-bads (:id note))
    [:section.section>div.container>div.content
     [:h2 (:login note) ", " (:date note)]
     [:div {:dangerouslySetInnerHTML
            {:__html (md->html (:note note))}}]
     [:hr]
     [:div#goodbad
      [:button.button.is-small
       {:on-click
        (fn [_]
          (GET "/api/good"
            {:params {:id (:id note)}
             :handler
             #(js/alert (good-bad %))
             :error-handler
             (fn [^js/Event e] (js/alert (.getMessage e)))}))}
       "👍｜👎 ?"]]]))

(defn send-good-bad!
  [stat mark id]
  [:button {:on-click
            (fn [_]
              (POST "/api/good"
                {:params {:from js/login :to id :condition stat}
                 :handler #(js/alert (str "sent " stat "."))
                 :error-handler
                 (fn [^js/Event e] (js/alert (.getMessage e)))}))}
   mark])

(defn others-notes-page
  "/api/notes/:date/:n から notes を取得。"
  []
  [:section.section>div.container>div.content
   [:h3 "他の人のノートも参考にしよう。"]
   [:p "真面目に取り組む人もいる。自分の取り組み方はどうか？
        半年後には取り返しがつかない差がつくよ。"]
   [:hr]
   (for [[i note] (map-indexed vector @others)]
     [:div {:key i}
      [:div
       {:dangerouslySetInnerHTML
        {:__html (md->html (:note note))}}]
      [:br]
      [send-good-bad! "good" "👍" (:id note)]
      " "
      [send-good-bad! "bad"  "👎" (:id note)]
      [:hr]])])

;; -------------------------
;; home page
;; 過去ノート一覧

(defn reset-others!
  [date]
  (GET (str "/api/notes/" date "/7")
    {:handler #(reset! others %)
     :error-handler #(js/alert "get /api/notes error")}))

(defn notes-component []
  (fn []
    [:div
     [:p "日付をクリックは同日のノートをランダムに 7 件、
          テキストのクリックは自分ノートを表示する。"
          [:br]
          "リストが更新されてない時は再読み込み。"]
     [:ol
      (for [[i note] (map-indexed vector @notes)]
        [:p
         {:key i}
         [:button.button.is-warning.is-small
          {:on-click (fn [_]
                       (reset-others! (:date note))
                       (swap! session assoc :page :others))}
          (:date note)]
         " "
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
     [:br]
     (when (or (= js/klass "*")
               (and (today-is-klass-day?) (not (done-todays?))))
       [:button.button.is-primary
        {:on-click (fn [_]
                     (reset! note "")
                     (swap! session assoc :page :new-note))}
        "本日分を追加"])]))

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
