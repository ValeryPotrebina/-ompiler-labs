import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LexAnalyser {

    ArrayList<Token> tokens;
    ArrayList<String> errors;
    private int position;
    private final String IDENT_REGEX = "[a-zA-Z][a-zA-Z0-9_]*";
    private final String SPEC_REGEX = "(\\[\\])|[()=:;,/|]";
    private final String KW_REGEX = "%(class|tokens|types|methods|grammar|axiom|end|rep)";
    private final String SKIP_REGEX = "(\\s+)|(\\$[^\n]*)";
    private final Pattern PATTERN = Pattern.compile("(?<ident>^" + IDENT_REGEX + ")|(?<spec>^" + SPEC_REGEX + ")|(?<kw>^" + KW_REGEX + ")|(?<skip>^" + SKIP_REGEX + ")");
    private final Pattern REPAIR = Pattern.compile("[^\"\\d]*[\"\\d]");
//REPAIR - ВОССТАНОВЛЕНИЕ
    public LexAnalyser(String path) {
        this.tokens = new ArrayList<>();
        this.errors = new ArrayList<>();
        position = 0;
        ArrayList<String> lines = getText(path);
        parse(lines);
//        tokens.forEach(System.out::println);
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
                    if (matcher.group("ident") != null){
                        Token token = new Token(Type.IDENT, new Coords(i, matcher.start()), matcher.group("ident"));
                        tokens.add(token);
                    } else if (matcher.group("spec") != null) {
                        Token token;
                        String value = matcher.group("spec");
                        switch (value) {
                            case "[]":
                                token = new Token(Type.ARR_TYPE, new Coords(i, matcher.start()), value);
                                break;
                            case "(":
                                token = new Token(Type.LPAREN, new Coords(i, matcher.start()), value);
                                break;
                            case ")":
                                token = new Token(Type.RPAREN, new Coords(i, matcher.start()), value);
                                break;
                            case "=":
                                token = new Token(Type.EQUAL, new Coords(i, matcher.start()), value);
                                break;
                            case ":":
                                token = new Token(Type.COLON, new Coords(i, matcher.start()), value);
                                break;
                            case ";":
                                token = new Token(Type.SEMICOLON, new Coords(i, matcher.start()), value);
                                break;
                            case ",":
                                token = new Token(Type.COMMA, new Coords(i, matcher.start()), value);
                                break;
                            case "/":
                                token = new Token(Type.SLASH, new Coords(i, matcher.start()), value);
                                break;
                            case "|":
                                token = new Token(Type.OR, new Coords(i, matcher.start()), value);
                                break;
                            default: {
                                int a = errorHandling(line, new Coords(i, matcher.regionStart()));
                                matcher.region(a, line.length());
                                continue;
                            }
                        }
                        tokens.add(token);
                    } else if (matcher.group("kw") != null) {
                        Token token;
                        String value = matcher.group("kw");
                        switch (value) {
                            case "%class":
                                token = new Token(Type.KW_CLASS, new Coords(i, matcher.start()), value);
                                break;
                            case "%tokens":
                                token = new Token(Type.KW_TOKENS, new Coords(i, matcher.start()), value);
                                break;
                            case "%types":
                                token = new Token(Type.KW_TYPES, new Coords(i, matcher.start()), value);
                                break;
                            case "%methods":
                                token = new Token(Type.KW_METHODS, new Coords(i, matcher.start()), value);
                                break;
                            case "%grammar":
                                token = new Token(Type.KW_GRAMMAR, new Coords(i, matcher.start()), value);
                                break;
                            case "%axiom":
                                token = new Token(Type.KW_AXIOM, new Coords(i, matcher.start()), value);
                                break;
                            case "%end":
                                token = new Token(Type.KW_END, new Coords(i, matcher.start()), value);
                                break;
                            case "%rep":
                                token = new Token(Type.KW_REP, new Coords(i, matcher.start()), value);
                                break;
                            default: {
                                int a = errorHandling(line, new Coords(i, matcher.regionStart()));
                                matcher.region(a, line.length());
                                continue;
                            }
                        }
                        tokens.add(token);
                    }
                    matcher.region(matcher.end(), line.length());
                } else {
                    int a = errorHandling(line, new Coords(i, matcher.regionStart()));
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

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public Token nextToken() {
        return tokens.get(position++);
    }

}
