public class Coords {
    public int line;
    public int position;

    public Coords(int line, int position) {
        this.line = line + 1;
        this.position = position + 1;
    }

    @Override
    public String toString() {
        return "(" + line + ", " + position + ")";
    }
}
