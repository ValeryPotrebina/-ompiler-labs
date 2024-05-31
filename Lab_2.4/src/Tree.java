import java.util.ArrayList;

public class Tree {
    public record Token(String name){};
    public record Tokens(ArrayList<Token> tokens){}
    public record TypeDef(ArrayList<Token> tokens, Record type){
        public TypeDef {
            if (!(type instanceof SimpleType || type instanceof ArrType)) {
                throw new RuntimeException("Type must be simple or array type");
            }
        }
    }
    public record SimpleType(String ident){

    }
    public record Specification(String className, Tokens tokens, TypeDefs typeDefs, MethodDefs methodDefs, Rules rules, Token axiom){

    }
    public record ArrType(Record type){
        public ArrType {
            if (!(type instanceof SimpleType || type instanceof ArrType)) {
                throw new RuntimeException("Type must be simple or array type");
            }
        }
    }

    public record TypeDefs(ArrayList<TypeDef> typeDefs){}
    public record MethodDefs(ArrayList<MethodDef> methodDefs){}
    public record MethodDef(Record type, String name, ArrayList<Record> methodArgs){
        public MethodDef {
            if (!(type instanceof SimpleType || type instanceof ArrType)) {
                throw new RuntimeException("Type must be simple or array type");
            }
            for (Record arg: methodArgs) {
                if (!(arg instanceof SimpleType || arg instanceof ArrType)) {
                    throw new RuntimeException("Type must be simple or array type");
                }
            }
        }
    }
    public record Rules(ArrayList<Rule> rules){

    }
    public record Rule(Token token, RuleBody ruleBody){

    }

    public record RuleBody(ArrayList<Alter> alters){}

    public record Alter(ArrayList<AlterElem> alterElems, String alterMethod){}
    public record AlterElem(boolean rep, Record content){
        public AlterElem {
            if (!(content instanceof Token || content instanceof RuleBody)){
                throw new RuntimeException("content must be Token or RuleBody");
            }
        }
    }


}
