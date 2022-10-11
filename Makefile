DEST="ubuntu@app.melt.kyutech.ac.jp"

all:
	@echo make build
	@echo make uberjar
	@echo make deploy
	@echo make clean

# build:
# 	docker build -t hkim0331/wil .

uberjar:
	lein uberjar

target/default+uberjar/wil.jar:
	lein uberjar

deploy: target/default+uberjar/wil.jar
	scp target/default+uberjar/wil.jar ${DEST}:wil/wil.jar && \
	ssh ${DEST} 'sudo systemctl restart wil' && \
	ssh ${DEST} 'systemctl status wil'

clean:
	${RM} -rf target
