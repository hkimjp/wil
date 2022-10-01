DEST="ubuntu@app.melt.kyutech.ac.jp"

uberjar:
	lein uberjar

deploy:
	scp target/default+uberjar/wil.jar ${DEST}:wil/wil.jar && \
	ssh ${DEST} 'sudo systemctl restart wil' && \
	ssh ${DEST} 'systemctl status wil'

hkim0331/lein:
	docker build -t $@ .

clean:
	${RM} -rf target
