# WIL

What you learned In this Lecture?

## Unreleased
- weeks テーブルの目的は？
- 送信失敗の理由。

## v2.8.397 / 2024-08-23
- gitignored `/out`.
- gitignored `/.cljs_node_repl
- fixed: core.cljs/new-note-page, called md->html twice.
```
{:__html @md}
```
- updated libraries.
| :file       | :name                          | :current | :latest    |
|------------ | ------------------------------ | -------- | -----------|
| project.clj | buddy/buddy-core               | 1.11.423 | 1.12.0-430 |
|             | ch.qos.logback/logback-classic | 1.5.6    | 1.5.7      |
|             | cider/cider-nrepl              | 0.49.0   | 0.49.3     |
|             | hato/hato                      | 0.9.0    | 1.0.0      |
|             | jonase/eastwood                | 1.4.2    | 1.4.3      |
|             | metosin/reitit                 | 0.7.0    | 0.7.1      |
|             | metosin/ring-http-response     | 0.9.3    | 0.9.4      |
|             | mount/mount                    | 0.1.18   | 0.1.19     |
|             | nrepl/nrepl                    | 1.1.2    | 1.3.0      |
|             | org.clojure/clojure            | 1.11.3   | 1.11.4     |
|             | org.postgresql/postgresql      | 42.7.3   | 42.7.4     |
|             | ring-webjars/ring-webjars      | 0.2.0    | 0.3.0      |
|             | ring/ring-core                 | 1.12.1   | 1.12.2     |
|             | ring/ring-devel                | 1.12.1   | 1.12.2     |
|             | thheller/shadow-cljs           | 2.28.8   | 2.28.12    |


## v2.7.392 / 2024-08-21
- forgot to merge remote repository.
```
[develop]% git fetch
[develop]% git merge
```

## v2.7.386 / 2024-08-21
- unhide `login`.
- list all today's WILs.
```
(def how-many-wil "ランダムに拾う WIL の数" 40)
```
- allow paste.
```
          ;;  (or
          ;;   (< @count-key-up 10)
          ;;   (< @count-key-up (count @note)))
          ;;  (js/alert (str "コピペは受け付けない。"))
