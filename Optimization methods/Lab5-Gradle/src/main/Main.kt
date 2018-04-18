import com.sun.xml.internal.ws.model.ExternalMetadataReader
import koma.*
import koma.extensions.get
import koma.matrix.Matrix
import main.ExtremaSearch
import main.ExtremaSeerch
import kotlin.math.pow

/**
 * F(x) = 50 * (x_0^2 - x_1)^2 + 2 * (x_0 - 1)^2 + 10
 * min(F(x)) = 10  at  (x_0, x_1) = (1, 1)
 *
 * @see https://www.wolframalpha.com/input/?i=extrema+50+*+(x_0%5E2+-+x_1)%5E2+%2B+2+*+(x_0+-+1)%5E2+%2B+10
 */
private fun myFunction(xValues: Matrix<Double>): Double {
    val a = 50
    val b = 2
    val f0 = 10
    val n = 1

    var sum = 0.0

    for (i in 0 until n) {
        sum += a * (xValues[i].pow(2) - xValues[i+1]).pow(2) + b * (xValues[i] - 1).pow(2)
    }

    return sum + f0
}

private fun derivFunction(xValues: Matrix<Double>): List<Double> {
    val x0 = 4 * (50 * xValues[0].pow(3) - 50 * xValues[0] * xValues[1] + xValues[0] - 1)
    val x1 = -100 * (xValues[0].pow(2) - xValues[1])

    return listOf(x0, x1)
}

private fun condFunction1(xValues: Matrix<Double>) = xValues[0].pow(2) + xValues[1].pow(2) - 1.0
private fun condFunction2(xValues: Matrix<Double>) = - xValues[0]
private fun condFunction3(xValues: Matrix<Double>) = - xValues[1]

private fun condFunctions(xValues: Matrix<Double>) = listOf(
        condFunction1(xValues),
        condFunction2(xValues),
        condFunction3(xValues))

private fun derivCondFunction1(xValues: Matrix<Double>) = listOf(2 * xValues[0], 2 * xValues[1])
private fun derivCondFunction2(xValues: Matrix<Double>) = listOf(-1.0, 0.0)
private fun derivCondFunction3(xValues: Matrix<Double>) = listOf(0.0, -1.0)

private fun derivFunctions(xValues: Matrix<Double>) = listOf(
        derivCondFunction1(xValues),
        derivCondFunction2(xValues),
        derivCondFunction3(xValues))


/**
 * @see http://www.wolframalpha.com/input/?i=diff(50*(x%5E2-y)%5E2%2B2*(x-1)%5E2%2B10,y)
 */
private fun gradient(xValues: Matrix<Double>) =
        create(doubleArrayOf((4 * (50 * xValues[0].pow(3) - 50 * xValues[0] * xValues[1] + xValues[0] - 1)), (-100 * (xValues[0].pow(2) - xValues[1]))))


/**
 * @see http://www.wolframalpha.com/input/?i=hessian+matrix+50+*+(x_0%5E2+-+x_1)%5E2+%2B+2+*+(x_0+-+1)%5E2+%2B+10
 */
private fun hessian(xValues: Matrix<Double>) = create(arrayOf(
        doubleArrayOf(400 * xValues[0].pow(2) + 200 * (xValues[0].pow(2) - xValues[1]) + 4, -200 * xValues[0]),
        doubleArrayOf(-200 * xValues[0], 100.0)))

fun main(args: Array<String>) {
    val xStart = listOf(0.0, 0.0)
    val epsilon = 0.0001
    val lambda = 1.0
    val betta = 1.5
    val curFunction = ::myFunction
    val curCondFunctions = ::condFunctions
    val gradientFunction = ::gradient
    val hessianFunction = ::hessian

    ExtremaSearch.penaltyFunction(xStart, epsilon, 1.0, 0.0, curFunction, gradientFunction, curCondFunctions)
    ExtremaSeerch.barrierFunction(xStart, epsilon, 1.0, 1.0, curFunction, gradientFunction, curCondFunctions)
    ExtremaSearch.lagrangeFunctions(xStart, epsilon*9, 1.0, 1.0, curFunction, gradientFunction, curCondFunctions)
    ExtremaSeerch.gradientProjections(xStart, epsilon, curFunction, ::derivFunction, curCondFunctions, ::derivFunctions)

}