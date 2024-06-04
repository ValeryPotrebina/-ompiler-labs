import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LexAnalyser {

    ArrayList<Token> tokens;
    ArrayList<String> errors;
    private int position;
    private final String NON_TERM = "[A-Z][A-Z0-9]*";
    private final String TERM = "[a-z0-9]|'\\S'";
    private final String SPEC_REGEX = ";|,|::=|\\|";
    private final String KW_REGEX = "non-terminal|terminal|axiom|epsilon";
    private final String SKIP_REGEX = "(\\s+)|(#[^\n]*)";
    private final Pattern PATTERN = Pattern.compile("(?<kw>^" + KW_REGEX + ")|(?<nonterm>^" + NON_TERM + ")|(?<term>^" + TERM + ")|(?<spec>^" + SPEC_REGEX + ")|(?<skip>^" + SKIP_REGEX + ")");
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

    public void parse(ArrayList<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            Matcher matcher = PATTERN.matcher(line);
            while (matcher.regionStart() < line.length()) {
                if (matcher.find()) {
                    if (matcher.group("kw") != null) {
                        Token token;
                        String value = matcher.group("kw");
                        switch (value) {
                            case "terminal" -> token = new Token(Type.KW_TERM, new Coords(i, matcher.start()), value);
                            case "non-terminal" ->
                                    token = new Token(Type.KW_NONTERM, new Coords(i, matcher.start()), value);
                            case "epsilon" -> token = new Token(Type.KW_EPSILON, new Coords(i, matcher.start()), value);
                            case "axiom" -> token = new Token(Type.KW_AXIOM, new Coords(i, matcher.start()), value);
                            default -> {
                                int a = errorHandling(line, new Coords(i, matcher.regionStart()));
                                matcher.region(a, line.length());
                                continue;
                            }
                        }
                        tokens.add(token);
                    } else if (matcher.group("nonterm") != null) {
                        Token token = new Token(Type.NONTERM, new Coords(i, matcher.start()), matcher.group("nonterm"));
                        tokens.add(token);
                    } else if (matcher.group("term") != null) {
                        Token token = new Token(Type.TERM, new Coords(i, matcher.start()), matcher.group("term"));
                        tokens.add(token);
                    } else if (matcher.group("spec") != null) {
                        Token token;
                        String value = matcher.group("spec");
                        switch (value) {
                            case "::=":
                                token = new Token(Type.EQUAL, new Coords(i, matcher.start()), value);
                                break;
                            case ";":
                                token = new Token(Type.SEMICOLON, new Coords(i, matcher.start()), value);
                                break;
                            case ",":
                                token = new Token(Type.COMMA, new Coords(i, matcher.start()), value);
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
                    }
                    matcher.region(matcher.end(), line.length());
                } else {
                    int a = errorHandling(line, new Coords(i, matcher.regionStart()));
                    matcher.region(a, line.length());
                }
            }
        }
        tokens.add(new Token(Type.END, new Coords(lines.size() - 1, lines.get(lines.size() - 1).length()), ""));

    }

    public int errorHandling(String line, Coords coord) {
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
        if (position >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(position++);
    }

}
