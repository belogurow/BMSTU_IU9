import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by alex on 16.05.15.
 */

enum tag {
    IDENT, NUMBER, PLUS, MINUS, MULTI, DIV, EQUAL, NOT_EQUAL, LESS, MORE, LESS_EQ, MORE_EQ, QUESTION, COLON, LPAREN, RPAREN, COMMA, SEMICOLON, ASSIGNMENT
}

class Lexem {
    String line, func;
    ArrayList<tag> tags;
    ArrayList<String> ident;
    ArrayList<String> vars;
    ArrayList<Integer> count_func;
    boolean assignment = false, in_func = false;
    int count = 0, count_args = 0, count_In_func = 0, j = 0, n;


    Lexem(String line, int n) {
        this.line = line;
        tags = new ArrayList<tag>();
        ident = new ArrayList<String>();
        vars = new ArrayList<String>();
        count_func = new ArrayList<Integer>();
        this.n = n;
    }

    void findLexem(HashMap<String, Integer> func_HM, ArrayList<String> aaa) throws Exception {
        String s = line;
        for(int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case ' ':
                    continue;
                case '+':
                    tags.add(tag.PLUS);
                    continue;
                case '-':
                    tags.add(tag.MINUS);
                    continue;
                case '*':
                    tags.add(tag.MULTI);
                    continue;
                case '/':
                    tags.add(tag.DIV);
                    continue;
                case '=':
                    tags.add(tag.EQUAL);
                    continue;
                case '<':
                    i++;
                    switch (s.charAt(i)) {
                        case '>':
                            tags.add(tag.NOT_EQUAL);
                            continue;
                        case '=':
                            tags.add(tag.LESS_EQ);
                            continue;
                        default:
                            i--;
                            tags.add(tag.LESS);
                            continue;
                    }
                case '>':
                    i++;
                    switch (s.charAt(i)) {
                        case '=':
                            tags.add(tag.MORE_EQ);
                            continue;
                        default:
                            i--;
                            tags.add(tag.MORE);
                            continue;
                    }
                case '?':
                    tags.add(tag.QUESTION);
                    continue;
                case ',':
                    tags.add(tag.COMMA);
                    if (in_func)
                        count_In_func++;
                    continue;
                case '(':
                    tags.add(tag.LPAREN);
                    if (assignment)
                        in_func = true;
                    count++;
                    continue;
                case ')':
                    tags.add(tag.RPAREN);
                    if (count == 1) {
                        func_HM.put(func, count_args);
                        aaa.add(n, func);
                    }
                    if (in_func) {
                        count_In_func++;
                        count_func.add(count_In_func);
                        count_In_func = 0;
                        in_func = false;
                    }
                    count++;
                    continue;
                case ';':
                    tags.add(tag.SEMICOLON);
                    continue;
                case ':':
                    i++;
                    switch (s.charAt(i)) {
                        case '=':
                            tags.add(tag.ASSIGNMENT);
                            assignment = true;
                            continue;
                        default:
                            i--;
                            tags.add(tag.COLON);
                            continue;
                    }
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
                        tags.add(tag.IDENT);
                        //count_var++;
                        String var = s.charAt(i) + "";
                        i++;
                        while (i < s.length() && (Character.isAlphabetic(s.charAt(i)) || Character.isDigit(s.charAt(i)))) {
                            var += s.charAt(i);
                            i++;
                        }
                        i--;
                        if (count == 1) {
                            ident.add(var);
                            count_args++;
                        }
                        if (count == 0)
                            func = var;
                        if (assignment)
                            vars.add(var);
                        continue;
                    }
            }
            throw new Exception();
        }
    }

    int findIdent(ArrayList<String> aaa, ArrayList<Graph> graph, String var, int n, HashMap<String, Integer> func_HM) throws Exception{
        j++;
        int lparen = 1;
        int comma = 0;
        int i;
        n += 2;
            for(i = n; lparen != 0; i++) {
                if (tags.get(i) == tag.COMMA) {
                    comma++;
                    continue;
                }
                if (tags.get(i) == tag.LPAREN) {
                    lparen++;
                    continue;
                }
                if (tags.get(i) == tag.IDENT) {
                    if (func_HM.containsKey(vars.get(j))) {
                        i = findIdent(aaa, graph, vars.get(j), i, func_HM);
                        i--;
                        continue;
                    }
                    j++;
                }
                if (tags.get(i) == tag.RPAREN) {
                    if (tags.get(i-1) == tag.LPAREN)
                        comma--;
                    //comma++;
                    lparen--;
                }
            }
        if (tags.get(i) == tag.LPAREN)
            comma--;
        else
            comma++;
        //System.out.println(this.n +": " + var + " " + comma);
        if (func_HM.get(var) != comma)
            throw new Exception();
        int size = graph.get(this.n).GR.size();
        boolean add = true;
        for(int q = 0; q < size; q++)
            if (graph.get(this.n).GR.get(q).n == aaa.indexOf(var))
                add = false;
        if (add)
            graph.get(this.n).GR.add(graph.get(aaa.indexOf(var)));
        return i;
    }

    void findError(ArrayList<String> aaa, ArrayList<Graph> graph, HashMap<String, Integer> func_HM) throws Exception {
        String s = line;
        assignment = false;
        for(int i = 0; i < tags.size(); i++) {
            if (tags.get(i) == tag.ASSIGNMENT)
                assignment = true;
            if (assignment && tags.get(i) == tag.IDENT) {
                if (!func_HM.containsKey(vars.get(j))) {
                    if (!ident.contains(vars.get(j)))
                        throw new Exception();
                }
                else {
                    i = findIdent(aaa, graph, vars.get(j), i, func_HM);
                    j--;
                }
                j++;
            }

        }
    }

    void parse() throws Exception {
        count = 0;
        parse_prog();
    }
    void parse_prog() throws Exception {
        parse_func();
        if (count < tags.size())
            //parse_prog();
            throw new Exception();
    }

    void parse_func() throws Exception {
        if (count < tags.size() && tags.get(count) == tag.IDENT) {
            count++;
            if (tags.get(count) == tag.LPAREN) {
                count++;
                parse_fal();
                if (tags.get(count) == tag.RPAREN) {
                    count++;
                    if (tags.get(count) == tag.ASSIGNMENT) {
                        count++;
                        parse_expr();
                        if (tags.get(count) == tag.SEMICOLON)
                            count++;
                        else
                            throw new Exception();
                    } else
                        throw new Exception();
                } else
                    throw new Exception();
            }
            else
                throw new Exception();
        }
    }

    void parse_fal() throws Exception {
        if (tags.get(count) == tag.IDENT)
            parse_ident();
    }

    void parse_ident() throws Exception {
        count++;
        if (count < tags.size() && tags.get(count) == tag.COMMA) {
            count++;
            parse_ident();
        }
    }

    void parse_expr() throws Exception {
        parse_comparison();
        if (count < tags.size() && tags.get(count) == tag.QUESTION) {
            count++;
            parse_comparison();
            if (tags.get(count) == tag.COLON) {
                count++;
                parse_expr();
            }
            else
                throw new Exception();
        }
    }

    void parse_comparison() throws Exception {
        parse_arith();
        if (tags.get(count) == tag.EQUAL || tags.get(count) == tag.NOT_EQUAL || tags.get(count) == tag.LESS || tags.get(count) == tag.MORE || tags.get(count) == tag.MORE_EQ || tags.get(count) == tag.LESS_EQ) {
            count++;
            parse_arith();
        }
    }

    void parse_arith() throws Exception {
        parse_term();
        parse_arith_2();
    }

    void parse_arith_2() throws Exception {
        if (tags.get(count) == tag.PLUS || tags.get(count) == tag.MINUS) {
            count++;
            parse_term();
            parse_arith_2();
        }
    }

    void parse_term() throws Exception {
        parse_factor();
        parse_term_2();
    }

    void parse_term_2() throws Exception {
        if (tags.get(count) == tag.MULTI || tags.get(count) == tag.DIV) {
            count++;
            parse_factor();
            parse_term_2();
        }
    }

    void parse_factor() throws Exception {
        if (tags.get(count) == tag.NUMBER || tags.get(count) == tag.IDENT || tags.get(count) == tag.LPAREN || tags.get(count) == tag.MINUS) {
            count++;
            if (count < tags.size() && tags.get(count-1) == tag.IDENT && tags.get(count) == tag.LPAREN) {
                count++;
                parse_actual();
                if (tags.get(count) == tag.RPAREN)
                    count++;
                else
                    throw new Exception();
            }
            if (tags.get(count-1) == tag.LPAREN) {
                parse_expr();
                if (tags.get(count) == tag.RPAREN)
                    count++;
                else
                    throw new Exception();
            }
            if (tags.get(count-1) == tag.MINUS)
                parse_factor();
        }
        else
            throw new Exception();

    }

    void parse_actual() throws Exception {
        if (count < tags.size() && (tags.get(count) == tag.NUMBER || tags.get(count) == tag.IDENT || tags.get(count) == tag.LPAREN || tags.get(count) == tag.MINUS)) {
            parse_expr();
            if (count < tags.size() && tags.get(count) == tag.COMMA) {
                count++;
                parse_actual();
            }
        }
    }
}

