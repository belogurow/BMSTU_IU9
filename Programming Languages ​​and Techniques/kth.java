import java.util.Scanner;

public class Kth {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long k = in.nextLong();
        long a = 9;
	long i, res = 0;
	for(i = 0; k - a > 0; i++) {	
		k = k - a; 
		a = 9*(i+2)*(long)Math.pow(10, i+1);
	}
	a = (long)Math.pow(10, i) + k / (i+1);
	long num = (long)Math.log10(a)+1;
	k = k % (i + 1);
	//System.out.println(a + " " + k + " " + i);
	a = a % (long)Math.pow(10, num-k);
	if ((long)Math.log10(a)+1 < (num - k))
		System.out.println(0);
	
	//if (a < 10) 
	//	System.out.println(a);
	else
		System.out.println(a / (long)Math.pow(10, (long)Math.log10(a)));
	
    }
}