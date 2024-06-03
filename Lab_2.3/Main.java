import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final String PATH = "D:\\Конструирование компиляторов\\Labs\\-ompiler-labs\\Lab_2.3\\input.txt" ;

    public static void main(String[] args) {
        LexAnalyser lexAnalyser = new LexAnalyser(PATH);
        SyntaxAnalyser syntaxAnalyser = new SyntaxAnalyser(lexAnalyser);
        ArrayList<Record> tree = syntaxAnalyser.parse();

    }

//    public static String resultToGraph(ArrayList<Record> trees) {
//
//    }
//
//    public static String treeToGraph(Record current, ArrayList<Record> trees){
//
//    }
}
