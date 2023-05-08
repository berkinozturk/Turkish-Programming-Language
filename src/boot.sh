#!/bin/bash

#compile
jflex-1.6.1/bin/jflex lcalc.flex;
java -cp .:java-cup-11b.jar java_cup.Main  < ycalc_.cup;
javac -cp .:java-cup-11b.jar Main_.java;

#run
java -cp .:java-cup-11b-runtime.jar Main test.txt;

#clean
rm Lexer.java;
rm parser.java;
rm sym.java;
rm *.class;