class Graph {
    ArrayList<Graph> GR;
    int comp, low, T1, n;

    public Graph(int n) {
        this.GR = new ArrayList<Graph>();
        this.comp = 0;
        this.T1 = 0;
        this.n = n;
    }
}

public class Modules {
    static int count = 1;
    static int time = 1;

    private static void Tarjan(ArrayList<Graph> graph) {
        Stack<Graph> stack = new Stack<Graph>();
        for (int i = 0; i < graph.size(); i++)
            if (graph.get(i).T1 == 0) {
                //System.out.println("--" + graph.get(i).n);
                VisitVertex_Tarjan(graph, graph.get(i), stack);
            }
    }

    private static void VisitVertex_Tarjan(ArrayList<Graph> graph, Graph v, Stack<Graph> stack) {
        v.T1 = v.low = time++;
        stack.push(v);
        for(int i = 0; i < v.GR.size(); i++) {
            if (v.GR.get(i).T1 == 0) {
                VisitVertex_Tarjan(graph, v.GR.get(i), stack);
            }
            if (v.GR.get(i).comp == 0 && v.low > v.GR.get(i).low)
                v.low = v.GR.get(i).low;
        }
        if (v.T1 == v.low) {
            Graph u;
            do {
                u = stack.pop();
                u.comp = count;
            } while (u != v);
            count++;
        }
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        HashMap<String, Integer> func_HM = new HashMap<String, Integer>();
        ArrayList<Lexem> str = new ArrayList<Lexem>();
        ArrayList<Graph> graph = new ArrayList<Graph>();
        ArrayList<String> aaa = new ArrayList<String>();
        try {
            for(int i = 0; in.hasNextLine(); i++) {
                String s = in.nextLine();
                if (s.isEmpty())
                    continue;
                graph.add(new Graph(i));
                str.add(new Lexem(s, i));
                str.get(i).findLexem(func_HM, aaa);
                str.get(i).parse();
            }
            for(int i = 0; i < str.size(); i++)
                str.get(i).findError(aaa, graph, func_HM);
        }
        catch (Exception e) {
            System.out.println("error");
            return;
        }
        Tarjan(graph);
        //System.out.println();
        System.out.print(count - 1);

    }
}
