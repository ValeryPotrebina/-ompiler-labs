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
        return new Tree.Specification(className, tokens, typeDefs, methodDefs);
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
