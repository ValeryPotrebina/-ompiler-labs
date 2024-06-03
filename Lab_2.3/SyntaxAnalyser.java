import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class SyntaxAnalyser {
    private LexAnalyser lexAnalyser;
    private Token current;
    public Stack<Record> magazine = new Stack<>();

    public SyntaxAnalyser(LexAnalyser lexAnalyser) {
        this.lexAnalyser = lexAnalyser;
    }


    public ArrayList<Record> parse() {
        ArrayList<Record> results = new ArrayList<>();
//        magazine.push(new Tree.Token(MAGAZINE_END));
        magazine.push(new Tree.Rule("S", new ArrayList<>()));
        nextToken();
        while (!magazine.isEmpty()) {
            Record last = magazine.pop();
            if (last instanceof Tree.Token) {
                Type type = ((Tree.Token) last).type();
                if (type == current.getType()) {
                    nextToken();
                    results.add(last);
                } else {
                    errorHandling("Excepted " + type + " but found " + current.getType(), current.getCoords());
                }
            } else if (last instanceof Tree.Rule) {
                Tree.Rule rule = (Tree.Rule) last;
                ArrayList<Record> children = analyseFunction(rule, current);
                for (int i = 0; i < children.size(); i++) {
                    rule.children().add(children.get(i));
                    magazine.push(children.get(children.size() - 1 - i));
                }
                results.add(rule);
            }
        }
        return results;
    }

    private ArrayList<Record> analyseFunction(Tree.Rule rule, Token token) {
        System.out.println(rule.name() + " " + token.getType());
        ArrayList<Record> result = new ArrayList<>();
        switch (rule.name()) {
            case "S" -> {
                if (token.getType() == Type.KW_NONTERM) {
                    result.add(new Tree.Token(Type.KW_NONTERM));
                    result.add(new Tree.Rule("NontermList", new ArrayList<>()));
                    result.add(new Tree.Token(Type.SEMICOLON));
                    result.add(new Tree.Token(Type.KW_TERM));
                    result.add(new Tree.Rule("TermList", new ArrayList<>()));
                    result.add(new Tree.Token(Type.SEMICOLON));
                    result.add(new Tree.Rule("Rules", new ArrayList<>()));
                    result.add(new Tree.Token(Type.KW_AXIOM));
                    result.add(new Tree.Token(Type.NONTERM));
                    result.add(new Tree.Token(Type.SEMICOLON));
                    return result;
                } else {
                    errorHandling("Expected KW_NONTERM but found " + token.getType(), token.getCoords());
                }
            }
            case "NontermList" -> {
                if (token.getType() == Type.NONTERM) {
                    result.add(new Tree.Token(Type.NONTERM));
                    result.add(new Tree.Rule("NontermList2", new ArrayList<>()));
                    return result;
                } else {
                    errorHandling("Expected NONTERM but found " + token.getType(), token.getCoords());
                }
            }
            case "NontermList2" -> {
                if (token.getType() == Type.COMMA) {
                    result.add(new Tree.Token(Type.COMMA));
                    result.add(new Tree.Token(Type.NONTERM));
                    result.add(new Tree.Rule("NontermList2", new ArrayList<>()));
                    return result;
                } else if (token.getType() == Type.SEMICOLON) {
                    return result;
                } else {
                    errorHandling("Expected COMMA or SEMICOLON but found " + token.getType(), token.getCoords());
                }
            }
            case "TermList" -> {
                if (token.getType() == Type.TERM) {
                    result.add(new Tree.Token(Type.TERM));
                    result.add(new Tree.Rule("TermList2", new ArrayList<>()));
                    return result;
                } else {
                    errorHandling("Expected TERM but found " + token.getType(), token.getCoords());
                }
            }
            case "TermList2" -> {
                if (token.getType() == Type.COMMA) {
                    result.add(new Tree.Token(Type.COMMA));
                    result.add(new Tree.Token(Type.TERM));
                    result.add(new Tree.Rule("TermList2", new ArrayList<>()));
                    return result;
                } else if (token.getType() == Type.SEMICOLON) {
                    return result;
                } else {
                    errorHandling("Expected COMMA or SEMICOLON but found " + token.getType(), token.getCoords());
                }
            }
            case "Rules" -> {
                if (token.getType() == Type.NONTERM) {
                    result.add(new Tree.Rule("Rule", new ArrayList<>()));
                    result.add(new Tree.Rule("Rules", new ArrayList<>()));
                    return result;
                } else if (token.getType() == Type.KW_AXIOM) {
                    return result;
                } else {
                    errorHandling("Expected Axiom | Nonterm but found " + token.getType(), token.getCoords());
                }
            }
            case "Rule" -> {
                if (token.getType() == Type.NONTERM) {
                    result.add(new Tree.Token(Type.NONTERM));
                    result.add(new Tree.Token(Type.EQUAL));
                    result.add(new Tree.Rule("RuleBody", new ArrayList<>()));
                    result.add(new Tree.Token(Type.SEMICOLON));
                    return result;
                } else {
                    errorHandling("Expected NONTERM but found " + token.getType(), token.getCoords());
                }
            }
            case "RuleBody" -> {
                if (token.getType() == Type.TERM
                        || token.getType() == Type.NONTERM
                        || token.getType() == Type.KW_EPSILON) {
                    result.add(new Tree.Rule("Alter", new ArrayList<>()));
                    result.add(new Tree.Rule("RuleBody2", new ArrayList<>()));
                    return result;
                } else {
                    errorHandling("Expected TERM | NONTERM | KW_EPSILON but found " + token.getType(), token.getCoords());
                }
            }
            case "RuleBody2" -> {
                if (token.getType() == Type.OR) {
                    result.add(new Tree.Token(Type.OR));
                    result.add(new Tree.Rule("Alter", new ArrayList<>()));
                    result.add(new Tree.Rule("RuleBody2", new ArrayList<>()));
                    return result;
                } else if (token.getType() == Type.SEMICOLON) {
                    return result;
                } else {
                    errorHandling("Expected OR | SEMICOLON but found " + token.getType(), token.getCoords());
                }
            }
            case "Alter" -> {
                if (token.getType() == Type.TERM || token.getType() == Type.NONTERM) {
                    result.add(new Tree.Rule("Token", new ArrayList<>()));
                    result.add(new Tree.Rule("Alter2", new ArrayList<>()));
                    return result;
                } else if (token.getType() == Type.KW_EPSILON) {
                    result.add(new Tree.Token(Type.KW_EPSILON));
                    return result;
                } else {
                    errorHandling("Expected TERN | NONTERM | KW_EPSILON but found " + token.getType(), token.getCoords());
                }
            }
            case "Alter2" -> {
                if (token.getType() == Type.TERM || token.getType() == Type.NONTERM) {
                    result.add(new Tree.Rule("Token", new ArrayList<>()));
                    result.add(new Tree.Rule("Alter2", new ArrayList<>()));
                    return result;
                } else if (token.getType() == Type.OR || token.getType() == Type.SEMICOLON) {
                    return result;
                } else {
                    errorHandling("Expected TERN | NONTERM | OR | SEMICOLON but found " + token.getType(), token.getCoords());
                }
            }
            case "Token" -> {
                if (token.getType() == Type.TERM) {
                    result.add(new Tree.Token(Type.TERM));
                    return result;
                } else if (token.getType() == Type.NONTERM) {
                    result.add(new Tree.Token(Type.NONTERM));
                    return result;
                }
            }
            default -> errorHandling("Unknown rule", token.getCoords());
        }
        return result;
    }

    private void errorHandling(String message, Coords coords) {
        System.out.println(coords + ", " + message);
        System.exit(0);
    }

    public void nextToken() {
        current = lexAnalyser.nextToken();
    }


    private void parseSpec(Type type) {
        if (current.getType() != type) {
            errorHandling("expected " + type.toString() + ", but found " + current.getValue(), current.getCoords());
        }
        nextToken();
    }

//    private

}
