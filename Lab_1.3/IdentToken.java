public class IdentToken extends Token{
    private int pointer;

    public IdentToken(Position start, Position end, int pointer) {
        super(Type.IDENT, start, end);
        this.pointer = pointer;
    }

    public int getPointer() {
        return pointer;
    }
}
