import java.util.ArrayList;

public class Tree {
    public record Rule(String name, ArrayList<Record> children){
    }
    public record Token(Type type){}

    public record Graph(String name, ArrayList<Record> children){}
}