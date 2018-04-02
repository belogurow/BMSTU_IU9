import kotlin.math.absoluteValue
import kotlin.math.min
import kotlin.math.pow
import kotlin.system.measureTimeMillis


class ExtremaSearch {
    companion object {
        fun svannMethod(xStart: Float, stepSize: Float, function: (x: Float) -> Float): Interval {
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
                    var delta = 0.0f
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
                        xValues.add(k + 1, (xValues[k] + 2.0.pow(k) * delta).toFloat())     // step 5

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

        fun bisectionMethod(epsilon: Float, interval: Interval, function: (x: Float) -> Float): Float {
            PrintUtils.printInfoStart("Bisection method")

            var k = 0
            var xMiddle = interval.getCenter()

            val timeExecution = measureTimeMillis {
                do {
                    val xLeftMiddle = interval.xStart + interval.getLength() / 4
                    val xRightMiddle = interval.xEnd - interval.getLength() / 4

                    when {
                        function(xLeftMiddle) < function(xMiddle) -> {
                            interval.xEnd = xMiddle
                            xMiddle = xLeftMiddle
                        }
                        function(xRightMiddle) < function(xMiddle) -> {
                            interval.xStart = xMiddle
                            xMiddle = xRightMiddle
                        }
                        else -> {
                            interval.xStart = xLeftMiddle
                            interval.xEnd = xRightMiddle
                        }
                    }

                    k += 1
                } while (interval.getLength() > epsilon)
            }

            PrintUtils.printInfoEndFunction(k, timeExecution, xMiddle, function)
            return xMiddle
        }

        fun goldenSectionMethod(epsilon: Float, interval: Interval, function: (x: Float) -> Float): Float {
            PrintUtils.printInfoStart("Golden Section method")

            var k = 0
            val timeExecution = measureTimeMillis {

                val phi = (1 + Math.sqrt(5.0)) / 2

                while (interval.getLength() > epsilon) {
                    val z = (interval.xEnd - (interval.xEnd - interval.xStart) / phi).toFloat()
                    val y = (interval.xStart + (interval.xEnd - interval.xStart) / phi).toFloat()
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

        fun fibonacciMethod(eps: Float, interval: Interval, function: (x: Float) -> Float): Float {
            PrintUtils.printInfoStart("Fibonacci method")

            var k = 0
            val timeExecution = measureTimeMillis {

                var y: Float
                var z: Float
                var n = 3
                val fibArr = arrayListOf(1.0, 1.0, 2.0, 3.0)
                var f1 = 2.0
                var f2 = 3.0
                while (fibArr[fibArr.size - 1] < interval.getLength() / eps) {
                    fibArr.add(f1 + f2)
                    f1 = f2
                    f2 = fibArr[fibArr.size - 1]
                    ++n
                }
                for (i in 1 until n - 3) {
                    y = (interval.xStart + fibArr[n - i - 1] / fibArr[n - i + 1] * (interval.xEnd - interval.xStart)).toFloat()
                    z = (interval.xStart + fibArr[n - i] / fibArr[n - i + 1] * (interval.xEnd - interval.xStart)).toFloat()
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


        fun quadraticInterpolation(eps: Float, delta: Float, xStart: Float, stepSize: Float, function: (x: Float) -> Float): Float {
            PrintUtils.printInfoStart("Powell method")

            var a1 = xStart
            var k = 0

            while (true) {
                // Step 2
                var a2 = a1 + stepSize

                // Step 3
                val f1 = function(a1)
                val f2 = function(a2)

                // Step 4
                var a3 = if (f1 > f2) {
                    a1 + 2 * stepSize
                } else {
                    a1 - 2 * stepSize
                }

                while (true) {
                    k += 1

                    // Step 5
                    val f3 = function(a3)

                    // Step 6
                    val fMin = min(f1, min(f2, f3))
                    val aMin = when (fMin) {
                        f1 -> a1
                        f2 -> a2
                        f3 -> a3
                        else -> throw Exception("Cannot find fMin")
                    }

                    // Step 7
                    val det = 2 * ((a2 - a3) * f1 + (a3 - a1) * f2 + (a1 - a2) * f3)
                    if (det == 0f) {
                        a1 = aMin
                    } else {
                        val a = ((a2.pow(2) - a3.pow(2)) * f1 + (a3.pow(2) - a1.pow(2)) * f2 + (a1.pow(2) - a2.pow(2)) * f3) / det

                        // Step 8
                        if (((fMin - function(a)) / function(a)).absoluteValue < eps && ((aMin - a) / a).absoluteValue < delta) { // a)
                            PrintUtils.printInfoEndFunction(k, 0, a1, function)
                            return a
                        } else {
                            if (a in a1..a3) { // b)
                                if (a < a2) {
                                    a3 = a2
                                    a2 = a
                                } else {
                                    a1 = a2
                                    a2 = a
                                }
                            } else { // c)
                                a1 = a
                                break
                            }
                        }
                    }
                }
            }
        }
    }
}