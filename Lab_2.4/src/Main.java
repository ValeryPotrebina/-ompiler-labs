import java.util.ArrayList;

public class Main {
    public static final String PATH = "D:\\Конструирование компиляторов\\Labs\\-ompiler-labs\\Lab_2.4\\src\\Input.txt";
    public static void main(String[] args) {
       LexAnalyser lexAnalyser = new LexAnalyser(PATH);
       SyntaxAnalyser syntaxAnalyser = new SyntaxAnalyser(lexAnalyser);
       System.out.println(syntaxAnalyser.parse());
    }

}