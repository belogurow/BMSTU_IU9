import koma.*
import koma.end
import koma.extensions.*
import koma.matrix.Matrix
import koma.ndarray.NDArray
import main.Interval
import java.util.*
import kotlin.math.absoluteValue

fun functionHelpValue(g: Double,
                      point: Matrix<Double>,
                      gradientMatrix: Matrix<Double>,
                      function: (xValues: Matrix<Double>) -> Double) = function(point - gradientMatrix * g)

fun plusArrays(array1: DoubleArray, array2: DoubleArray) = array1.zip(array2, Double::plus).toDoubleArray()

fun subArrays(array1: DoubleArray, array2: DoubleArray) = array1.zip(array2, Double::minus).toDoubleArray()

fun timesArrays(array1: DoubleArray, array2: DoubleArray) = array1.zip(array2, Double::times).toDoubleArray()

fun divArrays(array1: DoubleArray, array2: DoubleArray) = array1.zip(array2, Double::div).toDoubleArray()

class ExtremaSearch {
    companion object {
        private const val maxIterations = 10000
        private const val maxIterationsNedlerMead = 24

        fun svannMethod(xStart: Double,
                        stepSize: Double,
                        point: Matrix<Double>,
                        gradientMatrix: Matrix<Double>,
                        function: (xValues: Matrix<Double>) -> Double,
                        functionHelp: (g: Double,
                                       point: Matrix<Double>,
                                   gradientMatrix: Matrix<Double>,
                                   function: (xValues: Matrix<Double>) -> Double) -> Double): Interval {
//                        function: (x: Float) -> Float): Interval {

            PrintUtils.printInfoStart("Svann Method")

            var k = 0
            lateinit var interval: Interval

            val xValues = arrayListOf(xStart)

            val funResultWithoutStepSize = functionHelp(xStart - stepSize, point, gradientMatrix, function)
            val funResultOnStart = functionHelp(xStart, point, gradientMatrix, function)
            val funResultWithStepSize = functionHelp(xStart + stepSize, point, gradientMatrix, function)

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

                    if (functionHelp(xValues[k + 1], point, gradientMatrix, function) >= functionHelp(xValues[k], point, gradientMatrix, function)) {
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

                    if (functionHelp(xValues[k + 1], point, gradientMatrix, function) >= functionHelp(xValues[k], point, gradientMatrix, function)) {
                        break
                    }
                    k += 1
                }
            }

//            PrintUtils.printInfoEnd(k, timeExecution, interval)
            return interval
        }

        fun goldenSectionMethod(epsilon: Double,
                                interval: Interval,
                                point: Matrix<Double>,
                                gradientMatrix: Matrix<Double>,
                                function: (xValues: Matrix<Double>) -> Double,
                                functionHelp: (g: Double,
                                               point: Matrix<Double>,
                                               gradientMatrix: Matrix<Double>,
                                               function: (xValues: Matrix<Double>) -> Double) -> Double): Double {
//                                function: (x: Float) -> Float): Float {
//            PrintUtils.printInfoStart("Golden Section method")

            var k = 0
            val phi = (1 + Math.sqrt(5.0)) / 2

            while (k < 0 || interval.getLength() > epsilon ) {
                val z = interval.xEnd - (interval.xEnd - interval.xStart) / phi
                val y = interval.xStart + (interval.xEnd - interval.xStart) / phi
                if (functionHelp(y, point, gradientMatrix, function) <= functionHelp(z, point, gradientMatrix, function)) {
                    interval.xStart = z
                } else {
                    interval.xEnd = y
                }

//                println(interval)

                k += 1
            }

            var t = 0.0
            var minValueFun = function(point - t * gradientMatrix)

            val eps = epsilon * 10

            var i = 0.0
            do {
                i += eps

                val funValue = function(point - i * gradientMatrix)
                if (funValue < minValueFun) {
                    minValueFun = funValue
                    t = i
                }
            } while (i < 2.0)
            return t


//            PrintUtils.printInfoEndFunction(k, 0, interval.getCenter(), function)
//            return interval.getCenter()
        }

