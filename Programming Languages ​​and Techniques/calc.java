import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
	
enum tag{ MINUS, PLUS, MULTI, DIV, LPAREN, RPAREN, VAR, NUMBER}

class Lexem {
    ArrayList<String> s = new ArrayList<String>();
    HashMap<String, Integer> a = new HashMap<String, Integer>();
    Scanner in1;
    ArrayList<tag> tags = new ArrayList<tag>();
    int n = 0;
    int res;

    void findLexem(Scanner in1, String s) {
        this.in1 = in1;
        for(int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case '-': tags.add(tag.MINUS); this.s.add("-"); break;
                case '+': tags.add(tag.PLUS); this.s.add("+"); break;
                case '*': tags.add(tag.MULTI); this.s.add("*"); break;
                case '/': tags.add(tag.DIV); this.s.add("/"); break;
                case '(': tags.add(tag.LPAREN); this.s.add("("); break;
                case ')': tags.add(tag.RPAREN); this.s.add(")"); break;
                default:
                    if(Character.isDigit((s.charAt(i)))) {
                        tags.add(tag.NUMBER);
                        String var = "";
                        while (i < s.length() && Character.isDigit(s.charAt(i))) {
                            var += s.charAt(i) + "";
                            i++;
                        }
                        i--;
                        this.s.add(var);
                        break;
                    }
                    if (Character.isAlphabetic(s.charAt(i))) {
                        tags.add(tag.VAR);
                        String var = s.charAt(i) + "";
                        i++;
                        while (i < s.length() && (Character.isAlphabetic(s.charAt(i)) || Character.isDigit(s.charAt(i)))) {
                            var += s.charAt(i);
                            i++;
                        }
                        i--;
                        this.s.add(var);
                        if (!a.containsKey(var))
                            a.put(var, in1.nextInt());
                    }

            }
        }
    }

    int parse() throws Exception {
        int res = parse_e();
        if (n == tags.size())
            return res;
        else throw new Exception();
    }
 
    int parse_e() throws Exception {
        res = parse_t();
        parse_e2();
        return res;
    }

    int parse_e2() throws Exception {
        if (n < tags.size() && (tags.get(n) == tag.MINUS || tags.get(n) == tag.PLUS)) {
            if (tags.get(n) == tag.MINUS) {
                n++;
                res -= parse_t();
            }
            else if (tags.get(n) == tag.PLUS) {
                n++;
                res += parse_t();
            }
            parse_e2();
        }
        return res;
    }
 
    int parse_t() throws Exception {
        res = parse_f();
        parse_t2();
        return res;
    }

    int parse_t2() throws Exception {
        if (n < tags.size() && (tags.get(n) == tag.DIV || tags.get(n) == tag.MULTI)) {
            if (tags.get(n) == tag.DIV) {
                n++;
                res /= parse_f();
            }
            else if (tags.get(n) == tag.MULTI) {
                n++;
                res *= parse_f();
            }
            parse_t2();
        }
        return res;
    }

    int parse_f() throws Exception {
        if (tags.get(n) == tag.MINUS) {
            n++;
            return -parse_f();
        }
        else if (tags.get(n) == tag.LPAREN) {
            n++;
            int result = parse_e();
            if (tags.get(n++) != tag.RPAREN)
                throw new Exception();
            else return res;
        }
        else if(tags.get(n) == tag.NUMBER)
            return Integer.parseInt(s.get(n++));
        else if(tags.get(n) == tag.VAR)
            return a.get(s.get(n++)).intValue();
        else throw new Exception();
    }


}
public class Calc {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        Lexem lex = new Lexem();
        lex.findLexem(in, s);
        try {
            int res = lex.parse();
            System.out.println(res);
        }
        catch (Exception e) {
            System.out.println("error");
        }
    }
}