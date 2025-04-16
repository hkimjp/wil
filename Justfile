DEST:="ubuntu@tiger.melt.kyutech.ac.jp"

all:
	@echo make build
	@echo make uberjar
	@echo make deploy
	@echo make clean

prep:
	npm install

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

