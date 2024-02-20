# wil

What you learned In this Lecture?
Authentication https://l22.melt.kyutech.ac.jp/api/user/:login
## CAUSTION
when start, cljs says

```
clj꞉shadow.user꞉> 
; Evaluating 'afterCLJReplJackInCode'
Access the server at http://localhost:3000
```

but listening port is `3020`.

## docker/lein-devcontainer

Add git sudo, and `vscode` user to official colojre:clojure image.

## npm install
see package.json.

    "xmlhttprequest": "^1.8.0"

## start

### VScode Calva
Use vscode.

1. cd to this folder.
2. start vscode by `code .`
3. start REPL
4. choose start your REPL(a.k.a. jack-in)
4. choose 'wil Server + Client' menu.
5. open http://localhost:3020

### emacs cider

1. shadow-cljs watch :app
2. emacs -x cider-jack-in
3. choose [lein]
4. user> (start)
