public class Scanner {
    private String program;
    private Position position;
    private Compiler compiler;

    public Scanner(String program, Compiler compiler) {
        this.position = new Position(program);
        this.program = program;
        this.compiler = compiler;
    }


    public Token nextToken(){
        int state = 0;
        Position start = new Position(position);
        while (position.current() != -1) {
           int nextState = Automata.nextState(state, (char) position.current());
           if (nextState == -1) {
               compiler.addMessage(true, position, "UNKNOWN SYMBOL");
               position.next();
               continue;
           }
           if (nextState == 0) {
                if (state == 14){
                    state = 0;
                    start = new Position(position);
                    continue;
                }
                if (state == 13){
                    compiler.addComment(start, position, program.substring(start.getIndex(), position.getIndex()));
                    state = 0;
                    start = new Position(position);
                    continue;
                }
                DomainTag tag = Automata.states[state];
                return getToken(start, position, tag);
           }
           position.next();
           state = nextState;
       }
        if (state == 13) {
            compiler.addComment(start, position, program.substring(start.getIndex(), position.getIndex()));
            state = 0;
        }
        if (state == 14) {
            state = 0;
        }
        if (state == 0) {
            return new SpecToken(DomainTag.END, position, position);
        }


        if (Automata.isFinal[state]) {
            DomainTag tag = Automata.states[state];
            return getToken(start, position, tag);
        }
        compiler.addMessage(true, position, "UNEXPECTED END OF PROGRAM");
        return new SpecToken(DomainTag.END, position, position);
    }

    public Token getToken(Position start, Position end, DomainTag tag) {
        if (tag == DomainTag.IDENT)
            return new IdentToken(start, end, compiler.addName(program.substring(start.getIndex(), end.getIndex())));
        if (tag == DomainTag.NUMBER)
            return new NumberToken(start, end, Long.parseLong(program.substring(start.getIndex(), end.getIndex())));
        return new SpecToken(tag, start, end);

    }
//    Automata<>
}
