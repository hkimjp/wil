#!/bin/sh
if [ -z "$1" ]; then
    echo usage: $0 yyyy-mm-dd.dumo
    exit 1
fi

PSQL="psql -h db -U postgres"
${PSQL} -c "drop database wil"
${PSQL} -c "create database wil owner wil"
${PSQL} wil < $1
