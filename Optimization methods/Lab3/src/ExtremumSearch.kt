@Deprecated("Use ExtremaSearch class")
interface ExtremumSearch {

    fun svannMethod(xStart: Double, stepSize: Double, function: (x: Double) -> Double): ArrayList<Double>

    fun bisectionMethod(epsilon: Double, xRange: MutableList<Double>, function: (x: Double) -> Double): Double

    fun goldenSectionMethod(epsilon: Double, xRange: MutableList<Double>, function: (x: Double) -> Double): Double
}