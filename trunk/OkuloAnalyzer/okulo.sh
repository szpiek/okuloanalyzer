#!/bin/sh

CLASSP=$CLASSPATH:OkuloAnalyzer.jar:lib/jsr80.jar:lib/jsr80_linux.jar:lib/jsr80_ri.jar:lib/log4j-1.2.15.jar:lib/libJavaxUsb.so

java -classpath $CLASSP Main
