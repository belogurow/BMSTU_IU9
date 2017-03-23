/**
 * Created by alexbelogurow on 21.03.17.
 */
public class Fragment {
    public final Position starting;
    public final Position following;


    public Fragment(Position starting, Position following) {
        this.starting = starting;
        this.following = following;
    }

    @Override
    public String toString() {
        return starting.toString() + "-" + following.toString();
    }
}
