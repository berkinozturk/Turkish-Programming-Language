:: This batch file is for running required commands faster
@ECHO OFF
cd %CD%
java -jar java-cup-11b.jar -parser MyParser -symbols MySymbol < MyParser.cup
java -jar jflex-1.8.2.jar MyScanner.lex
javac *.java
java Main example.txt
PAUSE

