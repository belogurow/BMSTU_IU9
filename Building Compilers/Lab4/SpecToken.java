/**
 * Created by alexbelogurow on 21.03.17.
 */
public class SpecToken extends Token{


    protected SpecToken(DomainTag tag, Position starting, Position following) {
        super(tag, starting, following);


        assert (tag == DomainTag.SIGN ||
                tag == DomainTag.DOT ||
                tag == DomainTag.COLON ||
                tag == DomainTag.COMMA ||
                tag == DomainTag.SEMICOLON ||
                tag == DomainTag.END_OF_PROGRAM);
    }
}
