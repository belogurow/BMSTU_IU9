import java.util.*;

import static java.lang.Math.floor;
import static java.lang.Math.pow;

public class Nastya {
    public static int it=0;

    static float f(float x){
        return (float)(5*pow(x,6)-36*pow(x,5)-165/2*pow(x,4)-60*pow(x,3)+36);
    }

    static float f_der(float x){
        return (float) (30*pow(x,5)-180*pow(x,4)-330*pow(x,3)-180*pow(x,2));
    }
    /*
    public static double halfSectionMethod(double a, double b, double eps){
        double x_middle = (a+b)/2;
        double L = Math.abs(b-a);
        while (L > eps) {
            double y = a + L / 4;
            double z = b - L / 4;
            double f_x = f(x_middle);
            double f_y = f(y);
            double f_z = f(z);
            if (f_y < f_x) {
                b = x_middle;
                x_middle = y;
            } else {
                if (f_z < f_x) {
                    a = x_middle;
                    x_middle = z;
                } else {
                    a = y;
                    b = z;
                }
            }
            L = Math.abs(b-a);
            it++;
        }
        return x_middle;
    }

    public static double  goldenSectionMethod(double a, double b, double eps){
        double z,y;
        double PHI = (1.+Math.sqrt(5.))/2.;
        while (Math.abs(b - a) > eps){
            z = b - (b - a) / PHI;
            y = a + (b - a) / PHI;
            if (f(y) <= f(z))
                a = z;
            else
                b = y;
            it++;
        }
        return (a + b) / 2;
    }

    public static double methodFibonacci(double a, double b, double eps){
        double L = Math.abs(b-a);
        double y,z;
        int N = 3;
        ArrayList<Double> fibArr = new ArrayList<>(Arrays.asList(new Double[]{1.,1.,2.,3.}));//вынести в отделаный массив
        for (double f1 = 2., f2 = 3.; fibArr.get(fibArr.size()-1) < L/eps; ++N) {
            fibArr.add(f1+f2);
            f1 = f2;
            f2 = fibArr.get(fibArr.size()-1);
        }
        for (int i = 1; i != N-3; i++) {
            y = a + fibArr.get(N - i - 1) / fibArr.get(N - i + 1) * (b - a);
            z = a + fibArr.get(N - i) / fibArr.get(N - i + 1) * (b - a);
            if (f(y) <= f(z))
                b = z;
            else
                a = y;
            it++;
        }
        return (a + b) / 2;
    }*/
    public static float min(float x_0,float x_1,float x_2){
        if (f(x_0) < f(x_1)){
            if (f(x_0) < f(x_2)){
                return x_0;
            }
            else{
                return x_2;
            }
        }
        else{
            if (f(x_1) < f(x_2)){
                return x_1;
            }
            else{
                return x_2;
            }
        }
    }
    public static float quadraticInterpolation(float a1, float eps){
        float step = 0.01f;
        float a2, a3, f_min, alfa, a_min;
        float cond1, cond2;

        while (true) {
            //шаг 2
            a2 = a1 + step;
            //шаг 4
            if (f(a1) > f(a2)) {
                a3 = a1 + 2 * step;
            } else {
                a3 = a1 - 2 * step;
            }
            //step 6
            while (true){
                it++;
                a_min = min(a1, a2, a3);
                f_min = f(a_min);

                float den = ((a2 - a3) * f(a1) + (a3 - a1) * f(a2) + (a1 - a2) * f(a3));

                if (Math.abs(den) > 0.1) {
                    alfa = (float) (0.5 * ((Math.pow(a2, 2) - Math.pow(a3, 2)) * f(a1) +
                            (Math.pow(a3, 2) - Math.pow(a1, 2)) * f(a2) +
                            (Math.pow(a1, 2) - Math.pow(a2, 2)) * f(a3)) /
                            den);
                } else {
                    //System.out.println("den == 0 ");
                    a1 = a_min + step; //перейти на ш 2
                    break;
                }
                cond1 = (f_min - f(alfa)) / f(alfa);
                cond2 = (a_min - alfa) / alfa;
                //System.out.println("alfa "+ alfa);
                if (Math.abs(cond1) > eps || Math.abs(cond2) > eps) {
                    if (a1 <= alfa && alfa <= a3) {
                        //перейти к шагу 6
                        if (alfa < a2) {
                            a3 = a2;
                            a2 = alfa;
                        } else {
                            a1 = a2;
                            a2 = alfa;
                        }
                    } else {
                        a1 = alfa;
                        break;
                    }
                } else {
                    return alfa;
                }
            }
        }
    }

