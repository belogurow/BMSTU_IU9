/**
 * Created by alexbelogurow on 21.03.17.
 */
public class Main {
    public static void main(String[] args) {
        Position p = new Position("qw \r\n q");
        //p.nextPos();
        while(p.getCp() != -1) {
            System.out.println(p.getCp());
            System.out.println(p);
            p.next();
        }
        //System.out.println(p.getCp());



        System.out.println("Hello World");
    }
}
