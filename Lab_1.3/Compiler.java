import java.util.ArrayList;
import java.util.TreeMap;

public class Compiler {
    private MessageList messageList;
    private NameDictionary nameDictionary;

    public Compiler() {
        this.messageList = new MessageList();
        this.nameDictionary = new NameDictionary();
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

    public String getName(int pointer) {
        return nameDictionary.getName(pointer);
    }
}
