public class Token {
    private Type type;
    private Coords coords;
    private String value;

    public Token(Type type, Coords coords, String value) {
        this.type = type;
        this.coords = coords;
        this.value = value;
    }

    @Override
    public String toString() {
        return  type.toString() + " " + coords + ": " + value;
    }
}
