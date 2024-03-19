import java.util.ArrayList;

public class Compiler {
    private MessageList messageList;
    private NameDictionary nameDictionary;
    private ArrayList<Comment> comments;

    public Compiler() {
        this.messageList = new MessageList();
        this.nameDictionary = new NameDictionary();
        this.comments = new ArrayList<>();
    }

    public Scanner getScanner(String program){
        return new Scanner(program, this);
    }

    public int addName(String name){
        return nameDictionary.addName(name);
    }

    public void addMessage(boolean isError, Position position, String text){
        messageList.addMessage(isError, position, text);
    }

    public void addComment(Position start, Position end, String text){
        comments.add(new Comment(start, end, text));
    }
    public String getName(int pointer) {
        return nameDictionary.getName(pointer);
    }

    public void outPutMessages(){
        messageList.outPutMessages();
    }

    public void outPutComments() {
        comments.forEach((System.out::println));
    }
    public void outputDictionary(){
        nameDictionary.outputDictionary();
    }
}
