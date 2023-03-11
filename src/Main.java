/*
Run these commands:

java -jar java-cup-11b.jar -parser MyParser -symbols MySymbol < MyParser.cup
java -jar jflex-1.8.2.jar MyScanner.lex
javac *.java
java Main example.txt
*/
import java.io.FileNotFoundException;
import java.io.FileReader;
class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String fileName = "example.txt";
        MyParser parser = new MyParser(new MyScanner(new FileReader(fileName)));
        try {
            parser.parse();
            //parser.debug_parse();
        }
        catch (Exception e) {
            System.out.println("Caught an exception.");
        }
    }
}
