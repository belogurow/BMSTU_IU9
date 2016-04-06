import java.math.BigInteger;
import java.util.Scanner;


public class FastFib {
        public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
        	long n = in.nextLong();
		BigInteger a1new = BigInteger.ONE, a2new = BigInteger.ONE, a3new = BigInteger.ONE, a4new = BigInteger.ZERO, a1, a2, a3, a4;
		BigInteger b1 = BigInteger.ZERO, b2 = BigInteger.ONE;
		
		while (n != 0) {
			if (n % 2 == 1) {
				a3 = b1;
				b1 = b1.multiply(a1new).add(b2.multiply(a3new));
				b2 = a3.multiply(a2new).add(b2.multiply(a4new));
			}
			
			a1 = a1new;
			a2 = a2new;
			a3 = a3new;
			a4 = a4new;
			a1new = a1.multiply(a1).add(a2.multiply(a3));
			a2new = a1.multiply(a2).add(a2.multiply(a4));
			a3new = a3.multiply(a1).add(a4.multiply(a3));
			a4new = a3.multiply(a2).add(a4.multiply(a4));
			n /= 2;
			//System.out.println(a1new + " " + a2new);
			//System.out.println(a3new + " " + a4new + "--");
		}
		System.out.println(b1);
	}
}