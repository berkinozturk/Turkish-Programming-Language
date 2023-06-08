@echo off
set filename=%1
jflex-1.8.2.jar lcalc.flex 2> NUL
java -cp .;java-cup-11b.jar java_cup.Main < ycalc.cup 2> NUL
javac -cp .;java-cup-11b.jar Main.java 2> NUL

:: run
java -cp .;java-cup-11b-runtime.jar Main %filename%

:: clean
del Lexer.java
del parser.java
del sym.java
del *.class
echo Kapatmak icin bir tusa basin & set /p =" "