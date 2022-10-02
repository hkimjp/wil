DEST="ubuntu@app.melt.kyutech.ac.jp"

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
