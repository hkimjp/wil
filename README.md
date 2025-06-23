# wil

What you learned In this Lecture?

## Authentication

    https://l22.melt.kyutech.ac.jp/api/user/:login

## docker/lein-devcontainer

Add git sudo, and `vscode` user to official colojre:clojure image.

## npm install
see package.json.

    "xmlhttprequest": "^1.8.0"

## start

### sublime

    npm install
    just watch
    just repl
    subl .

### VScode dev container

### VScode Calva

1. cd to this folder.
1. `% npx shadow-cljs watch app`

```
    2025-01-09 21:49:47,633 [main] DEBUG io.undertow - Configuring listener with protocol HTTP for interface 0.0.0.0 and port 9630
    shadow-cljs - server version: 2.28.20 running at http://localhost:9630
    shadow-cljs - nREPL server started on port 7002
    shadow-cljs - watching build :app
```
1. start vscode by `code .`
1. start REPL
1. choose start your REPL(a.k.a. jack-in)
1. (2025-01-09 does not work  'wil Server + Client' menu)
1. instead, choose `Leiningen`

```
(cd /Users/hkim/workspace/wil; lein update-in :dependencies conj '[nrepl,"1.1.1"]' -- update-in :plugins conj '[cider/cider-nrepl,"0.47.1"]' -- update-in '[:repl-options,:nrepl-middleware]' conj '["cider.nrepl/cider-middleware"]' -- repl :headless)
nREPL server started on port 56092 on host 127.0.0.1 - nrepl://127.0.0.1:56092
```

1. open http://localhost:3020


### emacs cider

1. shadow-cljs watch :app
2. emacs -x cider-jack-in
3. choose [lein]
4. user> (start)
