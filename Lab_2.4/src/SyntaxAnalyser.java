import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.regex.Matcher;

public class SyntaxAnalyser {
    private LexAnalyser lexAnalyser;
    private Token current;

    public SyntaxAnalyser(LexAnalyser lexAnalyser) {
        this.lexAnalyser = lexAnalyser;
    }

    public Tree.Specification parse() {
        nextToken();
        return parseSpecification();
    }


    private Tree.Specification parseSpecification(){
        parseSpec(Type.KW_CLASS);
        String className = parseIdent();
        parseSpec(Type.KW_TOKENS);
        Tree.Tokens tokens =  parseTokens();
        parseSpec(Type.KW_TYPES);
        Tree.TypeDefs typeDefs = parseTypeDefs();
        parseSpec(Type.KW_METHODS);
        Tree.MethodDefs methodDefs = parseMethodDefs();
        parseSpec(Type.KW_GRAMMAR);
        Tree.Rules rules = parseRules();
        parseSpec(Type.KW_AXIOM);
        Tree.Token axiom =  parseToken();
        parseSpec(Type.KW_END);
        return new Tree.Specification(className, tokens, typeDefs, methodDefs, rules, axiom);
    }

    private Tree.Rules parseRules(){
        ArrayList<Tree.Rule> rules = new ArrayList<>();
        while (current.getType() == Type.IDENT){
            Tree.Rule rule = parseRule();
            rules.add(rule);
        }
        return  new Tree.Rules(rules);
    }

    private Tree.Rule parseRule(){
        Tree.Token token = parseToken();
        parseSpec(Type.EQUAL);
        Tree.RuleBody ruleBody = parseRuleBody();
        parseSpec(Type.SEMICOLON);

        return new Tree.Rule(token, ruleBody);
    }

    private Tree.RuleBody parseRuleBody(){
        ArrayList<Tree.Alter> alters = new ArrayList<>();

        alters.add(parseAlter());

        while (current.getType() == Type.OR) {
            parseSpec(Type.OR);
            alters.add(parseAlter());
        }

        return new Tree.RuleBody(alters);
    }

    private Tree.Alter parseAlter() {
        ArrayList<Tree.AlterElem> elems = new ArrayList<>();
        String alterMethod = "";
        while (current.getType() == Type.KW_REP || current.getType() == Type.IDENT || current.getType() == Type.LPAREN) {
            elems.add(parseAlterElem());
        }

        if (current.getType() == Type.SLASH) {
            parseSpec(Type.SLASH);
            alterMethod = parseIdent();
        }
        return new Tree.Alter(elems, alterMethod);
    }

    private Tree.AlterElem parseAlterElem() {
        boolean isRep = false;
        Record content = null;
        if (current.getType() == Type.KW_REP) {
            parseSpec(Type.KW_REP);
            isRep = true;
        }
        if (current.getType() == Type.IDENT) {
            content = parseToken();
        } else if (current.getType() == Type.LPAREN){
            parseSpec(Type.LPAREN);
            content = parseRuleBody();
            parseSpec(Type.RPAREN);
        } else {
            errorHandling("Expected ident or lparen but found " + current.getValue(), current.getCoords());
        }
        return new Tree.AlterElem(isRep, content);
    }
    private Tree.MethodDefs parseMethodDefs() {
        ArrayList<Tree.MethodDef> methodDefs = new ArrayList<>();
        while (current.getType() == Type.IDENT) {
            Tree.MethodDef methodDef = parseMethodDef();
            methodDefs.add(methodDef);
        }
        return new Tree.MethodDefs(methodDefs);
    }

    private Tree.MethodDef parseMethodDef() {
        Record type = parseType();
        String methodName = parseIdent();
        parseSpec(Type.LPAREN);
        ArrayList<Record> args = new ArrayList<>();
        if (current.getType() == Type.IDENT) {
            args.add(parseType());
            while (current.getType() == Type.COMMA) {
                parseSpec(Type.COMMA);
                args.add(parseType());
            }
        }
        parseSpec(Type.RPAREN);
        parseSpec(Type.SEMICOLON);
        return new Tree.MethodDef(type, methodName, args);
    }
    private Tree.Tokens parseTokens(){
        ArrayList<Tree.Token> tokens = new ArrayList<>();
        while (current.getType() == Type.IDENT) {
            Tree.Token token = parseToken();
            tokens.add(token);
        }
        return new Tree.Tokens(tokens);
    }

    private Tree.TypeDefs parseTypeDefs(){
        ArrayList<Tree.TypeDef> typeDefs = new ArrayList<>();
        while (current.getType() == Type.IDENT) {
            Tree.TypeDef typeDef = parseTypeDef();
            typeDefs.add(typeDef);
        }
        return new Tree.TypeDefs(typeDefs);
    }

    private Tree.TypeDef parseTypeDef(){
        ArrayList<Tree.Token> tokens = new ArrayList<>();
        tokens.add(parseToken());
        while (current.getType() == Type.COMMA) {
            parseSpec(Type.COMMA);
            tokens.add(parseToken());
        }
        parseSpec(Type.COLON);
        Record type = parseType();
        parseSpec(Type.SEMICOLON);
        return new Tree.TypeDef(tokens, type);
    }

    private Record parseType(){
        Record type = new Tree.SimpleType(parseIdent());
        while (current.getType() == Type.ARR_TYPE) {
            parseSpec(Type.ARR_TYPE);
            type = new Tree.ArrType(type);
        }
        return type;
    }

    private String parseIdent(){
        if (current.getType() != Type.IDENT) {
            errorHandling("expected IDENT, but found " + current.getValue(), current.getCoords());
        }
        String ident = current.getValue();
        nextToken();
        return ident;
    }

    private void parseSpec(Type type){
        if (current.getType() != type) {
            errorHandling("expected " + type.toString() + ", but found " + current.getValue(), current.getCoords());
        }
        nextToken();
    }

    private Tree.Token parseToken(){
        return new Tree.Token(parseIdent());
    }
    private void errorHandling(String message, Coords coords){
        System.out.println(coords + ", " + message);
        System.exit(0);
    }


    public void nextToken() {
        current = lexAnalyser.nextToken();
    }
}
