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
   [reagent.dom :as rdom]
   [reitit.core :as reitit]
   [wil.ajax :as ajax])
  (:import goog.History))

(def ^:private version "0.16.14")
(def ^:private updated "2024-02-20 13:41:58")

(def shortest-wil "これ以上短い行の WIL は受け付けない" 5)
(def how-many-wil "ランダムに拾う WIL の数" 7)

;; -------------------------
;; r/atom

(defonce session (r/atom {:page :home}))
(defonce notes   (r/atom nil))
(defonce params  (r/atom nil))
(defonce others  (r/atom nil)) ;; 必要か？
(defonce note    (r/atom ""))

;; async
;; (defonce ans (r/atom nil))
;; (defonce goods-bads (r/atom ""))

(defn reset-notes!
  "get the notes from `/api/notes/:login`, set it in `notes` r/atom."
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
  "returns yyyy-MM-dd string."
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

(defn send-note
  [note]
  (POST "/api/note"
    {:params {:login js/login :date (today) :note note}
     :handler #(reset-notes!)
     :error-handler
     (fn [^js/Event e] (js/alert (str "送信失敗。もう一度。" (.getMessage e))))}))

(defonce count-key-up (r/atom 0))

(defn new-note-page []
  ;; section.section じゃないとナビバートのマージンが狭すぎになる。
  [:section.section>div.container>div.content
   [:p "WIL には今日の授業で何を学んだ内容を具体的に書く。メモは取れたか？" [:br]
    "オープン戦は終わりだ。いつまでも 12345 は幼稚園か猿だね。"
    [:br]
    "コピペはブロック。"]
   [:p "送信は１日一回。マークダウン OK."
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
          (< (count (str/split-lines @note)) shortest-wil)
          (js/alert "もうちょっと授業の内容書けないと。今日は何したっけ？")
          (or (< @count-key-up 10)
              (< @count-key-up (count @note)))
          (js/alert (str "コピペは不可。学んでないの裏返し。"))
          :else (do
                  (send-note @note)
                  (swap! session assoc :page :home))))}
     "送信"]]])

;; -------------------------
;; view notes

(defn good-bad
  [coll]
  (let [goods (-> (filter #(pos? (:kind %)) coll) count)
        soso  (-> (filter #(zero? (:kind %)) coll) count)
        bads  (-> (filter #(neg? (:kind %)) coll) count)]
    (str "you have 👍 " goods " 😐 " soso " 👎 " bads ".")))

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
       "👍 😐 👎 ?"]]]))

(defn send-good-bad!
  [stat mark id]
  [:button {:on-click
            (fn [_]
              (POST "/api/good"
                {:params {:from js/login :to id :condition stat}
                 :handler #(js/alert (str "sent " stat "."))
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
       "From: " [:b (:login note)] ", "
       ;;
       ;; FIXME: not displayed.
       ;;
       ;; (js/alert (str (:created_at note)))
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

(defn notes-component []
  (fn []
    [:div
     [:ol
      (for [[i note] (reverse (map-indexed vector @notes))]
        [:p
         {:key i}
         ;; これでページが切り替わるわけは？
         ;; => (defn page [] [(pages (:page @session))])
         [:button.button.is-warning.is-small
          {:on-click (fn [_]
                       (fetch-others! (:date note))
                       (swap! session assoc :page :others))}
          (str (:date note))]
         " "
         [:button.button.is-small
          {:on-click (fn [_]
                       (fetch-goods-bads! (:date note)))}
          "👍 😐 👎"]
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

(defn home-page
  "js/klass はどこでセットしているか？"
  []
  (fn []
    [:section.section>div.container>div.content
     [:h3 js/login "(" js/klass "), What I Learned?"]
     [:p "出席の記録。自分が WIL 書いてない週は他の人の WIL は見れないよ。"]
     [:ul
      [:li [:button.button.is-primary.is-small "本日分を追加"]
       "は、授業当日だけ現れ、一度しかクリックできない。"]
      [:li "左側の"
       [:button.button.is-warning.is-small "yyyy-mm-dd"]
       "は同日の他人ノートをランダムに "
       how-many-wil
       " 件、表示する。"
       "積極的に、👍、😐、👎 つけよう。情けは人の為ならず。"]
      [:li "真ん中の"
       [:button.button.is-prinary.is-small "👍 😐 👎"]
       "はクラス全体の当日いいね、まあまあ、悪いね総数。"]
      [:li "右側のテキストは自分ノートの1行目。"
       "クリックで当日自分ノートを表示する。"
       "自分についた 👍 😐 👎 もそのページから。"]]
     #_[:p "wil に戻るにはメニューの WIL をクリック。ブラウザの「戻る」はすいません、変なところに行きます。"]
     [:br]
     (when (or (admin?)
               (and (today-is-klass-day?) (not (done-todays?))))
       [:button.button.is-primary
        {:on-click (fn [_]
                     (reset! note "")
                     (swap! session assoc :page :new-note))}
        "本日分を追加"])
     [notes-component]
     [:hr]
     [:div "version " version]]))

;; (defn good-pages
;;   []
;;   [:section.section>div.container>div.content
;;    [:h3 "👍: under construction"]
;;    [:p [:a {:href "/#/"} "back"]]])

;; (defn bad-pages
;;   []
;;   [:section.section>div.container>div.content
;;    [:h3 "👎: under construction"]
;;    [:p [:a {:href "/#/"} "back"]]])

;; -------------------------
;; pages

(def pages
  {:home     #'home-page
   :about    #'about-page
   ;; :bad      #'bad-pages
   ;; :good     #'good-pages
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
    ;; ["/bad/:n"  :bad]
    ;; ["/good/:n" :good]
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
  (rdom/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (ajax/load-interceptors!)
  (hook-browser-navigation!)
  (reset-notes!)
  (mount-components))
