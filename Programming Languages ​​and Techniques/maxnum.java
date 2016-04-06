import java.util.*;

public class MaxNum {
    public static void main (String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(), i, x, k;
        int[] p = new int[n];
        String s1, s2;
        for(i = 0; i < n; i++) {
            p[i] = in.nextInt();
            //System.out.println(p[i]);
        }
        for(i = 0; i < n-1; i++) {
            s1 = p[i] + "" + p[i+1];
            s2 = p[i+1] + "" + p[i];
            if (s1.compareTo(s2) <  0) {
                int swap = p[i];
                p[i] = p[i + 1];
                p[i + 1] = swap;
                i = -1;
            }
        }
        for(i = 0; i < n; i++)
            System.out.print(p[i]);
    }
}