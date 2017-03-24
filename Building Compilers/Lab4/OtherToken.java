/**
 * Created by alexbelogurow on 21.03.17.
 */
public class OtherToken extends Token{
    public final DomainTag tag;


    public OtherToken(DomainTag tag, Position starting, Position following) {
        super(tag, starting, following);
        this.tag = tag;


    }
}
