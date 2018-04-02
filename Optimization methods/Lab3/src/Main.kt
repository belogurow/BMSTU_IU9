import kotlin.math.absoluteValue
import kotlin.math.pow

// F(x) = 12x^2-2x+ abs(x-3) = 0, x_0 = -3
private fun myFunction(x: Float) = 12 * x.pow(2) - 2 * x + (x - 3).absoluteValue

// F'(x)
private fun myDerivativeFunction(x: Float) = (x - 3) / (x - 3).absoluteValue + 24 * x - 2


fun main(args: Array<String>) {
    val xStart = -3f
    var stepSize = 1f
    val epsilon = 0.01f
    val delta = 0.01f
    val curFunction = ::myFunction
    val curDerivativeFunction = ::myDerivativeFunction

    val interval = ExtremaSearch.svannMethod(xStart, stepSize, curFunction)

    ExtremaSearch.bisectionMethod(epsilon, interval.copy(), curFunction)
    ExtremaSearch.goldenSectionMethod(epsilon, interval.copy(), curFunction)
    ExtremaSearch.fibonacciMethod(epsilon, interval.copy(), curFunction)

    stepSize = 0.01f
    ExtremaSearch.quadraticInterpolation(epsilon, delta, xStart, stepSize, curFunction)
    ExtremaSearch.cubicInterpolation(epsilon, delta, xStart, stepSize, curFunction, curDerivativeFunction)
}
