package com.belogurow;

/**
 * Created by alexbelogurow on 25.04.17.
 */
public class Position  {
    private String text;
    private int line, pos, index;

    public int getLine() {
        return line;
    }

    public int getPos() {
        return pos;
    }

    public int getIndex() {
        return index;
    }

    public String getText() {
        return text;
    }

    public Position(String text) {
        this.text = text;
        line = pos = 1;
        index = 0;
    }

    public Position(Position p) {
        this.text = p.getText();
        this.line = p.getLine();
        this.pos = p.getPos();
        this.index = p.getIndex();
    }

    public boolean isEOF() {
        return index == text.length();
    }

    public String toString() {
        return "(" + line + "," + pos + ")";
    }

    public int getCode() {
        return isEOF() ? -1 : text.codePointAt(index);
    }

    public boolean isWhitespace() {
        return !isEOF() && Character.isWhitespace(getCode());
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

    public boolean isDecimalDigit() {
        return !isEOF() && text.charAt(index) >= '0' && text.charAt(index) <= '9';
    }



    public Position next() {
        Position p = new Position(this);
        if (!p.isEOF()) {
            if (p.isNewLine()) {
                if (p.text.charAt(p.index) == '\r')
                    p.index++;
                p.line++;
                p.pos = 1;
            } else {
                if (Character.isHighSurrogate(p.text.charAt(p.index)))
                    p.index++;
                p.pos++;
            }
            p.index++;
        }
        return p;
    }
    public Position prev() {
        Position p = new Position(this);
        if (!p.isEOF()) {
            p.pos--;
        }
        return p;
    }


}
