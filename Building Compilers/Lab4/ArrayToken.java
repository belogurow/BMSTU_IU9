/**
 * Created by alexbelogurow on 21.03.17.
 */
public class ArrayToken extends Token {
    public final String value;

    public ArrayToken(String value, Position starting, Position following) {
        super(DomainTag.ARRAY, starting, following);
        this.value = value;
    }

    @Override
    public String toString() {
        return "ARRAY " + super.toString() + ": " + value;
    }
}