```

## v2.6.379 / 2024-08-09
for re-re exam.
- allow VPN connection by undef BAN_IP in `wil/start.sh`.


## v2.5
- bulma 1.0.1
```
% clj -Tantq outdated
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
[##################################################] 53/53
All dependencies are up-to-date.
```

## v2.5.368 / 2024-05-03
- updated libraries except bulma 0.9.4.
- logback-classic 1.5.6
- metosin/reitit 0.7.0
- ring/ring-defaults 0.5.0
- thheller/shadow-cljs 2.28.4
- org/clojure 1.11.3

## v2.4.358 / 2024-04-19
- login 名を匿名に。

## v2.3.352 / 2024-04-17
post back reload to prevent plural posts.

## 2.3.346 / 2024-04-16
- check 'already checked or not.
- post /api/good から戻り値を戻すしてアラートに表示する。

## 2.2.337 / 2024-04-10
preview.
- 当日の全WILを拾う。
  ```
  (def how-many-wil "ランダムに拾う WIL の数" 200)
  ```
- interactive markdown preview.

## 2.1.318 / 2024-04-09
display self goods counts in dialog.
- this wil received/sent to all
- how may wil? 50 (was 7)

## 2.0.314 / 2024-04-09
for 2024 classes. should stay bulma 0.9.4?
```
  modified:   CHANGELOG.md
  modified:   env/dev/cljs/wil/app.cljs
  modified:   project.clj
```

- updated bump-version.sh

- Extending an existing JavaScript type - use a different symbol name instead of js/Symbol e.g symbol

- Consider using [org.webjars.npm/bulma "1.0.0" :exclusions [org.webjars.npm/is-number org.webjars.npm/to-regex-range org.webjars.npm/normalize-path org.webjars.npm/picomatch org.webjars.npm/is-glob org.webjars.npm/is-extglob]].

| :file       | :name                                           | :current  | :latest   |
|------------ | ----------------------------------------------- | --------- | ----------|
| project.clj | ch.qos.logback/logback-classic                  | 1.4.11    | 1.5.3     |
|             | cider/cider-nrepl                               | 0.37.1    | 0.47.1    |
|             | clojure.java-time/clojure.java-time             | 1.3.0     | 1.4.2     |
|             | com.google.javascript/closure-compiler-unshaded | v20230802 | v20240317 |
|             | cprop/cprop                                     | 0.1.19    | 0.1.20    |
|             | jonase/eastwood                                 | 1.4.0     | 1.4.2     |
|             | markdown-clj/markdown-clj                       | 1.11.4    | 1.12.1    |
|             | metosin/muuntaja                                | 0.6.8     | 0.6.10    |
|             | mount/mount                                     | 0.1.17    | 0.1.18    |
|             | nrepl/nrepl                                     | 1.0.0     | 1.1.1     |
|             | org.clojure/clojure                             | 1.11.1    | 1.11.2    |
|             | org.clojure/clojurescript                       | 1.11.121  | 1.11.132  |
|             | org.clojure/tools.cli                           | 1.0.219   | 1.1.230   |
|             | org.clojure/tools.logging                       | 1.2.4     | 1.3.0     |
|             | org.clojure/tools.namespace                     | 1.4.4     | 1.5.0     |
|             | org.postgresql/postgresql                       | 42.6.0    | 42.7.3    |
|             | org.webjars.npm/bulma                           | 0.9.4     | 1.0.0     |
|             | org.webjars/webjars-locator                     | 0.47      | 0.52      |
|             | ring/ring-core                                  | 1.10.0    | 1.12.1    |
|             | ring/ring-devel                                 | 1.10.0    | 1.12.1    |
|             | thheller/shadow-cljs                            | 2.25.5    | 2.28.3    |


## 0.16.14 / 2024-02-20
- 期末テストの 311,321,331,341 の回答を wil.notes に取り込んだ．
- l22 に wil/not ユーザを作成した．
- tiger.melt に wil-added-wil.sql を送信, restore.sh.

## 0.15.13 - 2023-11-01
- use same background color at textarea#note with other App's (qa.melt) markdown areas.
```
textarea#note{
  background-color: #cfc;
}
```

## 0.15.12 - 2023-10-13
- 2023-10-01 以前の notes も削除した。(tiger.melt で)

## 0.15.11 - 2023-10-06
- about に updated 表示する。
- 2023-10-01 以降の notes を削除した。(tiger.melt で)

## 0.14.10 - 2023-10-04
- css を bulma の is-button で書き直し。
- 忘れ物。

## 0.13.9 - 2023-10-04
- コンテナが立ち上がらなかったのは db セクションのインデントミス？
- .devcontainer.json
  calva
  emacs.keymap.improved
- docker-compose.yml: use postgres:14.9
- 表示を合わせる。ボタン、ボックス
- hkimura は2時間目も「本日分を追加」できる。
- /css/wil.css を書き換えても vivaldiが読まないのは「ブラウザのキャッシュをクリア」で直った。

## 0.13.8 - 2023-10-03
- core.cljs: / に表示する文言をアップデート。

## 0.13.7 - 2023-09-19
Cannot infer target type in expression

```clojure
;; before
(.-rep (:created_at note))
;; fixed
(.-rep ^js/LocalDateTime (:created_at note)) 0 19)
```

## 0.13.6 - 2023-09-19
- 文言の修正

## 0.13.5 - 2023-09-19
- GET の内側で js/alert

date(create_at)=date(:date) の箇所でつまづいた。

```sql
-- :name goods-bads :? :*
-- :doc retrieve goods/so-so/bads count for date
SELECT count(*) from goods
WHERE date(created_at) = date(:date) and kind=:kind;
```

## 0.13.4 - 2023-09-13
clojure -Tantq outdated :upgrade true

## 0.13.3 - 2023-09-13
deployed to tiger.melt

## 0.13.2 - 2023-09-13
### Added
- systemd/ copy from app.melt:wil
- middleware/wrap-ip
  ban ip connection from VPN.

## 0.13.1 - 2023-09-13
### Changed
- wil.env/dev を見るではく、dev-config.edn を参照する
  (wil.config/env :dev)
- admin? which replaces #(= "hkimura" %)

## 0.13.0 - 2023-09-13
2023 cycle started.

## 0.12.1 - 2023-08-01
- fixed
```
178 |       [:div "From: " [:b (:login note)] ", " (str (.-rep (:created_at note))) ","]
---------------------------------------------------------^----------------------
 Cannot infer target type in expression (. (:created_at note) -rep)
