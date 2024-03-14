import java.util.ArrayList;

public class LexAnalyser {
    final private char LPAREN = '(';
    final private char RPAREN = ')';

    private String input;
    private ArrayList<Token> token;
    private int position;

    public LexAnalyser(String input) {
        this.input = input;
        this.position = 0;
        token = new ArrayList<>();
    }







}
