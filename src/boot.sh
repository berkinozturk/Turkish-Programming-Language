#!/bin/bash

set +v

java -jar java-cup-11b.jar -parser MyParser -symbols MySymbol < MyParser.cup
java -jar jflex-1.8.2.jar MyScanner.lex
javac *.java
java Main example.txt

read -n 1 -s -r -p "Press any key to continue..."
