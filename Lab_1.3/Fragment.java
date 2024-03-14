public class Fragment {
    private Position startPos;
    private Position endPos;

    public Fragment(Position startPos, Position endPos) {
        this.startPos = startPos;
        this.endPos = endPos;
    }

    @Override
    public String toString() {
        return startPos + " - " + endPos;
    }
}
