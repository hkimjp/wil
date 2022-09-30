# WIL

## Unreleased

## 0.1.1-SNAPSHOT
- .gitignore
  - .calva/output-window
  - .clj-kondo/cache/
  - .lsp/cache/

## 0.1.0
- lein new luminus will +auth +postgres +reagent の後、
  clj -Tantq outdated :upgrade true
  で闇雲にパッケージを新しくすると shadow-cljs watch app でエラー。
  その後、javascrit と shadow-cljs を外してアップグレードのあと、
  javascript と shadow-cljs をアップグレード。
  そしたらエラーにならない。どうなってんだ？

