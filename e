#!/bin/sh
EXP_DATE=$1
EXP_AMOUNT=$2
EXP_DESCRIPTION=$3
shift 3
java \
  -Dscript=e \
  -Ddate="$EXP_DATE" \
  -Damount="$EXP_AMOUNT" \
  -Ddescription="$EXP_DESCRIPTION" \
  -Dtags="$*" \
  -jar target/expenses-0.1.0-SNAPSHOT.jar
