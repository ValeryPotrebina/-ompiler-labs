abstract public class Token {
    protected Fragment fragment;
    protected Type type;

    public Token(Type type, Position start, Position end) {
        this.fragment = new Fragment(start, end);
        this.type = type;
    }

    @Override
    public String toString() {
        return fragment + " " + type.toString();
    }
}
