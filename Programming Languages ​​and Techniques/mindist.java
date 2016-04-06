import java.util.Scanner;

public class MinDist {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        char x = in.next().charAt(0), y = in.next().charAt(0);
        int x1 = -1, y1 = -1, dist = s.length();
        for(int i = 0; i < s.length(); i++) {
		if (x1 != -1) x1++; 
		if (y1 != -1) y1++; 
		if (s.charAt(i) == x) 
				x1 = 0;		
		if (s.charAt(i) == y) 
				y1 = 0;
		if ((x1 != -1) && (y1 != -1))
			dist = Math.min(dist, Math.abs(x1-y1));
	}	
	if (dist == s.length()) 
		dist = Math.abs(x1-y1);
        System.out.println(dist-1);
    }
}
