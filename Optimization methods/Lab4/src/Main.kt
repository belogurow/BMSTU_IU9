import kotlin.math.pow

/**
 * F(x) = 50 * (x_0^2 - x_1)^2 + 2 * (x_0 - 1)^2 + 10
 * min(F(x)) = 10  at  (x_0, x_1) = (1, 1)
 *
 * @see https://www.wolframalpha.com/input/?i=extrema+50+*+(x_0%5E2+-+x_1)%5E2+%2B+2+*+(x_0+-+1)%5E2+%2B+10
 */
private fun myFunction(xValues: List<Float>): Float {
    val a = 50
    val b = 2
    val f0 = 10
    val n = 1

    var sum = 0f

    for (i in 0 until n) {
        sum += a * (xValues[i].pow(2) - xValues[i+1]).pow(2) + b * (xValues[i] - 1).pow(2)
    }

    return sum + f0
}

/**
 * @see http://www.wolframalpha.com/input/?i=diff(50*(x%5E2-y)%5E2%2B2*(x-1)%5E2%2B10,y)
 */
private fun gradient(xValues: List<Float>) = listOf(4 * (50 * xValues[0].pow(3) - 50 * xValues[0] * xValues[1] + xValues[0] - 1), -100 * (xValues[0].pow(2) - xValues[1]))

fun main(args: Array<String>) {
    val xStart = listOf(0f, 0f)
    val epsilon = 0.1f
    val delta = listOf(1f, 1f)
    val lambda = 1f
    val betta = 1.5f
    val curFunction = ::myFunction
    val gradientFunction = ::gradient

    ExtremaSearch.hookeJeeves2(xStart, epsilon, delta, lambda, betta, curFunction, gradientFunction)

//    val xValues = listOf(0f, 1f)
//    print(myFunction(xValues))
}