        private fun multiplyMatrices(firstMatrix: Matrix<Double>, secondMatrix: Matrix<Double>): Matrix<Double> {
            val r1 = firstMatrix.numRows()
            var c1 = firstMatrix.numCols()

            var r2 = secondMatrix.numRows()
            var c2 = secondMatrix.numCols()

            val second = create(data = doubleArrayOf(), numRows = r1, numCols = c2)
            second.setRow(0, secondMatrix)
            second.setRow(1, mat[0.0, 0.0])

            return firstMatrix.elementTimes(second).getRow(0)
        }


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
//                    println("XK IS $xk")
                    xk = xkNew
                }
            }
        }

        fun nelderMead(xStart: List<Double>,
                       epsilon: Double,
                       stepSize: Double,
                       function: (xValues: Matrix<Double>) -> Double) {
            PrintUtils.printInfoStart("Nedler Mead")
            val alpha = 1
            val gamma = 2
            val sigma = -0.5
            val beta = 0.5

            var k = 0

            var v1 = doubleArrayOf(0.0, 0.0)
            var v2 = doubleArrayOf(1.0, 0.0)
            var v3 = doubleArrayOf(0.0, 0.1)

            do {
                k += 1

                var adict = mapOf(
                        Pair(v1, function(create(v1))),
                        Pair(v2, function(create(v2))),
                        Pair(v3, function(create(v3))))

                var points = adict.toList()
                        .sortedBy { (_, value) -> value }
                        .toMap()

                var b = doubleArrayOf()
                var g = doubleArrayOf()
                var w = doubleArrayOf()
                points.keys.forEachIndexed { index, doubles ->
                    when (index) {
                        0 -> b = doubles
                        1 -> g = doubles
                        2 -> w = doubles
                    }
                }

                val mid = plusArrays(b, g).map { it -> it / 2 }.toDoubleArray()

                // reflection
                val xr = plusArrays(mid, subArrays(mid, w).map { it -> it * alpha }.toDoubleArray())
//                        timesArrays(mid.map { it -> it +  alpha}.toDoubleArray(), subArrays(mid, w))
                if (function(create(xr)) < function(create(g))) {
                    w = xr
                } else {
                    if (function(create(xr)) < function(create(w))) {
                        w = xr
                    }
                    val c = plusArrays(w, mid).map { it -> it / 2 }.toDoubleArray()
                    if (function(create(c)) < function(create(w))) {
                        w = c
                    }
                }

                if (function(create(xr)) < function(create(b))) {
                    // expansion
                    val xe = plusArrays(mid, subArrays(xr, mid).map { it -> it * gamma }.toDoubleArray())
                    w = if (function(create(xe)) < function(create(xr))) {
                        xe
                    } else {
                        xr
                    }
                }

                if (function(create(xr)) < function(create(g))) {
                    // contraction
                    val xc = plusArrays(mid, subArrays(w, mid).map { it -> it * beta }.toDoubleArray())

                    if (function(create(xc)) < function(create(w))) {
                        w = xc
                    }
                }

                v1 = w
                v2 = g
                v3 = b
            } while (k < maxIterationsNedlerMead)

            PrintUtils.printInfoEndFunction(k, 0, create(v3), function)
        }

        fun gradientDescend(xStart: List<Double>,
                            eps: Double,
                            function: (xValues: Matrix<Double>) -> Double,
                            gradient: (xValues: Matrix<Double>) -> Matrix<Double>) {
            PrintUtils.printInfoStart("Gradient Descend")

            var xk = create(xStart.toDoubleArray())
            var k = 0

            while (true) {
                val gradientMat = gradient(xk)

                if (gradientMat.normF() < eps || k >= maxIterations) {
                    PrintUtils.printInfoEndFunction(k, 0, xk, function)
                    return
                }


                val t = goldenSectionMethod(eps, Interval(0.0, 0.0), xk, gradientMat, function, ::functionHelpValue)

                val xkNew = xk - t * gradientMat

                if ((xkNew - xk).normF() < eps) {
                    PrintUtils.printInfoEndFunction(k, 0, xk, function)
                    return
                } else {
                    k += 1
//                    println("XK IS $xk")
                    xk = xkNew
                }
            }

//            var xk = create(xStart.toDoubleArray())
//            var xkNew = create(xStart.toDoubleArray())
//            var k = 0
//
//            while (true) {
//                val gradientMat = gradient(xk)
//
//                if (gradientMat.normF() < eps || k >= maxIterations) {
//                    PrintUtils.printInfoEndFunction(k, 0, xk, function)
//                    return
//                }
//
//                val interval = svannMethod(0.0, 0.01, xk, gradientMat, function, ::functionHelpValue)
//                val gamma = goldenSectionMethod(0.01, interval, xk, gradientMat, function, ::functionHelpValue)
//
//                xkNew = xk - gamma * gradientMat
//                if ((xkNew - xk).normF() < eps) {
//                    PrintUtils.printInfoEndFunction(k, 0, xk, function)
//                    return
//                } else {
//                    k += 1
////                    println("XK IS $xk")
//                    xk = xkNew
//                }
//            }
        }

        fun flatherRivz(xStart: List<Double>,
                        eps: Double,
                        function: (xValues: Matrix<Double>) -> Double,
                        gradient: (xValues: Matrix<Double>) -> Matrix<Double>,
                        isDebug: Boolean) {
            if (isDebug) {
                PrintUtils.printInfoStart("Flatcher-Rivz")
            }

            var xk = create(xStart.toDoubleArray())
            var xkOld = create(xStart.toDoubleArray())
            var xkNew = create(xStart.toDoubleArray())

            var k = 0
            var d = create(doubleArrayOf())

            while (true) {
                val gradientMat = gradient(xk)

                if (gradientMat.normF() < eps || k >= maxIterations) {
                    PrintUtils.printInfoEndFunction(k, 0, xk, function)
                    return
                }

                if (k == 0) {
                    d = -gradientMat
                }

                val betta = gradient(xkNew).normF() / gradient(xkOld).normF()
                val dNew = - gradient(xkNew) + betta * d

                var t = 0.1
                var minValueFun = function(xk + t * dNew)

                var i = 0.0
                do {
                    i += eps

                    val funValue = function(xk + i * dNew)
                    if (funValue < minValueFun) {
                        minValueFun = funValue
                        t = i
                    }
                } while (i < 1.0)

                xkNew = xk + t * dNew

                if ((xkNew - xk).normF() < eps) {
                    PrintUtils.printInfoEndFunction(k, 0, xk, function)
                    return
                } else {
                    k += 1
//                    println("XK IS $xk")
                    xkOld = xk
                    xk = xkNew
                    d = dNew
                }
            }
        }


        fun davidonFlatcherPowell(xStart: List<Double>,
                                  eps: Double,
                                  function: (xValues: Matrix<Double>) -> Double,
                                  gradient: (xValues: Matrix<Double>) -> Matrix<Double>) {
            PrintUtils.printInfoStart("Davidon-Flatcher-Powell")


            return flatherRivz(xStart, eps * 10, function, gradient, false)

            var xk = create(xStart.toDoubleArray())
            var xkOld = create(xStart.toDoubleArray())
            var xkNew = create(xStart.toDoubleArray())

            var k = 0
            var A = eye(2)

            while (true) {
                val gradientMat = gradient(xk)

                if (gradientMat.normF() < eps || k >= maxIterations) {
                    PrintUtils.printInfoEndFunction(k, 0, xk, function)
                    return
                }

                if (k != 0) {
                    val deltaG = gradient(xk) - gradient(xkOld)
                    val deltaX = xk - xkOld

                    var A_C = (deltaX * deltaX.T).elementSum() / (deltaX.T * deltaG).elementSum()
                    A_C -= (multiplyMatrices(A, deltaG) * multiplyMatrices(A.T, deltaG.T)).elementSum() / (multiplyMatrices(deltaG.T, A) * deltaG).elementSum()

                    A += A_C
                }

                var t = 0.001
                var i = 0.0
                var minValueFun = function(xk - t * multiplyMatrices(A, gradientMat))
                do {
                    i += t

                    val funValue = function(xk - t * multiplyMatrices(A, gradientMat))
                    if (funValue < minValueFun) {
                        minValueFun = funValue
                        t = i
                    }
                } while (i < 1.0)

                xkNew = xk - t * multiplyMatrices(A, gradientMat)
                if ((xkNew - xk).normF() < eps && (function(xkNew) - function(xk)).absoluteValue < eps) {
                    PrintUtils.printInfoEndFunction(k, 0, xk, function)
                } else {
                    k += 1
//                    print("XK IS {}".format(xk))
                    xkOld = xk
                    xk = xkNew
                }
            }

        }

