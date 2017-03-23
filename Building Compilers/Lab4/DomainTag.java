/**
 * Created by alexbelogurow on 21.03.17.
 */
public enum DomainTag {
    NUMBER(0),          // Number
    SIGN(1),            // Sign #
    DOT(2),             // Dot .
    COLON(3),           // Colon :
    COMMA(4),           // Comma ,
    SEMICOLON(5),       // Semicolon ;
    CONST(6),
    VARIABLE(7),
    ARRAY(8),
    KEYWORD(9),
    END_OF_PROGRAM(10);



    private final int id;
    DomainTag(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
