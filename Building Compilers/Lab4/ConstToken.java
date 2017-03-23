/**
 * Created by alexbelogurow on 21.03.17.
 */
public class ConstToken extends Token {

    public final String value;

    protected ConstToken(String value, Position starting, Position following) {
        super(DomainTag.CONST, starting, following);
        this.value = value;
    }

    @Override
    public String toString() {
        return "CONST " + super.toString() + ": " + value;
    }
}
