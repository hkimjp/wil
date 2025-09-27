DEST:="ubuntu@tiger.melt.kyutech.ac.jp"

prep:
	npm install

watch:
	npx shadow-cljs watch app

repl:
	lein repl

dev:
	just prep
	just watch &
	just repl

run:
	lein run

uberjar: clean
	lein uberjar

deploy: uberjar
	scp target/uberjar/wil.jar {{DEST}}:wil/wil.jar
	ssh {{DEST}} 'sudo systemctl restart wil'
	ssh {{DEST}} 'systemctl status wil'

clean:
	rm -rf target
	rm -I bak --exec rm