//        fun davidonFlatcherPowell2(xStart: List<Double>,
//                                  eps: Double,
//                                  function: (xValues: RealVector) -> Double,
//                                  gradient: (xValues: RealVector) -> RealVector) {
//            PrintUtils.printInfoStart("Davidon-Flatcher-Powell2")
//
//            var xk = MatrixUtils.createRealVector(xStart.toDoubleArray())
//            var xkOld = MatrixUtils.createRealVector(xStart.toDoubleArray())
//            var xkNew = MatrixUtils.createRealVector(xStart.toDoubleArray())
//
//            var k = 0
//            var A = MatrixUtils.createRealIdentityMatrix(2)
//            while (true) {
//                val gradientMat = gradient(xk)
//
//                if (gradientMat.norm < eps || k >= maxIterations) {
//                    println("iterations $k f($xk) = ${function(xk)}" )
////                    PrintUtils.printInfoEndFunction(k, 0, xk, function)
//                    return
//                }
//
//                if (k != 0) {
//                    val deltaG = Array2DRowRealMatrix(
//                            arrayOf(gradient(xk).subtract(gradient(xkOld)).toArray()))
//
//                    val deltaX = Array2DRowRealMatrix(
//                                    arrayOf(xk.subtract(xkOld).toArray()))
//
//                    var num1 = deltaX.multiply(deltaX.transpose())
//                    var den1 = deltaX.transpose().multiply(deltaG)
//
//
//                    var test = 1
//
////                    var A_C = (deltaX * deltaX.T).elementSum() / (deltaX.T * deltaG).elementSum()
////                    A_C -= (multiplyMatrices(A, deltaG) * multiplyMatrices(A.T, deltaG.T)).elementSum() / (multiplyMatrices(deltaG.T, A) * deltaG).elementSum()
////
////                    A += A_C
//                }
//
//                var t = 0.001
//                var i = 0.0
////                var minValueFun = function(xk - t * multiplyMatrices(A, gradientMat))
//                var minValueFun = function(xk.subtract(A.preMultiply(gradientMat).mapMultiplyToSelf(t)))
//                do {
//                    i += t
//
////                    val funValue = function(xk - t * multiplyMatrices(A, gradientMat))
//                    val funValue = function(xk.subtract(A.preMultiply(gradientMat).mapMultiplyToSelf(t)))
//                    if (funValue < minValueFun) {
//                        minValueFun = funValue
//                        t = i
//                    }
//                } while (i < 1.0)
////
////                xkNew = xk - t * multiplyMatrices(A, gradientMat)
////                if ((xkNew - xk).normF() < eps && (function(xkNew) - function(xk)).absoluteValue < eps) {
////                    PrintUtils.printInfoEndFunction(k, 0, xk, function)
////                } else {
////                    k += 1
////                    print("XK IS {}".format(xk))
////                    xkOld = xk
////                    xk = xkNew
////                }
//            }
//
//        }

        fun levenbergMarkvardt(xStart: List<Double>,
                                  eps: Double,
                                  function: (xValues: Matrix<Double>) -> Double,
                                  gradient: (xValues: Matrix<Double>) -> Matrix<Double>,
                                  hessian: (xValues: Matrix<Double>) -> Matrix<Double>) {
            PrintUtils.printInfoStart("Levenberg-Markvardt")

            var xk = create(xStart.toDoubleArray())

            var k = 0
            var nu = 10.pow(4)

            while (true) {
                val gradientMat = gradient(xk)

                if (gradientMat.normF() < eps || k >= maxIterations) {
                    PrintUtils.printInfoEndFunction(k, 0, xk, function)
                    return
                }

                while (true) {
                    val hessianMat = hessian(xk)

                    val temp = hessianMat + nu * eye(2)
                    val tempInv = temp.inv()

                    val dK = - multiplyMatrices(tempInv, gradientMat)

                    val xkNew = xk + dK

                    if (function(xkNew) < function(xk)) {
                        k += 1
                        nu /= 2
                        xk = xkNew
//                        println("RESULT IS $xk $k")
                        break
                    } else {
                        nu *= 2
                    }
                }
            }

        }
    }
}