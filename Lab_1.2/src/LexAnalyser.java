import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LexAnalyser {

    ArrayList<Token> tokens;
    ArrayList<String> errors;
    private final String NUMBER_REGEX = "\\d[\\d_]*";
    private final String STRING_REGEX = "\\\"([^\\\"\\\\]*(\\\\n|\\\\t|\\\\\\\\|\\\\\\\")?)*\\\"";
    private final Pattern PATTERN = Pattern.compile("(?<string>^" + STRING_REGEX + ")|(?<number>^" + NUMBER_REGEX + ")|(?<space>^ (\\s+))");
    private final Pattern REPAIR = Pattern.compile("[^\"\\d]*[\"\\d]");
//REPAIR - ВОССТАНОВЛЕНИЕ
    public LexAnalyser(String path) {
        this.tokens = new ArrayList<>();
        this.errors = new ArrayList<>();
        ArrayList<String> lines = getText(path);
        parse(lines);
        tokens.forEach(System.out::println);
        errors.forEach(System.out::println);
    }

    //    Переводим строки в токены

    public void parse(ArrayList<String> lines) {
        // зачем используем Mather TODO Изучить ДОКУ matcher
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            Matcher matcher = PATTERN.matcher(line);
            while (matcher.regionStart() < line.length()) {
                if (matcher.find()){
                    if (matcher.group("string") != null){
                        Token token = new Token(Type.STRING, new Coords(i, matcher.start()), matcher.group("string"));
                        tokens.add(token);
                    } else if (matcher.group("number") != null) {
                        Token token = new Token(Type.NUMBER, new Coords(i, matcher.start()), matcher.group("number"));
                        tokens.add(token);
                    }

                    matcher.region(matcher.end(), line.length());
                } else {
                    int a = errorHandling(line, new Coords(i, matcher.regionStart()));
                    //System.out.println(a);
                    matcher.region(a, line.length());
                }
            }
        }

    }


    public int errorHandling(String line, Coords coord){
        Matcher matcher = REPAIR.matcher(line);
        matcher.region(coord.position, line.length());
        errors.add("syntax error " + coord);
        if (matcher.find()) {
            return matcher.end() - 1;
        }
        return line.length();
    }
    public ArrayList<String> getText(String path) {
        ArrayList<String> arraylistLines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while (line != null) {
                arraylistLines.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arraylistLines;
    }


}
