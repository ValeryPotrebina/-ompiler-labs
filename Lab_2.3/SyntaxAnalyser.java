import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class SyntaxAnalyser {
    private LexAnalyser lexAnalyser;
    private Token current;
    public Stack<Tree> magazine = new Stack<>();

    public SyntaxAnalyser(LexAnalyser lexAnalyser) {
        this.lexAnalyser = lexAnalyser;
    }


    public Rule parse() {
        Rule initRule = new Rule("S");
        magazine.push(initRule);
        nextToken();
        while (!magazine.isEmpty()) {
            Tree last = magazine.pop();
            if (last instanceof SynToken) {
                SynToken synToken = (SynToken) last;
                if (synToken.tokenType == current.getType()) {
                    synToken.tokenValue = current.getValue();
                    nextToken();
                } else {
                    errorHandling("Excepted " + synToken.tokenType + " but found " + current.getType(), current.getCoords());
                }
            } else if (last instanceof Rule) {
                Rule rule = (Rule) last;
                ArrayList<Tree> children = analyseFunction(rule.ruleName, current);
                for (int i = children.size() - 1; i >= 0; i--) {
                    magazine.push(children.get(i));
                }
                rule.setChildren(children);
            }
        }
        Stack<Tree> stack = new Stack<>();
        int index = 0;
        stack.push(initRule);
        while (!stack.isEmpty()){
            Tree tree = stack.pop();
            tree.index = index++;
            if (tree instanceof Rule) {
                Rule rule = (Rule) tree;
                for (Tree child : rule.children) {
                    stack.push(child);
                }
            }
        }
        return initRule;
    }

    private ArrayList<Tree> analyseFunction(String ruleName, Token token) {
//        System.out.println(rule.name() + " " + token.getType());
        ArrayList<Tree> result = new ArrayList<>();
        switch (ruleName) {
            case "S" -> {
                if (token.getType() == Type.KW_NONTERM) {
                    result.add(new SynToken(Type.KW_NONTERM));
                    result.add(new Rule("NontermList"));
                    result.add(new SynToken(Type.SEMICOLON));
                    result.add(new SynToken(Type.KW_TERM));
                    result.add(new Rule("TermList"));
                    result.add(new SynToken(Type.SEMICOLON));
                    result.add(new Rule("Rules"));
                    result.add(new SynToken(Type.KW_AXIOM));
                    result.add(new SynToken(Type.NONTERM));
                    result.add(new SynToken(Type.SEMICOLON));
                    return result;
                } else {
                    errorHandling("Expected KW_NONTERM but found " + token.getType(), token.getCoords());
                }
            }
            case "NontermList" -> {
                if (token.getType() == Type.NONTERM) {
                    result.add(new SynToken(Type.NONTERM));
                    result.add(new Rule("NontermList2"));
                    return result;
                } else {
                    errorHandling("Expected NONTERM but found " + token.getType(), token.getCoords());
                }
            }
            case "NontermList2" -> {
                if (token.getType() == Type.COMMA) {
                    result.add(new SynToken(Type.COMMA));
                    result.add(new SynToken(Type.NONTERM));
                    result.add(new Rule("NontermList2"));
                    return result;
                } else if (token.getType() == Type.SEMICOLON) {
                    return result;
                } else {
                    errorHandling("Expected COMMA or SEMICOLON but found " + token.getType(), token.getCoords());
                }
            }
            case "TermList" -> {
                if (token.getType() == Type.TERM) {
                    result.add(new SynToken(Type.TERM));
                    result.add(new Rule("TermList2"));
                    return result;
                } else {
                    errorHandling("Expected TERM but found " + token.getType(), token.getCoords());
                }
            }
            case "TermList2" -> {
                if (token.getType() == Type.COMMA) {
                    result.add(new SynToken(Type.COMMA));
                    result.add(new SynToken(Type.TERM));
                    result.add(new Rule("TermList2"));
                    return result;
                } else if (token.getType() == Type.SEMICOLON) {
                    return result;
                } else {
                    errorHandling("Expected COMMA or SEMICOLON but found " + token.getType(), token.getCoords());
                }
            }
            case "Rules" -> {
                if (token.getType() == Type.NONTERM) {
                    result.add(new Rule("Rule"));
                    result.add(new Rule("Rules"));
                    return result;
                } else if (token.getType() == Type.KW_AXIOM) {
                    return result;
                } else {
                    errorHandling("Expected Axiom | Nonterm but found " + token.getType(), token.getCoords());
                }
            }
            case "Rule" -> {
                if (token.getType() == Type.NONTERM) {
                    result.add(new SynToken(Type.NONTERM));
                    result.add(new SynToken(Type.EQUAL));
                    result.add(new Rule("RuleBody"));
                    result.add(new SynToken(Type.SEMICOLON));
                    return result;
                } else {
                    errorHandling("Expected NONTERM but found " + token.getType(), token.getCoords());
                }
            }
            case "RuleBody" -> {
                if (token.getType() == Type.TERM
                        || token.getType() == Type.NONTERM
                        || token.getType() == Type.KW_EPSILON) {
                    result.add(new Rule("Alter"));
                    result.add(new Rule("RuleBody2"));
                    return result;
                } else {
                    errorHandling("Expected TERM | NONTERM | KW_EPSILON but found " + token.getType(), token.getCoords());
                }
            }
            case "RuleBody2" -> {
                if (token.getType() == Type.OR) {
                    result.add(new SynToken(Type.OR));
                    result.add(new Rule("Alter"));
                    result.add(new Rule("RuleBody2"));
                    return result;
                } else if (token.getType() == Type.SEMICOLON) {
                    return result;
                } else {
                    errorHandling("Expected OR | SEMICOLON but found " + token.getType(), token.getCoords());
                }
            }
            case "Alter" -> {
                if (token.getType() == Type.TERM || token.getType() == Type.NONTERM) {
                    result.add(new Rule("Token"));
                    result.add(new Rule("Alter2"));
                    return result;
                } else if (token.getType() == Type.KW_EPSILON) {
                    result.add(new SynToken(Type.KW_EPSILON));
                    return result;
                } else {
                    errorHandling("Expected TERN | NONTERM | KW_EPSILON but found " + token.getType(), token.getCoords());
                }
            }
            case "Alter2" -> {
                if (token.getType() == Type.TERM || token.getType() == Type.NONTERM) {
                    result.add(new Rule("Token"));
                    result.add(new Rule("Alter2"));
                    return result;
                } else if (token.getType() == Type.OR || token.getType() == Type.SEMICOLON) {
                    return result;
                } else {
                    errorHandling("Expected TERN | NONTERM | OR | SEMICOLON but found " + token.getType(), token.getCoords());
                }
            }
            case "Token" -> {
                if (token.getType() == Type.TERM) {
                    result.add(new SynToken(Type.TERM));
                    return result;
                } else if (token.getType() == Type.NONTERM) {
                    result.add(new SynToken(Type.NONTERM));
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
