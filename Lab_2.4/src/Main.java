import java.util.ArrayList;

public class Main {
    public static final String PATH = "D:\\Конструирование компиляторов\\Labs\\-ompiler-labs\\Lab_2.4\\src\\Input.txt";
    public static void main(String[] args) {
       LexAnalyser lexAnalyser = new LexAnalyser(PATH);
        System.out.println(lexAnalyser);
//       lexAnalyser.parse();
        char a = 'A';
        char f = 'F';

        System.out.println((int) a);
        System.out.println((int) f);

    }

}