# WIL

## Unreleased
- textarea のサイズ
- コピペ検知
- みんなのノート
- goods/bads

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

