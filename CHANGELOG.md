# WIL

## Unreleased
- seed
- how to hundle `time`?


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

