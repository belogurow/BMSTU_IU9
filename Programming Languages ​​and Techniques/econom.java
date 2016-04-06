import java.util.Scanner;
import java.util.regex.Pattern;

public class Econom {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        int count = 0, i;
        for(;s.length() > 1; count++) {
            i = s.indexOf(')');
            s = s.replaceAll(Pattern.quote(s.substring(i - 4, i + 1)), count + "");
            //System.out.println(s);
            //count++;
        }
        System.out.println(count);
    }
}
