import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by alex on 13.05.15.
 */
enum tag{ MINUS, PLUS, MULTI, DIV, LPAREN, RPAREN, VAR, NUMBER, COMMA, EQUAL}

class Lexem {
    boolean equal = false;
    String line, var_new;
    ArrayList<tag> tags;
    int count = 0, count_comma = 0, count_equal = 0, count_var = 0;

    Lexem(String line) {
        this.line = line;
        this.tags = new ArrayList<tag>();
    }

    void printLex() {
        for(int i = 0; i < tags.size(); i++)
            System.out.println(tags.get(i));
    }
    void findLexem(int n, HashMap<String, Integer> a) throws Exception {
        String s = line;
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case ' ':
                    continue;
                case '-':
                    tags.add(tag.MINUS);
                    continue;
                case '+':
                    if (count_equal == 0)
                        throw new Exception();
                    tags.add(tag.PLUS);
                    continue;
                case '*':
                    tags.add(tag.MULTI);
                    continue;
                case '/':
                    tags.add(tag.DIV);
                    continue;
                case '(':
                    tags.add(tag.LPAREN);
                    continue;
                case ')':
                    tags.add(tag.RPAREN);
                    continue;
                case '=':
                    equal = true;
                    count_equal++;
                    if (count_equal > 1)
                        throw new Exception();
                    tags.add(tag.EQUAL);
                    continue;
                case ',':
                    tags.add(tag.COMMA);
                    continue;
                default:
                    if (Character.isDigit((s.charAt(i)))) {
                        tags.add(tag.NUMBER);
                        while (i < s.length() && Character.isDigit(s.charAt(i))) {
                            i++;
                        }
                        i--;
                        continue;
                    }
                    if (Character.isAlphabetic(s.charAt(i))) {
                        tags.add(tag.VAR);
                        count_var++;
                        var_new = s.charAt(i) + "";
                        i++;
                        while (i < s.length() && (Character.isAlphabetic(s.charAt(i)) || Character.isDigit(s.charAt(i)))) {
                            var_new += s.charAt(i);
                            i++;
                        }
                        i--;
                        if (!equal) {
                            if (a.containsKey(var_new))
                                throw new Exception();
                            a.put(var_new, n);
                        }
                        continue;
                    }

            }
            throw new Exception();
        }
    }

    void for_HM (int n, HashMap<String, Integer> a, ArrayList<Graph> graph) throws Exception {
        equal = false;
        String s = line;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '=')
                equal = true;
            if (equal) {
                if (Character.isAlphabetic(s.charAt(i))) {
                    String var = s.charAt(i) + "";
                    i++;
                    while (i < s.length() && (Character.isAlphabetic(s.charAt(i)) || Character.isDigit(s.charAt(i)))) {
                        var += s.charAt(i);
                        i++;
                    }
                    i--;
                    if (!a.containsKey(var))
                        throw new Exception();
                    int x = a.get(var);
                    graph.get(n).GR.add(graph.get(x));
                    continue;
                }
            }
        }
    }

    void parse_left () throws Exception {
        if (tags.get(count) == tag.VAR) {
            count++;
            if (tags.get(count) == tag.COMMA) {
                count++;
                count_comma++;
                parse_left();
            }
            if (count < tags.size() && tags.get(count) == tag.EQUAL) {
                count++;
                if (count_comma == 0)
                    parse_right();
                else {
                    parse_right_comma();
                    if (count_comma != 0)
                        throw new Exception();
                }
            }
        }
        else
            throw new Exception();
    }

    void parse_right_comma() throws Exception {
        if (tags.get(count) == tag.VAR || tags.get(count) == tag.NUMBER) {
            count++;
            if (count < tags.size() && tags.get(count) == tag.COMMA) {
                count++;
                count_comma--;
                parse_right_comma();
            }
        }
    }

    void parse_right() throws Exception {
        parse_t();
        parse_e2();
        }

    void parse_t() throws  Exception {
        parse_f();
        parse_t2();
    }

    void parse_e2() throws Exception {
        if (count < tags.size() && (tags.get(count) == tag.MINUS || tags.get(count) == tag.PLUS)) {
            count++;
            parse_t();
            parse_e2();
        }
        if (count == tags.size()-1 && (tags.get(count) == tag.NUMBER || tags.get(count) == tag.COMMA))
            throw new Exception();
    }

    void parse_t2() throws Exception{
        if (count < tags.size() && (tags.get(count) == tag.MULTI || tags.get(count) == tag.DIV)) {
            count++;
            parse_f();
            parse_t2();
        }

    }

    void parse_f() throws Exception {
        if (tags.get(count) == tag.MINUS || tags.get(count) == tag.LPAREN || tags.get(count) == tag.NUMBER || tags.get(count) == tag.VAR) {
            count++;
            if (tags.get(count-1) == tag.MINUS)
                parse_f();
            if (tags.get(count-1) == tag.LPAREN) {
                parse_right();
                if (tags.get(count) == tag.RPAREN)
                    count++;
                else
                    throw new Exception();
            }
        }
        else
            throw new Exception();
    }


}

class Graph {
    ArrayList<Graph> GR;
    int count;
    int color;

    Graph(int count) {
        GR = new ArrayList<>();
        this.count = count;
        this.color = 0;
    }
}



public class FormulaOrder {
    public static ArrayList<Graph> dfs_first(ArrayList<Graph> graph) throws Exception {
        ArrayList<Graph> s = new ArrayList<>();
        for(int i = 0; i < graph.size(); i++)
            if (graph.get(i).color == 0)
                dfs_second(graph.get(i), s);
        return s;
    }

    public static void dfs_second(Graph v, ArrayList<Graph> s) throws Exception {
        v.color = 1;
        for(int i = 0; i < v.GR.size(); i++)
            if (v.GR.get(i).color == 0)
                dfs_second(v.GR.get(i), s);
            else
                if (v.GR.get(i).color == 1)
                    throw new Exception();
        v.color = 2;
        s.add(v);
    }


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<Graph> print = null;
        HashMap<String, Integer> a = new HashMap<>();
        a.put("]", -1);
        ArrayList<Lexem> str = new ArrayList<Lexem>();
        ArrayList<Graph> graph = new ArrayList<>();
        try {
            for (int i = 0; in.hasNextLine(); i++) {
                str.add(new Lexem(in.nextLine()));
                graph.add(new Graph(i));
                str.get(i).findLexem(i, a);
                str.get(i).parse_left();
            }
            for (int i = 0; i < str.size(); i++) {
                str.get(i).for_HM(i, a, graph);
            }
        }
        catch(Exception e){
            System.out.println("syntax error");
            return;
        }

        try {
            print = dfs_first(graph);
        }
        catch (Exception e) {
            System.out.println("cycle");
            return;
        }
        for(int i = 0; i < print.size(); i++)
            System.out.println(str.get(print.get(i).count).line);

    }

}
