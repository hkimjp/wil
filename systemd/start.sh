#!/bin/sh

export PORT=20223
export DATABASE_URL='postgresql://localhost/wil?user=wil&password=not'
java -jar wil.jar

