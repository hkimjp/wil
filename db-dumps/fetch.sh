#!/bin/sh

SERV=tiger.melt
PROJ=wil
DUMP=${PROJ}/db-dumps
TODAY=`date +%F`

ssh ${SERV} "cd ${DUMP} && ./dump.sh"
scp ${SERV}:${DUMP}/${PROJ}-${TODAY}.sql .
