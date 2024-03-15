public class Fragment {
    private Position startPos;
    private Position endPos;

    public Fragment(Position startPos, Position endPos) {
        this.startPos = new Position(startPos);
        this.endPos = new Position(endPos);
    }

    @Override
    public String toString() {
        return startPos + " - " + endPos;
    }
}
