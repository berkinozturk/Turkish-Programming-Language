@echo off
:: compile
jflex-1.8.2.jar lcalc.flex
java -cp .;java-cup-11b.jar java_cup.Main < ycalc.cup
javac -cp .;java-cup-11b.jar Main.java

:: run
java -cp .;java-cup-11b-runtime.jar Main test.txt

:: clean
del Lexer.java
del parser.java
del sym.java
del *.class
