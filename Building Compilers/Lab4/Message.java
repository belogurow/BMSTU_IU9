/**
 * Created by alexbelogurow on 21.03.17.
 */
public class Message {
    public final boolean IsError;
    public final String Text;

    public Message(boolean isError, String text) {
        IsError = isError;
        Text = text;
    }
}
