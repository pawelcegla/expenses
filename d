#!/bin/sh
java \
  -Dscript=d \
  -Ddate=$1 \
  -jar target/expenses-0.1.0-SNAPSHOT.jar
