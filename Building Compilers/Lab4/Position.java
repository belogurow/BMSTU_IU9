import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by alexbelogurow on 21.03.17.
 */
public class Position implements Comparable<Position>{
    private String text;
    private int line, pos, index;

    public Position(String text) {
        this.text = text;
        line = pos = 1;
        index = 0;
    }

    public int getLine() {
        return line;
    }

    public int getPos() {
        return pos;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "(" + line + "," + pos + ")";
    }

    public int getCp() {
        return (index == text.length()) ? -1 : text.codePointAt(index);
    }

    public boolean isWhiteSpace() {
        return index != text.length() && Character.isWhitespace(text.charAt(index));
    }

    public boolean isDecimalDigit() {
        return index != text.length() && text.charAt(index) >= '0' && text.charAt(index) <= '9';
    }

    public boolean isNewLine() {
        // \n - Modern Mac and Unix
        // \r\n - Windows and Dos

        if (index == text.length()) {
            return true;
        }

        if (text.charAt(index) == '\r' && index + 1 < text.length()) {
            return (text.charAt(index + 1) == '\n');
        }

        return (text.charAt(index) == '\n');
    }

    public void next() {
        if (index < text.length()) {
            if (this.isNewLine()) {
                if (text.charAt(index) == '\r') {
                    index++;
                }
                line++;
                pos = 1;
            }
            else {
                if (Character.isHighSurrogate(text.charAt(index))) {
                    index++;
                }
                pos++;
            }
            index++;
        }
    }

    @Override
    public int compareTo(Position o) {
        return this.index - o.index;
    }
}
