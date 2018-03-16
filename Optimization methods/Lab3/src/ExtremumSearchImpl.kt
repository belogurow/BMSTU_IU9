import kotlin.collections.ArrayList
import kotlin.math.absoluteValue
import kotlin.math.pow

@Deprecated("Use ExtremaSearch.kt")
class ExtremumSearchImpl {

    companion object : ExtremumSearch {

        override fun svannMethod(xStart: Double, stepSize: Double, function: (x: Double) -> Double): ArrayList<Double> {
            println("Start Svann method:")

            var k = 0
            val xValues = arrayListOf(xStart)

            val funResultWithoutStepSize = function(xStart - stepSize)
            val funResultOnStart = function(xStart)
            val funResultWithStepSize = function(xStart + stepSize)

            var rangeA = funResultWithoutStepSize
            var rangeB = funResultWithStepSize

            if (funResultWithoutStepSize >= funResultOnStart && funResultOnStart <= funResultWithStepSize) {    // step 3.a
                return arrayListOf(rangeA, rangeB)

            } else if (funResultWithoutStepSize <= funResultOnStart && funResultOnStart >= funResultWithStepSize) {     // step 3.b
                throw Exception("Interval can't be found, choose another xStart ($xStart) variable!")

            } else {    // step 4
                var delta = 0.0
                k += 1

                if (funResultWithoutStepSize >= funResultOnStart && funResultOnStart >= funResultWithStepSize) {  // step 4.a
                    delta = stepSize
                    rangeA = xValues[0]

                    xValues.add(k, xStart + stepSize)
                } else if (funResultWithoutStepSize <= funResultOnStart && funResultOnStart <= funResultWithStepSize) { // step 4.b
                    delta = -stepSize
                    rangeB = xValues[0]

                    xValues.add(k, xStart - stepSize)
                }

                while (true) {
                    xValues.add(k + 1, xValues[k] + 2.0.pow(k) * delta)     // step 5

                    if (function(xValues[k + 1]) >= function(xValues[k])) {
                        if (delta > 0) {
                            rangeB = xValues[k + 1]
                        } else if (delta < 0) {
                            rangeA = xValues[k + 1]
                        }
                    } else {
                        if (delta > 0) {
                            rangeA = xValues[k]
                        } else if (delta < 0) {
                            rangeB = xValues[k]
                        }
                    }

                    if (function(xValues[k + 1]) >= function(xValues[k])) {
                        break
                    }
                    k += 1
                }

                println("\tIteration(s): $k, result: ${arrayListOf(rangeA, rangeB)} \n")
                return arrayListOf(rangeA, rangeB)
            }
        }

        override fun bisectionMethod(epsilon: Double, xRange: MutableList<Double>, function: (x: Double) -> Double): Double {
            println("Start Bisection method:")

            var k = 0;
            var xMiddle = (xRange[0] + xRange[1]) / 2

            do {
                val xLeftMiddle = xRange[0] + rangeLength(xRange) / 4
                val xRightMiddle = xRange[1] - rangeLength(xRange) / 4

                if (function(xLeftMiddle) < function(xMiddle)) {
                    xRange[1] = xMiddle
                    xMiddle = xLeftMiddle
                } else if (function(xRightMiddle) < function(xMiddle)) {
                    xRange[0] = xMiddle
                    xMiddle = xRightMiddle
                } else {
                    xRange[0] = xLeftMiddle
                    xRange[1] = xRightMiddle
                }

                k += 1
            } while (rangeLength(xRange) > epsilon)

            println("\tIteration(s): $k, result: f($xMiddle) = ${function(xMiddle)} \n")
            return xMiddle
        }

        private fun rangeLength(xRange: List<Double>) = (xRange[1] - xRange[0]).absoluteValue

        override fun goldenSectionMethod(epsilon: Double, xRange: MutableList<Double>, function: (x: Double) -> Double): Double {
            println("Start Golden Section method:")

            var k = 0
            val phi = (1 + Math.sqrt(5.0)) / 2

            while (rangeLength(xRange) > epsilon) {
                val z = xRange[1] - (xRange[1] - xRange[0]) / phi
                val y = xRange[0] + (xRange[1] - xRange[0]) / phi
                if (function(y) <= function(z)) {
                    xRange[0] = z
                } else {
                    xRange[1] = y
                }

                k += 1
            }

            val result = (xRange[1] + xRange[0]) / 2

            println("\tIteration(s): $k, result: f($result) = ${function(result)} \n")
            return result
        }
    }



}
