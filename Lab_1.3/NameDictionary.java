import java.util.*;

public class NameDictionary {
    ArrayList<String> names;
    HashMap<String, Integer> namePointers;


    public NameDictionary() {
        names = new ArrayList<>();
        namePointers = new HashMap<>();
    }

    public int addName(String name) {
        if (!names.contains(name)) {
            namePointers.put(name, namePointers.size());
            names.add(name);
        }
        return namePointers.get(name);
    }

    public boolean contains(String name) {
        return names.contains(name);
    }

    public String getName(int pointer) {
        return names.get(pointer);
    }

    public void outputDictionary(){
        for (int i = 0; i < names.size(); i++) {
            System.out.println(i + " "  + names.get(i));
        }
    }

}
