public class Comment {
    private Fragment fragment;
    private String text;

    public Comment(Position start, Position end, String text) {
        this.fragment = new Fragment(start, end);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return  "COMMENT " + fragment + " " + text;

    }
}
