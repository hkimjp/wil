#!/bin/sh
export PORT=20223
export DATABASE_URL='postgresql://localhost/wil?user=wil&password=not'
export BAN_IP='150.69.77.*'
java -jar wil.jar
