import java.util.*;

/**
 * Created by alexbelogurow on 21.03.17.
 */
public class Compiler {
    private TreeMap<Position, Message> messages;

    private Map<String, Integer> nameCodes;
    private List<String> names;

    public Compiler() {
        messages = new TreeMap<>();
        nameCodes = new HashMap<>();
        names = new ArrayList<>();

    }

    public int addName(String name) {
        if (nameCodes.containsKey(name)) {
            return nameCodes.get(name);
        } else {
            int code = names.size();
            names.add(name);
            nameCodes.put(name, code);
            return code;
        }
    }

    public String getName(int code) {
        return names.get(code);
    }


}

