(ns wil.core
  (:require
   [ajax.core :refer [GET POST]]
   ;; [cljs-time.coerce :refer [to-local-date]]
   [cljs-time.core :refer [day-of-week]]
   [cljs-time.format :refer [formatter unparse #_parse-local]]
   [cljs-time.local :refer [local-now]]
   [clojure.string :as str]
   [goog.events :as events]
   [goog.history.EventType :as HistoryEventType]
   [markdown.core :refer [md->html]]
   [reagent.core :as r]
   ;[reagent.dom.client :as rdom-client]
   [reagent.dom :as rdom]
   [reitit.core :as reitit]
   [wil.ajax :as ajax])
  (:import goog.History))

(def ^:private version "2.28.0")
(def ^:private updated "2025-09-27 21:57:18")

(def shortest-wil "これ以上短い行の WIL は受け付けない" 10)
(def how-many-wil "ランダムに拾う WIL の数" 7) ; was 40 is for re-re-exam.

;; -------------------------
;; r/atom

(defonce session (r/atom {:page :home}))

(defonce params  (r/atom nil))
(defonce others  (r/atom nil)) ;; 必要か？
(defonce note    (r/atom ""))
(defonce md      (r/atom "preview"))
(defonce notes   (r/atom nil))
(defonce date-count  (r/atom "boke")) ;; r/atom の必要ない。

(reset! date-count "aho")

;; Warning: Reactive deref not supported in lazy seq,
;; it should be wrapped in doall
(defn reset-notes!
  "get the notes from `/api/notes/:login`, set it in `notes` r/atom."
  []
  (GET (str "/api/notes/" js/login)
    {:handler #(reset! notes %)
     :error-handler (fn [^js/Event e] (js/alert (.getMessage e)))}))

(defn reset-date-count!
  []
  (GET "/api/date-count"
    {:handler #(reset! date-count %)
     :error-hadler (fn [^js/Event e] (js/alert (.getMessage e)))}))

;;-------------------------
;; navbar

(defn nav-link [uri title page]
  [:a.navbar-item
   {:href   uri
    :class (when (= page (:page @session)) "is-active")}
   title])

(def expanded? (r/atom false))

(defn navbar []
;;  (r/with-let [expanded? (r/atom false)]
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
     [nav-link "/logout" "Logout"]]]])

;; -------------------------
;; misc functions

(defn today
  "returns string `yyyy-MM-dd`"
  []
  (unparse (formatter "yyyy-MM-DD") (local-now)))

;; -------------------------
;; about page

(defn about-page []
  [:section.section>div.container>div.content
   [:img {:src "/img/warning_clojure.png"}]
   [:p "version: " version [:br]
    "update: " updated]])

;; -------------------------
;; 今日のノート

;; FIXME: 送信失敗したらnote を戻す。
(defn send-note
  [note]
  (POST "/api/note"
    {:params {:login js/login :date (today) :note note}
     :handler #(do
                 (js/alert "ページを再読み込みで今日のWILを表示。")
                 (reset-notes!))
     :error-handler (fn [^js/Event e]
                      (js/alert (str "送信失敗。もう一度。" (.getMessage e))))}))

;;
(defonce count-key-up (r/atom 0))

(defn new-note-page []
  ;; section.section じゃないとナビバートのマージンが狭すぎになる。
  [:section.section>div.container>div.content
   [:p "今日の授業で何を学んだか、内容を思い出して具体的に書く。"
    [:br]
    "コピペはブロック。"]
   [:p "送信は１日一回。マークダウンで。"
    [:a {:href "https://github.com/yogthos/markdown-clj#supported-syntax"}
     "<https://github.com/yogthos/markdown-clj>"]]
   [:div.columns.gapless
    [:div.column
     [:textarea
      {:id "note"
       :value @note
       :on-key-up #(swap! count-key-up inc)
       :on-change #(let [text (-> % .-target .-value)]
                     (reset! note text)
                     (reset! md (md->html text)))}]
     [:br]
     [:button.button.is-danger
      {:on-click
       (fn [_]
         (cond
           (< (count (str/split-lines @note)) shortest-wil)
           (js/alert "もうちょっと内容書けないと。今日は何した？")
           ;; forbiden pasting.
           ;;  (< @count-key-up (count @note))
           ;;  (js/alert (str "コピペは受け付けない。"))
           :else (do
                   (send-note @note)
                   (swap! session assoc :page :home))))}
      "送信"]]
    [:div.column
     [:div
      {:id "preview"
       :dangerouslySetInnerHTML {:__html @md}}]]]])

