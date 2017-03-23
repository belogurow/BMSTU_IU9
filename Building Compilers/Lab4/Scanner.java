import java.util.ArrayList;

/**
 * Created by alexbelogurow on 21.03.17.
 */
public class Scanner {
    public final String Program;

    private Compiler compiler;
    private Position cur;
    private ArrayList<Fragment> comments;

    public Scanner(String program, Compiler compiler) {
        Program = program;
        this.compiler = compiler;
        this.cur = cur;
        this.comments = comments;
    }
}
