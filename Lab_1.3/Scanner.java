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
        while (position.current() != -1) {
            while (position.isWhiteSpace())
                position.next();
            Position start = new Position(position);
            switch (position.current()){
                case '(':
                    return new SpecToken(Type.LPAREN, start, position.next());
                case ')':
                    return new SpecToken(Type.RPAREN, start, position.next());
                default:
                    if (position.isDigit()) {
                        if (position.current() == '0') {
                            position.next();
                            switch (position.current()) {
                                case 'b', 'B' -> {
                                    position.next();
                                    while (position.isBinary())
                                        position.next();

                                    long binaryNumber = Integer.parseInt(program.substring(start.getIndex() + 2, position.getIndex()), 2);
                                    return new NumberToken(start, position, binaryNumber);
                                }
                                case 't', 'T' -> {
                                    position.next();
                                    while (position.isOcto())
                                        position.next();

                                    long octoNumber = Integer.parseInt(program.substring(start.getIndex() + 2, position.getIndex()), 8);
                                    return new NumberToken(start, position, octoNumber);
                                }
                                case 'x', 'X' -> {
                                    position.next();
                                    while (position.isHex())
                                        position.next();

                                    long hexNumber = Integer.parseInt(program.substring(start.getIndex() + 2, position.getIndex()), 16);
                                    return new NumberToken(start, position, hexNumber);
                                }
                            }
                        }
                        while (position.isDigit())
                            position.next();
                        long decimalNumber = Integer.parseInt(program.substring(start.getIndex(), position.getIndex()), 10);
                        return new NumberToken(start, position, decimalNumber);
                    } else if (position.isLetter()) {

                        do {
                            position.next();
                        } while (position.isLetter());
                        String name = program.substring(start.getIndex(), position.getIndex());
                        if (name.equals("or"))
                            return new SpecToken(Type.OR, start, position);
                        if (name.equals("and"))
                            return new SpecToken(Type.AND, start, position);
                        return new IdentToken(start, position, compiler.addName(name));
                    } else if (position.current() != -1) {
                        compiler.addMessage(true, position, "unexpected character");
                        position.next();
                    }

            }
        }
        return new SpecToken(Type.END_OF_PROGRAM, position, position);
    }
}
