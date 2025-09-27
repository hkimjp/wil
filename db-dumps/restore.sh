#!/bin/sh
PSQL="psql -h db -U postgres --port=15432"
${PSQL} -c "drop database wil"
${PSQL} -c "create database wil owner wil"
${PSQL} wil < $1
