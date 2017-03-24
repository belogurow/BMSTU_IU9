
/**
 * Created by alexbelogurow on 21.03.17.
 */
public class Scanner {
    public final String program;
    private Compiler compiler;
    private Position cur;

    public Scanner(String program, Compiler compiler) {
        this.compiler = compiler;
        this.program = program;
        cur = new Position(program);
    }

    public Token nextToken() {
        while (!cur.isEOF()) {
            while (cur.isWhitespace())
                cur = cur.next();
            Token token = new OtherToken(DomainTag.OTHER, cur, cur);
            switch (cur.getCode()) {
                case '#':
                    token = readConst(cur);
                    break;
                case ',':
                case ';':
                    token = readArray(cur);
                    break;
                case '.':
                case ':':
                    token = readVariable(cur);
                    break;
                case 'P':
                case 'D':
                case 'F':
                    token = readKeyword(cur);
                    break;
            }
            if (token.tag == DomainTag.OTHER) {
                compiler.addMessage(true, cur, "Token: unrecognized token");
                cur = cur.next();
            } else {
                cur = token.coords.following;
                return token;
            }
        }
        return new OtherToken(DomainTag.END_OF_PROGRAM, cur, cur);
    }

    private Token readKeyword(Position cur) {
        Position p = cur.next();
        if (p.getCode() == 'L') {
            p = p.next();
            if (p.getCode() == 'E') {
                p = p.next();
                if (p.getCode() == 'A') {
                    p = p.next();
                    if (p.getCode() == 'S') {
                        p = p.next();
                        if (p.getCode() == 'E') {
                            p = p.next();
                            return new KeywordToken("PLEASE", cur, p);
                        }
                    }
                }
            }
        }

        if (p.getCode() == 'O') {
            p = p.next();
            if (p.getCode() == 'R') {
                p = p.next();
                if (p.getCode() == 'G') {
                    p = p.next();
                    if (p.getCode() == 'E') {
                        p = p.next();
                        if (p.getCode() == 'T') {
                            p = p.next();
                            return new KeywordToken("FORGET", cur, p);
                        }
                    }
                }
            }
            else {
                return new KeywordToken("DO", cur, p);
            }
        }
        return new OtherToken(DomainTag.OTHER, cur, cur);
    }

    private Token readVariable(Position cur) {
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toChars(cur.getCode()));
        Position p = cur.next();
        if (p.isDecimalDigit()) {
            while (p.isDecimalDigit()) {
                sb.append(Character.toChars(p.getCode()));
                p = p.next();
            }
            return new VariableToken(sb.toString(), cur, p);
        }
        compiler.addMessage(false, p.prev(), "Definition of Variable: after \'.\' or \':\' must be a number");
        return new VariableToken(sb.toString(), cur, p);
    }

    private Token readArray(Position cur) {
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toChars(cur.getCode()));
        Position p = cur.next();
        if (p.isDecimalDigit()) {
            while (p.isDecimalDigit()) {
                sb.append(Character.toChars(p.getCode()));
                p = p.next();
            }
            return new ArrayToken(sb.toString(), cur, p);
        }
        compiler.addMessage(false, p.prev(), "Definition of Array: after \',\' or \';\' must be a number");
        return new ArrayToken(sb.toString(), cur, p);
    }

    private Token readConst(Position cur) {
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toChars(cur.getCode()));
        Position p = cur.next();
        if (p.isDecimalDigit()) {
            while (p.isDecimalDigit()) {
                sb.append(Character.toChars(p.getCode()));
                p = p.next();
            }
            return new ConstToken(sb.toString(), cur, p);
        }
        compiler.addMessage(false, p.prev(), "Definition of Const: after \'#\' must be a number");
        return new ConstToken(sb.toString(), cur, p);
    }

}
