import java.util.TreeMap;

public class MessageList {
    private TreeMap<Position, Message> messages;

    public MessageList() {
        this.messages = new TreeMap<>();
    }

    public void addMessage(boolean isError, Position position, String text){
        messages.put(new Position(position), new Message(isError, text));
    }

    public void outPutMessages(){
        messages.forEach(((position, message) -> {
            System.out.print(message.isError() ? "Error" : "Warning");
            System.out.print(" " + position + " ");
            System.out.println(message.getText());
        }));
    }
}
