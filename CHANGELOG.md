# WIL

## Unreleased
- cljs の warning: luminus のコード navbar
- hyperfiddle でコンパイルエラーになる
Compiling wil.corona-absents
✅Syntax error macroexpanding at (corona_absents.clj:34:1).
Execution error (ClassCastException) at wil.db.core/fn (core.clj:14).
class mount.core.DerefableState cannot be cast to class clojure.lang.
IFn (mount.core.DerefableState is in unnamed module of loader clojure.
lang.DynamicClassLoader @cc239ba; clojure.lang.IFn is in unnamed 
module of loader 'app')
- skip auth in develop

## 0.9.1 - 2023-05-05
- reverse order

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
