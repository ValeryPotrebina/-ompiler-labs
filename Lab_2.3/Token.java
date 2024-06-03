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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Coords getCoords() {
        return coords;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
