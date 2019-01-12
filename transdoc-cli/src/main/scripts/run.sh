#!/bin/sh

HOME=`pwd`

CLASSPATH="$HOME/bin"
MAIN_CLASS="com.transdoc.cli.TransdocAppication"

for file in "$HOME"/libs/*.jar; do
  CLASSPATH="${CLASSPATH}:${file}"
done

java -classpath $CLASSPATH $MAIN_CLASS