public class Message {
    private boolean isError;
    private String text;

    public Message(boolean isError, String text) {
        this.isError = isError;
        this.text = text;
    }

    public boolean isError() {
        return isError;
    }

    public String getText() {
        return text;
    }
}
