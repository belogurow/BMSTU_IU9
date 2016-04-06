import java.util.Scanner;

/**
 * Created by alex on 09.04.15.
 *
 5
 6 0 0 0 -7 0
 0 0 -5 0 0 8
 7 -1 -4 0 5 4
 8 8 0 0 0 1
 0 0 0 3 -5 0
 */

class Rational{
    int num, den;

    public int nod(int a, int b) {
        int c;
        a = Math.abs(a);
        b = Math.abs(b);
        while(b != 0) {
            c = a % b;
            a = b;
            b = c;
        }
        if (a == 0) {
            System.out.println("No solution");
            System.exit(0);
        }
        return a;
    }

    public Rational div(Rational x) {
        Rational z = new Rational();
        z.num = x.den * num;
        z.den = x.num * den;
        int a = z.nod(z.num, z.den);
        z.num /= a;
        z.den /= a;
        return z;
    }

    public Rational mul(Rational x) {
        Rational z = new Rational();
        z.num = x.num * num;
        z.den = x.den * den;
        int a = z.nod(z.num, z.den);
        //System.out.println(z);
        z.num /= a;
        z.den /= a;
        return z;
    }

    public Rational sub(Rational x) {
        Rational z = new Rational();
        z.num = x.den * num - x.num * den;
        z.den = x.den * den;
        int a = z.nod(z.num, z.den);
        z.num /= a;
        z.den /= a;
        return z;
    }

    public String toString() {
        return num + "/" + den;
    }
}
public class Gauss {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n, j, m = 1;
        n = in.nextInt();
        Rational[][] A = new Rational[n][n+1];
        for(int i = 0; i < n; i++) {
            boolean test = true;
            for (j = 0; j < n + 1; j++) {
                A[i][j] = new Rational();
                A[i][j].num = in.nextInt();
                A[i][j].den = 1;
                if (A[i][j].num != 0 && j < n) {
                    test = false;
                    //System.out.println(i + " " + j + " " + test);
                }
            }
            if (test == true) {
                System.out.println("No solution");
                System.exit(0);
            }
        }

        Rational z = new Rational();
        Rational x = new Rational();
        z.num = 0;
        z.den = 1;
        for(int i = 0; i < n+1; i++) {
            int test = 1;
            for (j = 0; j < n; j++) {
                if (A[j][i].num != 0 && i < n)
                    test = 0;
                if (A[j][i].num != 0 && i == n)
                    m = 0;
            }
            if (test == 1 && i < n) {
                System.out.println("No solution");
                System.exit(0);
            }
            if (m == 1 && i == n) {
                for(int q = 0; q < n; q++)
                    System.out.println(z);
                System.exit(0);
            }

        }
        int q;
        //j = 0;
        //for(q = 0; A[j][q].num == 0 && A[j+1][q].num == 0; q++);
        //System.out.println(A[0][0].num == 0);
        for(j = 0; j < n-1; j++) {
            for(q = 0; A[j][q].num == 0 && A[j+1][q].num == 0; q++);
            //System.out.println(j + " " + q);
            if (A[j][q].num == 0) {
                //System.out.println(j + "-" + q);
                for (int i = 0; i < n + 1; i++) {
                    z = A[j][i];
                    A[j][i] = A[j + 1][i];
                    A[j + 1][i] = z;
                }
                j = -1;
            }
        }

        /*for(int i = 0; i < n; i++) {
            for (j = 0; j < n + 1; j++)
                System.out.print(A[i][j] + " ");
            System.out.println();
        }*/


        z = A[1][0];
        z = z.div(A[0][0]);
        for(q = 0; q < n-1; q++)
            for(j = q+1; j < n; j++) {
                if (A[q][q].num == 0) {
                    //System.out.println(q + " " + j);
                    for(int p = q; p < n+1; p++) {
                        x = A[j][p];
                        A[j][p] = A[j-1][p];
                        A[j-1][p] = x;
                    }
                }

                z = A[j][q].div(A[q][q]);
                //System.out.println(z);
                /*for(int w = 0; w < n; w++) {
                    for (int s = 0; s < n + 1; s++)
                        System.out.print(A[w][s] + " ");
                    System.out.println("");
                }
                System.out.println();*/
                for (int i = q; i < n + 1; i++)
                        A[j][i] = A[j][i].sub(A[q][i].mul(z));
            }

        //System.out.println("obratnyu");

        for(q = n-1; q > 0; q--)
            for(j = q-1; j >= 0; j--) {
                z = A[j][q].div(A[q][q]);
                //System.out.println(z);
                for(int i = q; i < n+1; i++)
                    A[j][i] = A[j][i].sub(A[q][i].mul(z));
            }

        for(int i = 0; i < n; i++) {
            z = A[i][n].div(A[i][i]);
            if (z.den < 0) {
                z.den = Math.abs(z.den);
                if (z.num < 0)
                    z.num = Math.abs(z.num);
                else
                    z.num *= -1;
                System.out.println(z);
                continue;
            }
            System.out.println(z);
        }


        /*for(int i = 0; i < n; i++) {
            for(j = 0; j < n+1; j++)
                System.out.print(A[i][j] + " ");
            System.out.println();
        }*/
    }

}