```

by letting `-rep` know that the type of argument is String.

```
(.-rep (str (:created_at note)))
```

## 0.11.1 - 2023-05-21
- added so-so button

## 0.11.0 - 2023-05-19
- dummy links

## 0.10.0 - 2023-05-19
- display from who and when

## 0.9.4 - 2023-05-06
- 文言の修正

## 0.9.3 - 2023-05-06
- clean up Makefile
### Added
- skip auth in develop: wil.env/dev? を定義した
### Changed
- port 3020
- does not staticly export 3020 and 7002 from docker-compose.yml
  Calva does it.

## 0.9.2 - 2023-05-05
### Removed
- 0.9.0 matter, list-notes etc.

## 0.9.1 - 2023-05-05
- reverse order
- hkimura can view all wils

## 0.9.0 - 2023-05-04
### Added
- /list/:date admin(hkimura) only

## 0.8.4 - 2022-12-28
### Added
- src/clj/wil/corona_absents.clj
  resources/data/corona-absents.txt を読んで、
  corona テーブルに sid, date, created_at を追加。
  date コラムの型は wil テーブルの date コラムに合わせて char(10).
  SQL での日付の扱いがまだ甘い。

## 0.7.3 - 2022-10-11
- favicon.ico

## 0.7.2 - 2022-10-05
- コピペ検知

## 0.7.1 - 2022-10-05
- click して good/bad を見れる。

## 0.7.0 - 2022-10-05
- can send good/bad

## 0.6.1 - 2022-10-05
- alter table goods alter to_ type int USING to_::integer;

## 0.6.0 - 2022-10-05
- ランダムに他の人の当日ノートを表示する

## 0.5.6 - 2022-10-04
### Refactor
- remove find-note
- order notes. by date, by id.

## 0.5.5 - 2022-10-03
- textarea のサイズ

## 0.5.4 - 2022-10-03
## 0.5.3 - 2022-10-03
- merged on m2

## 0.5.2 - 2022-10-03
- wil.melt は最新版か？

## 0.5 1 - 2022-10-03
- klass の入力を不要に。データベースから取ってくる。

## 0.5.0 - 2022-10-03
- path params を渡せるようになった。これが正しい方法か、違うだろうな。
- markdown, HTML のエスケープを忘れるな！

## 0.4.2 - 2022-10-02
- リモートコンテナでは、7002, 9360 を転送しなくて良い。
- refactoring

## 0.4.1 - 2022-10-02
- 更新したhome-page へjs/alert なしで。

## 0.4.0 - 2022-10-02
- wil.melt(150.69.90.85) で動かしてみた。
  ノート送信の後の戻りがボタンがくっついたページなのはおかしい。

## 0.3.1 - 2022-10-02
- new-note-page と new-note-compoment.
- 今日のメモへのリンク（ボタン）。再読み込みなしでできないか。

## 0.3.0 - 2022-10-01
- 過去ノートのリスト表示ができた。

## 2.0.314 /
- wrap-restricted
- csrf どうする？

## 0.2.2 - 2022-10-01
- clj/wil/routews/services.clj

## 0.2.1 - 2022-10-01
- remote container
- clj/wil/seed.clj

## 0.2.0 - 2022-10-01
- session に login と klass を渡す(string)
- cljs
- bump-version.sh

## 0.1.1 - 2022-10-01
- .gitignore
  * .calva/output-window
  * .clj-kondo/cache/
  * .lsp/cache/
- login.html `required`

## 0.1.0
- lein new luminus will +auth +postgres +reagent の後、
  clj -Tantq outdated :upgrade true
  で闇雲にパッケージを新しくすると shadow-cljs watch app でエラー。
  その後、javascrit と shadow-cljs を外してアップグレードのあと、
  javascript と shadow-cljs をアップグレード。
  そしたらエラーにならない。どうなってんだ？
