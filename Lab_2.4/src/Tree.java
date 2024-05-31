import java.util.ArrayList;
import java.util.Optional;

public class Tree {
    public record Specification(String className, Tokens tokens, TypeDefs typeDefs, MethodDefs methodDefs, Rules rules, Token axiom){
        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Specification{\n\t");
            sb.append("className: ").append(className).append(",\n\t");
            sb.append("tokens: ").append(tokens.toString().replace("\n", "\n\t")).append(",\n\t");
            sb.append("typeDefs: ").append(typeDefs.toString().replace("\n", "\n\t")).append(",\n\t");
            sb.append("methodDefs: ").append(methodDefs.toString().replace("\n", "\n\t")).append(",\n\t");
            sb.append("rules: ").append(rules.toString().replace("\n", "\n\t")).append(",\n\t");
            sb.append("axiom: ").append(axiom.toString()).append(",\n}");
            return sb.toString();
        }
    }
    public record Token(String name){
        @Override
        public String toString() {
            return "Token{ name: " + name  + " }";
        }
    };
    public record Tokens(ArrayList<Token> tokens) {
        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Tokens{\n\ttokens: [");
            tokens.forEach((token -> {
                sb.append("\n\t\t");
                sb.append(token.toString());
                sb.append(", ");
            }));
            sb.append("\n\t]\n}");
            return sb.toString();
        }
    }
    public record TypeDef(ArrayList<Token> tokens, Record type){
        public TypeDef {
            if (!(type instanceof SimpleType || type instanceof ArrType)) {
                throw new RuntimeException("Type must be simple or array type");
            }
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("TypeDef{\n\ttokens: [");
            tokens.forEach((token -> {
                sb.append("\n\t\t");
                sb.append(token.toString());
                sb.append(", ");
            }));
            sb.append("\n\t],\n\ttype: ");
            sb.append(type.toString().replace("\n", "\n\t"));
            sb.append("\n}");
            return sb.toString();
        }
    }
    public record SimpleType(String name){
        @Override
        public String toString() {
            return "SimpleType{ name: " + name + " }";
        }
    }
    public record ArrType(Record type){
        public ArrType {
            if (!(type instanceof SimpleType || type instanceof ArrType)) {
                throw new RuntimeException("Type must be simple or array type");
            }
        }
        @Override
        public String toString() {
            return "ArrType{ type: " + type.toString() + " }";
        }
    }

    public record TypeDefs(ArrayList<TypeDef> typeDefs){
        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("TypeDefs{\n\ttypeDefs: [");
            typeDefs.forEach(typeDef -> {
                sb.append("\n\t\t");
                sb.append(typeDef.toString().replace("\n", "\n\t\t"));
                sb.append(",");
            });
            sb.append("\n\t]\n}");
            return sb.toString();
        }
    }
    public record MethodDefs(ArrayList<MethodDef> methodDefs){
        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("MethodDefs{\n\tmethodDefs: [");
            methodDefs.forEach(methodDef -> {
                sb.append("\n\t\t");
                sb.append(methodDef.toString().replace("\n", "\n\t\t"));
                sb.append(",");
            });
            sb.append("\n\t]\n}");
            return sb.toString();
        }
    }
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

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("MethodDef{\n\ttype: ");
            sb.append(type.toString());
            sb.append(",\n\tname: " + name + ",\n\targs: [");
            methodArgs.forEach(arg -> {
                sb.append("\n\t\t");
                sb.append(arg.toString());
                sb.append(",");
            });
            sb.append("\n\t],\n}");
            return sb.toString();
        }
    }
    public record Rules(ArrayList<Rule> rules){
        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Rules{\n\trules: [");
            rules.forEach((rule -> {
                sb.append("\n\t\t");
                sb.append(rule.toString().replace("\n", "\n\t\t"));
                sb.append(", ");
            }));
            sb.append("\n\t]\n}");
            return sb.toString();
        }
    }
    public record Rule(Token token, RuleBody ruleBody){

        @Override
        public String toString() {
            return "Rule{\n\ttoken: " +  token.toString() + ",\n\tbody: " + ruleBody.toString().replace("\n", "\n\t") + ",\n}";
        }
    }

    public record RuleBody(ArrayList<Alter> alters){
        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("RuleBody{\n\talters: [");
            alters.forEach((alter -> {
                sb.append("\n\t\t");
                sb.append(alter.toString().replace("\n", "\n\t\t"));
                sb.append(", ");
            }));
            sb.append("\n\t]\n}");
            return sb.toString();
        }
    }

    public record Alter(ArrayList<AlterElem> alterElems, String alterMethod){
        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Alter{\n\telems: [");
            alterElems.forEach(elem -> {
                sb.append("\n\t\t").append(elem.toString().replace("\n", "\n\t\t")).append(",");
            });
            sb.append("\n\t],\n\tmethod: ").append(alterMethod.equals("") ? "None" : alterMethod).append(",\n}");
            return sb.toString();
        }
    }
    public record AlterElem(boolean rep, Record content){
        public AlterElem {
            if (!(content instanceof Token || content instanceof RuleBody)){
                throw new RuntimeException("content must be Token or RuleBody");
            }
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("AlterElem{\n\tisRep: " + rep + ",\n\tcontent: ");
            sb.append(content.toString().replace("\n", "\n\t")).append(",\n}");
            return sb.toString();
        }
    }


}
