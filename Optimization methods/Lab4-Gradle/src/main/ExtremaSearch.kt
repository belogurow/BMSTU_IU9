import koma.*
import koma.extensions.*
import koma.matrix.Matrix

class ExtremaSearch {
    companion object {
        private const val maxIterations = 10000


        fun hookeJeeves(xStart: List<Double>,
                        eps: Double,
                        function: (xValues: Matrix<Double>) -> Double,
                        gradient: (xValues: Matrix<Double>) -> Matrix<Double>) {
            PrintUtils.printInfoStart("Hooke Jeeves")

            var xk = create(xStart.toDoubleArray())
            var k = 0

            while (true) {
                val gradientMat = gradient(xk)

                if (gradientMat.normF() < eps || k >= maxIterations) {
                    PrintUtils.printInfoEndFunction(k, 0, xk, function)
                    return
                }

                var t = 0.0
                var minValueFun = function(xk - t * gradientMat)

                var i = 0.0
                do {
                    i += eps

                    val funValue = function(xk - i * gradientMat)
                    if (funValue < minValueFun) {
                        minValueFun = funValue
                        t = i
                    }
                } while (i < 2.0)

                val xkNew = xk - t * gradientMat

                if ((xkNew - xk).normF() < eps) {
                    PrintUtils.printInfoEndFunction(k, 0, xk, function)
                    return
                } else {
                    k += 1
                    println("XK IS $xk")
                    xk = xkNew
                }
            }

        }

    }
}