    public static float qubicInterpolation(float a1, float eps){
        ArrayList<Float> points = new ArrayList<>();
        float a2 = a1;
        float alfa = 0.0f;
        float step = 0.02f;
        if (f_der(a1)< 0) {
            int i = 0;
            do {
                a1 = a2;
                a2 += pow(2, i) * step;
                ++i;
                it++;
            } while (f_der(a1) * f_der(a2) > 0.0f);
        } else {
            int i = 0;
            do {
                a1 = a2;
                a2 -= pow(2, i) * step;
                ++i;
                it++;
            } while (f_der(a1) * f_der(a2) > 0.0f);
        }
        //System.out.println(a1);
        //System.out.println(a2);
        /*if (f_der(a1) < 0.f){
            int i = 0;
            do {
                float x_new = a1 + (float)pow(2,i)*step;
                points.add(x_new);
                a1 = x_new;
                i++;
            } while (f_der(points.get(points.size()-2))*f_der(points.get(points.size()-1)) <= 0.0f );
            System.out.println("sdsd");
        }
        else {
            int i = 0;
            do {
                float x_new = a1 - (float)pow(2,i)*step;
                points.add(x_new);
                a1 = x_new;
                i++;
            } while (f_der(points.get(points.size()-2))*f_der(points.get(points.size()-1)) <= 0.0f);
        }
        System.out.println(f_der(points.get(points.size()-2))*f_der(points.get(points.size()-1)) <= 0.0f );
*/
        float z,w,mu, cond1,cond2;
        while (true){
            it++;
            z = 3 * (f(a1) - f(a2)) / (a2 - a1) + f_der(a1) + f_der(a2);
            if (a1 < a2) {
                w = (float)Math.sqrt(pow(z, 2) - f_der(a1) * f_der(a2));
            } else {
                w = -(float)Math.sqrt(pow(z, 2) - f_der(a1) * f_der(a2));

            }
            mu = (f_der(a2) + w - z) / (f_der(a2) - f_der(a1) + 2 * w);

            if (mu < 0.0f) {
                alfa = a2;
            }
            if (0.0f <= mu && mu <= 1.0f) {
                alfa = a2 - mu * (a2 - a1);
            }
            if (mu > 1.f) {
                alfa = a1;
            }
            while (f(alfa) >= f(a1) && Math.abs((alfa - a1) /alfa) > eps) {
                alfa = alfa -(alfa - a1) / 2;
            }
            //System.out.println("kllk");
            cond1 = f_der(alfa);
            cond2 = (alfa - a1)/alfa;
            //System.out.println("a1= "+a1+" a2= "+a2);
            //System.out.println(cond1);
            //System.out.println(cond2);
            //System.out.println(alfa);
            if (Math.abs(cond1) > eps || Math.abs(cond2) > eps ) {
                if (f_der(alfa) * f_der(a1) <= 0.0f) {
                    a2 = a1;
                    a1 = alfa;
                    //System.out.println("if1");
                }
                if (f_der(alfa) * f_der(a2) <= 0.0f) {
                    a1 = alfa;
                    //System.out.println("if2");

                }
            }
            else {
                return alfa;
            }

        }
        //return alfa;
    }

    public static void main(String[] args) {
        // Алгоритм Свенна:
        double x_0 = -13.;
        double t = 2.;
        double a_0 = 0., b_0 = 10., delta = t ;
        /*while (true){
            if (f(x_0 - t) <= f(x_0) && f(x_0) >= f(x_0 + t)){
                t++;
            }
            if (f(x_0 - t) >= f(x_0) && f(x_0) <= f(x_0 + t)) {
                a_0 = x_0 - t;
                b_0 = x_0 + t;//начальный интервал неопределенности
                break;
            } else {
                if (f(x_0 - t) >= f(x_0) && f(x_0) >= f(x_0 + t)) {
                    delta = t;
                    a_0 = x_0;
                    x_0 = x_0 + t;
                }
                if (f(x_0 - t) <= f(x_0) && f(x_0) <= f(x_0 + t)) {
                    delta = -t;
                    b_0 = x_0;
                    x_0 = x_0 - t;
                }
                while (true) {
                    double x_k = x_0 + 2 * delta;
                    if (f(x_k) < f(x_0) && delta == t) {
                        a_0 = x_0;
                    }
                    if (f(x_k) < f(x_0) && delta == -t) {
                        b_0 = x_0;
                    }
                    if (f(x_k) >= f(x_0)) {
                        break;
                    }
                    x_0 = x_k;
                }
                break;
            }
        }
        System.out.println("Алгоритм Свенна:");

        System.out.println("Итервал: ("+a_0+", " +b_0+")");

        double a = a_0;
        double b = b_0;
        double eps = 0.01;

        System.out.println("==============================");

        System.out.println("Метод половинного деления");
        double x = halfSectionMethod(a, b, eps);
        System.out.println("x =" + x);
        System.out.println("f(x)=" + f(x));
        System.out.println("Количество итераций " + it);

        System.out.println("==============================");

        System.out.println("Метод золотого сечения");
        it = 0;
        x = goldenSectionMethod(a, b, eps);
        System.out.println("x =" + x);
        System.out.println("f(x)=" + f(x));
        System.out.println("Количество итераций " + it);

        System.out.println("==============================");

        System.out.println("Метод Фибоначчи");
        it = 0;
        x = methodFibonacci(a, b, eps);
        System.out.println("x =" + x);
        System.out.println("f(x)=" + f(x));
        System.out.println("Количество итераций " + it);*/

        System.out.println("Метод квадратичной интерполяции");
        it = 0;
        float eps = 0.01f;
        float x_ = quadraticInterpolation(-13.0f, eps);
        System.out.println("x =" + x_);
        System.out.println("f(x)=" + f(x_));
        System.out.println("Количество итераций " + it);

        System.out.println("Метод кубической интерполяции");
        it = 0;
        x_ = qubicInterpolation(-13.0f, 0.04f);
        System.out.println("x =" + x_);
        System.out.println("f(x)=" + f(x_));
        System.out.println("Количество итераций " + it);
    }
}