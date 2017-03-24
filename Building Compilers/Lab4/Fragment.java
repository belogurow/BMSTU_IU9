/**
 * Created by alexbelogurow on 21.03.17.
 */
public class Fragment {
    public final Position starting, following;

    public Fragment(Position starting, Position following) {
        this.starting = starting;
        this.following = following;
    }

    public String toString() {
        return starting.toString() + "-" + following.toString();
    }
}
