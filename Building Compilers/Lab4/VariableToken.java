/**
 * Created by alexbelogurow on 21.03.17.
 */
public class VariableToken extends Token {
    public final long value;

    protected VariableToken(long value, Position starting, Position following) {
        super(DomainTag.VARIABLE, starting, following);
        this.value = value;
    }

    @Override
    public String toString() {
        return "VARIABLE " + super.toString() + ": " + value;
    }
}