;; -------------------------
;; view notes

(defn good-bad
  [coll]
  (let [goods (-> (filter #(pos? (:kind %)) coll) count)
        soso  (-> (filter #(zero? (:kind %)) coll) count)
        bads  (-> (filter #(neg? (:kind %)) coll) count)]
    (str "you have 👍 " goods ", 😐 " soso ", 👎 " bads ".")))

(defn my-note
  "r/atom notes から id を拾って表示。good/bad は js/alert で。"
  []
  (let [note (first (filter #(= (:id @params) (str (:id %))) @notes))]
    ;; ここで呼んだらダメ。前もって reset! しとかなくちゃ。
    ;; (goods-bads (:id note))
    ;; (js/alert (md->html (:note note)))
    [:section.section>div.container>div.content
     [:h2 (:login note) ", " (:date note)]
     [:div {:dangerouslySetInnerHTML
            {:__html (md->html (:note note))}}]
     [:hr]
     [:div
      [:button.button.is-small
       {:on-click
        (fn [_]
          (GET "/api/good"
            {:params {:id (:id note)}
             :handler
             #(js/alert (good-bad %))
             :error-handler
             (fn [^js/Event e] (js/alert (.getMessage e)))}))}
       "received"]]]))

(defn send-good-bad!
  [stat mark id]
  [:button {:on-click
            (fn [_]
              (POST "/api/good"
                {:params {:from js/login :to id :condition stat}
                 :handler (fn [ret] (js/alert ret))
                 :error-handler
                 (fn [^js/Event e]
                   (js/alert (str "error: " (.getMessage e))))}))}
   mark])

(defn others-notes-page
  "/api/notes/:date/:n から notes を取得。"
  []
  [:section.section>div.container>div.content
   [:h3 "他の人のノートも参考にしよう。"]
   [:p "自分の取り組みはどうか？取り組み次第で点数以上の差がつく。当たり前。"]
   [:hr]
   (for [[i note] (map-indexed vector @others)]
     [:div {:key i}
      [:div
       ;; "From: " [:b "********"] ", " ;; was (:login note)
       "From: " [:b (:login note)] ", "
       (subs (.-rep ^js/LocalDateTime (:created_at note)) 0 19) ","]
      [:br]
      [:div
       {:dangerouslySetInnerHTML
        {:__html (md->html (:note note))}}]
      [:br]
      [send-good-bad! "good" "👍" (:id note)]
      " "
      [send-good-bad! "so-so" "😐" (:id note)]
      " "
      [send-good-bad! "bad"  "👎" (:id note)]
      [:hr]])])

;; -------------------------
;; home page
;; 過去ノート一覧

(defn- admin? []
  (or (= js/login "hkimura")
      (= js/login "wil")))

(defn fetch-others!
  "/api/notes/:date/300 からノートをフェッチ、atom others を更新する。"
  [date]
  (GET (str "/api/notes/" date "/300")
    {:handler #(reset! others (if (admin?)
                                %
                                (take how-many-wil %)))
     :error-handler #(js/alert "get /api/notes error")}))

(defn- format-goods-bads
  [gb]
  ;; (str (select-keys gb [:date :goods :so-so :bads]))
  (str
   (:date gb)
   ",\n"
   "👍 " (:goods gb)
   " 😐 " (:so-so gb)
   " 👎 " (:bads gb)))

(defn fetch-goods-bads!
  "/api/goods-bads/:date から goods-bads を取得、atom ans を更新する。"
  [date]
  (let [uri (str "/api/goods-bads/" date)]
    (GET uri
      {:handler #(js/alert (format-goods-bads %))
       :error-handler #(js/alert (str "error: get " uri))})))

(defn cnt [date]
  (-> (filter #(= date (:date %)) @date-count)
      first
      :count))

(defn notes-component []
  (fn []
    [:div
     [:ol
      (doall (for [[i note] (reverse (map-indexed vector @notes))]
               [:p
                {:key i}
                [:button.button.is-warning.is-small
                 {:on-click (fn [_]
                              (fetch-others! (:date note))
                              (swap! session assoc :page :others))}
                 (str (:date note))]
                " ("
                (cnt (:date note))
                ") "
                [:a {:href (str "/#/my/" (:id note))}
                 (-> (:note note)
                     str/split-lines
                     first
                     (str " ..."))]]))]]))

(defn done-todays?
  []
  (seq (filter #(= (today) (:date %)) @notes)))

(def ^:private wd
  {"mon" 1, "tue" 2, "wed" 3, "thu" 4, "fri" 5, "sat" 6, "sun" 7})

(defn today-is-klass-day?
  []
  (or (= js/klass "*")
      (= (day-of-week (local-now)) (wd (subs js/klass 0 3)))))

(defn home-page
  "display wil top-page."
  []
  (fn []
    [:section.section>div.container>div.content
     [:h3 js/login "(" js/klass "), What I Learned?"]
     [:p "出席の記録。自分が WIL 書いてない週は他の人の WIL は見れないよ。"]
     [:ul
      ; for re-re-exam
      [:li [:button.button.is-primary.is-small "本日分を追加"]
       " は、授業当日だけ現れ、送信は一度限り。"]
      [:li [:button.button.is-warning.is-small "yyyy-mm-dd"]
       "は同日の他人ノートをランダムに表示する。
       積極的に👍(いいね）、😐（まあまあ）、👎（悪いね）つけよう。
       情けは人の為ならず。"]
      [:li "右側の" [:span.blue "青いテキスト"] "は自分ノートの1行目。"
       "クリックで当日自分ノートを表示する。そのページ下部の received を押すと自分ノートの人気がわかる。"]
      [:li "自分がこれまでにつけた 👍、😐、👎 のトータルは "
       [:button.button.is-small
        {:on-click
         (fn [_]
           (GET "/api/good-sent"
             {:params {:login js/login}
              :handler
              #(js/alert (good-bad %))
              :error-handler
              (fn [^js/Event e] (js/alert (.getMessage e)))}))}
        "ここ"] " から。"]
      [:li "( ) 内は現在までに届いた WIL の数。カラの時は再読み込みでどうかな？"]]
     [:br]
     (when (or
            ; true ;; for debug
            (admin?)
            (and (today-is-klass-day?) (not (done-todays?))))
       [:button.button.is-primary
        {:on-click (fn [_]
                     (reset! note "")
                     (swap! session assoc :page :new-note))}
        "本日分を追加"])
     [notes-component]
     [:hr]
     [:div "version " version]]))

;; -------------------------
;; pages

(def pages
  {:home     #'home-page
   :about    #'about-page
   :new-note #'new-note-page
   :my       #'my-note
   :others   #'others-notes-page
   :list     #'list})

(defn page []
  [(pages (:page @session))])

;; -------------------------
;; Routes

(def router
  (reitit/router
   [["/"        :home]
    ["/about"   :about]
    ["/my/:id"  :my]
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
  (rdom/render [#'page]   (.getElementById js/document "app")))

;18
;(defn ^:dev/after-load mount-components []
;  (let [container (.getElementById js/document "navbar")
;        root (rdom-client/create-root container)]
;    (rdom-client/render root [#'navbar])
;    (rdom-client/render root [#'page])))

(defn init! []
  (ajax/load-interceptors!)
  (hook-browser-navigation!)
  (reset-notes!)
  (reset-date-count!)
  (mount-components))
