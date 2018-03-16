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

    }
}