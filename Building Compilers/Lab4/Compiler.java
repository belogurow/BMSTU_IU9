import java.util.*;

/**
 * Created by alexbelogurow on 21.03.17.
 */
public class Compiler {
    private List<Message> messages;
    private Map<String, Integer> varCodes;
    private List<String> vars;

    public Compiler() {
        messages = new ArrayList<Message>();
        varCodes = new HashMap<String, Integer>();
        vars = new ArrayList<String>();
    }

    public void addMessage(boolean isErr, Position c, String text) {
        messages.add(new Message(isErr, text, c));
    }

    public void outputMessages() {
        for (Message m : messages) {
            System.out.print(m.isError ? "Error" : "Warning");
            System.out.print(" " + m.position + ": ");
            System.out.println(m.text);
        }
    }

    public Scanner getScanner(String program) {
        return new Scanner(program, this);
    }
}

