# WIL

What you learned In this Lecture?

## Unreleased

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

## 0.2.3-SNAPSHOT
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
