DEST="ubuntu@app.melt.kyutech.ac.jp"

all:
	@echo make bulild
	@echo make uberjar
	@echo make deploy
	@echo make clean

build:
	docker build -t hkim0331/wil .

uberjar:
	lein uberjar

deploy:
	scp target/default+uberjar/wil.jar ${DEST}:wil/wil.jar && \
	ssh ${DEST} 'sudo systemctl restart wil' && \
	ssh ${DEST} 'systemctl status wil'

clean:
	${RM} -rf target
