#!/bin/sh
EXP_DATE=$1
EXP_AMOUNT=$2
EXP_DESCRIPTION=$3
shift 3
java -jar target/expenses-0.1.0-SNAPSHOT.jar $EXP_DATE $EXP_AMOUNT $EXP_DESCRIPTION $@
