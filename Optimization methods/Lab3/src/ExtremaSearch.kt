import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.min
import kotlin.math.pow
import kotlin.system.measureTimeMillis


class ExtremaSearch {
    companion object {
        fun svannMethod(xStart: Double, stepSize: Double, function: (x: Double) -> Double): Interval {
            PrintUtils.printInfoStart("Svann Method")

            var k = 0
            lateinit var interval: Interval

            val timeExecution = measureTimeMillis {


                val xValues = arrayListOf(xStart)

                val funResultWithoutStepSize = function(xStart - stepSize)
                val funResultOnStart = function(xStart)
                val funResultWithStepSize = function(xStart + stepSize)

                interval = Interval(funResultWithoutStepSize, funResultOnStart)

                if (funResultWithoutStepSize >= funResultOnStart && funResultOnStart <= funResultWithStepSize) {    // step 3.a
                    return interval

                } else if (funResultWithoutStepSize <= funResultOnStart && funResultOnStart >= funResultWithStepSize) {     // step 3.b
                    throw Exception("Interval can't be found, choose another xStart ($xStart) variable!")

                } else {    // step 4
                    var delta = 0.0
                    k += 1

                    if (funResultWithoutStepSize >= funResultOnStart && funResultOnStart >= funResultWithStepSize) {  // step 4.a
                        delta = stepSize
                        interval.xStart = xValues[0]

                        xValues.add(k, xStart + stepSize)
                    } else if (funResultWithoutStepSize <= funResultOnStart && funResultOnStart <= funResultWithStepSize) { // step 4.b
                        delta = -stepSize
                        interval.xEnd = xValues[0]

                        xValues.add(k, xStart - stepSize)
                    }

                    while (true) {
                        xValues.add(k + 1, xValues[k] + 2.0.pow(k) * delta)     // step 5

                        if (function(xValues[k + 1]) >= function(xValues[k])) {
                            if (delta > 0) {
                                interval.xEnd = xValues[k + 1]
                            } else if (delta < 0) {
                                interval.xStart = xValues[k + 1]
                            }
                        } else {
                            if (delta > 0) {
                                interval.xStart = xValues[k]
                            } else if (delta < 0) {
                                interval.xEnd = xValues[k]
                            }
                        }

                        if (function(xValues[k + 1]) >= function(xValues[k])) {
                            break
                        }
                        k += 1
                    }
                }
            }

            PrintUtils.printInfoEnd(k, timeExecution, interval)
            return interval
        }

        fun bisectionMethod(epsilon: Double, interval: Interval, function: (x: Double) -> Double): Double {
            PrintUtils.printInfoStart("Bisection method")

            var k = 0
            var xMiddle = interval.getCenter()

            val timeExecution = measureTimeMillis {
                do {
                    val xLeftMiddle = interval.xStart + interval.getLength() / 4
                    val xRightMiddle = interval.xEnd - interval.getLength() / 4

                    if (function(xLeftMiddle) < function(xMiddle)) {
                        interval.xEnd = xMiddle
                        xMiddle = xLeftMiddle
                    } else if (function(xRightMiddle) < function(xMiddle)) {
                        interval.xStart = xMiddle
                        xMiddle = xRightMiddle
                    } else {
                        interval.xStart = xLeftMiddle
                        interval.xEnd = xRightMiddle
                    }

                    k += 1
                } while (interval.getLength() > epsilon)
            }

            PrintUtils.printInfoEndFunction(k, timeExecution, xMiddle, function)
            return xMiddle
        }

        fun goldenSectionMethod(epsilon: Double, interval: Interval, function: (x: Double) -> Double): Double {
            PrintUtils.printInfoStart("Golden Section method")

            var k = 0
            val timeExecution = measureTimeMillis {

                val phi = (1 + Math.sqrt(5.0)) / 2

                while (interval.getLength() > epsilon) {
                    val z = interval.xEnd - (interval.xEnd - interval.xStart) / phi
                    val y = interval.xStart + (interval.xEnd - interval.xStart) / phi
                    if (function(y) <= function(z)) {
                        interval.xStart = z
                    } else {
                        interval.xEnd = y
                    }

                    k += 1
                }
            }

            PrintUtils.printInfoEndFunction(k, timeExecution, interval.getCenter(), function)
            return interval.getCenter()
        }

        fun fibonacciMethod(eps: Double, interval: Interval, function: (x: Double) -> Double): Double {
            PrintUtils.printInfoStart("Fibonacci method")

            var k = 0
            val timeExecution = measureTimeMillis {

                var y: Double
                var z: Double
                var N = 3
                val fibArr = arrayListOf(1.0, 1.0, 2.0, 3.0)
                var f1 = 2.0
                var f2 = 3.0
                while (fibArr[fibArr.size - 1] < interval.getLength() / eps) {
                    fibArr.add(f1 + f2)
                    f1 = f2
                    f2 = fibArr[fibArr.size - 1]
                    ++N
                }
                for (i in 1 until N - 3) {
                    y = interval.xStart + fibArr[N - i - 1] / fibArr[N - i + 1] * (interval.xEnd - interval.xStart)
                    z = interval.xStart + fibArr[N - i] / fibArr[N - i + 1] * (interval.xEnd - interval.xStart)
                    if (function(y) <= function(z))
                        interval.xEnd = z
                    else
                        interval.xStart = y

                    k += 1
                }
            }

            PrintUtils.printInfoEndFunction(k, timeExecution, interval.getCenter(), function)
            return interval.getCenter()
        }


        fun powellMethod(eps: Float, delta: Float, xStart: Float, stepSize: Float, function: (x: Float) -> Float): Float {
            PrintUtils.printInfoStart("Powell method")

            var a1 = xStart
            var k = 0

            while (true) {
                // Step 2
                var a2 = a1 + stepSize

                // Step 3
                var f1 = function(a1)
                var f2 = function(a2)

                // Step 4
                var a3 = if (f1 > f2) {
                    a1 + 2 * stepSize
                } else {
                    a1 - 2 * stepSize
                }

                while (true) {
                    k += 1

                    // Step 5
                    var f3 = function(a3)

                    // Step 6
                    var fMin = min(f1, min(f2, f3))
                    var aMin = when (fMin) {
                        f1 -> a1
                        f2 -> a2
                        f3 -> a3
                        else -> throw Exception("Cannot find fMin")
                    }

                    // Step 7
                    var det = 2 * ((a2 - a3) * f1 + (a3 - a1) * f2 + (a1 - a2) *f3)
                    if (det == 0f) {
//                        if ((a1 - a2).absoluteValue < delta && (a2 - a3).absoluteValue < delta) {
//                            PrintUtils.printInfoEndFunction(k, 0, a1, function)
//                            return a1;
//                        }

                        a1 = aMin;
                    } else {
                        var a = ((a2.pow(2) - a3.pow(2)) * f1 + (a3.pow(2) - a1.pow(2)) * f2 + (a1.pow(2) - a2.pow(2)) * f3 ) / det;

                        // Step 8
                        var aBool = ((fMin - function(a)) / function(a)).absoluteValue < eps;
                        var bBool = ((aMin - a) / a).absoluteValue < delta;
                        if (aBool && bBool) { // а)
                            PrintUtils.printInfoEndFunction(k, 0, a1, function)
                            return a;
                        } else {
                            if (a in a1..a3) { // б)
                                if (a < a2) {
                                    a3 = a2;
                                    a2 = a;
                                } else {
                                    a1 = a2;
                                    a2 = a;
                                }
                            } else { // в)
//                                a1 = a3 + 2 * stepSize;
                                a1 = a
                                break;
                            }
                        }
                    }
                } // Step 5
            }
        }
    }

//    double powell(float a1, double h0, double delta, double eps, int &k) {
//        k = 0;
//        while (true) {
//            // Шаг 2
//            float a2 = a1 + h0;
//            // Шаг 3
//            double f1 = f(a1);
//            double f2 = f(a2);
//            // Шаг 4
//            float a3 =  a1 + 2 * h0;
//
//            if (f1 <= f2) {
//                a3 = a1 - 2 * h0;
//            }
//            while (true) {
//                // Расположить в естественном порядке
//                if (a3 < a1) {
//                    std::swap(a1, a3);
//                }
//                if (a2 < a1) {
//                    std::swap(a2, a1);
//                }
//                if (a3 < a2) {
//                    std::swap(a3, a2);
//                }
//                k++;
//                // Шаг 5
//                double f3 = f(a3);
//                f1 = f(a1);
//                f2 = f(a2);
//                // Шаг 6
//                double f_min = FLT_MAX;
//                double a_min = FLT_MAX;
//
//                if (f_min > f1) {
//                    f_min = f1;
//                    a_min = a1;
//                }
//
//                if (f_min > f2) {
//                    f_min = f2;
//                    a_min = a2;
//                }
//
//                if (f_min > f3) {
//                    f_min = f3;
//                    a_min = a3;
//                }
//                // Шаг 7
////            std::cout << "a1=" << a1 << ", a2=" << a2 << ", a3=" << a3 << ", a_min=" << a_min;
//                double det = 2 * ((a2 - a3) *f1 + (a3 - a1)*f2 + (a1-a2)*f3);
//                if (det == 0) {
//                    if (fabs(a1 - a2) < delta && fabs(a2 - a3) < delta) return a1;
//                    a1 = a_min;
////                std::cout << ", a1=a_min" << std::endl;
//                } else {
//                    double a = (  (a2 * a2 - a3 * a3) * f1
//                            + (a3 * a3 - a1 * a1) * f2
//                            + (a1 * a1 - a2 * a2) * f3 )
//                    / det;
////                std::cout << ", a=" << a << std::endl;
//                    // Шаг 8
//                    bool a_bool = fabs((f_min - f(a)) / f(a)) < eps;
//                    bool b_bool = fabs((a_min - a) / a) < delta;
//                    if (a_bool && b_bool) { // а)
//                        return a;
//                    } else {
//                        if (a >= a1 && a <= a3) { // б)
//                            if (a < a2) {
//                                a3 = a2;
//                                a2 = a;
//                            } else {
//                                a1 = a2;
//                                a2 = a;
//                            }
//                        } else { // в)
////                        std::cout << "a1=a" << std::endl;
////                        a1  = a;
//                            a1 = a3 + 2*h0;
//                            break;
//                        }
//                    }
//                }
//            } // Шаг 5
//        }
//    }
}