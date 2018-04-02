import kotlin.math.absoluteValue
import kotlin.math.pow

// F(x) = 12x^2-2x+ abs(x-3) = 0, x_0 = -3
private fun myFunction(x: Double) = 12 * x.pow(2) - 2 * x + (x - 3).absoluteValue
private fun myFunction2(x: Float) = 12 * x.pow(2) - 2 * x + (x - 3).absoluteValue

// F'(x)
private fun myDFunction(x: Double) = (x - 3) / (x - 3).absoluteValue + 24 * x - 2

//private fun testFunction(x: Double) = 5 * x.pow(4) - 2 * x.pow(2) + (x + 1).absoluteValue
private fun testFunction(x: Double) = 5*x.pow(6)-36*x.pow(5)-165/2*x.pow(4)-60*x.pow(3)+36;

fun main(args: Array<String>) {
    val xStart = -3.0
    var stepSize = 1.0
    val epsilon = 0.01
    var delta = 0.01
    val curFunction = ::myFunction

    val interval = ExtremaSearch.svannMethod(xStart, stepSize, curFunction)

    ExtremaSearch.bisectionMethod(epsilon, interval.copy(), curFunction)
    ExtremaSearch.goldenSectionMethod(epsilon, interval.copy(), curFunction)
    ExtremaSearch.fibonacciMethod(epsilon, interval.copy(), curFunction)

    stepSize = 0.01
    ExtremaSearch.powellMethod(epsilon.toFloat(), delta.toFloat(), xStart.toFloat(), stepSize.toFloat(), ::myFunction2)
}
