DEST:="ubuntu@tiger.melt.kyutech.ac.jp"

prep:
	npm install

watch:
	npx shadow-cljs watch app

dev:
	lein repl

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

# docker
build:
	docker build -t hkim0331/wil:${VER} .

