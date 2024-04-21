DEST="ubuntu@tiger.melt.kyutech.ac.jp"
VER=0.2.0

all:
	@echo make build
	@echo make uberjar
	@echo make deploy
	@echo make clean

build:
	docker build -t hkim0331/wil:${VER} .

prep:
	npm install

uberjar: clean
	lein uberjar

deploy: uberjar
	scp target/uberjar/wil.jar ${DEST}:wil/wil.jar && \
	ssh ${DEST} 'sudo systemctl restart wil' && \
	ssh ${DEST} 'systemctl status wil'

clean:
	${RM} -rf target
