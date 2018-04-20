package main

import koma.*
import koma.extensions.set
import koma.extensions.times
import koma.matrix.Matrix
import kotlin.math.absoluteValue

class ExtremaSearch {
    companion object {
        const val maxIterations = 10000

        fun bisectionMethod(epsilon: Double,
                            interval: Interval,
                            function: (x: Double) -> Double): Double {
            PrintUtils.printInfoStart("Bisection method")

            var k = 0
            var xMiddle = interval.getCenter()

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

//            PrintUtils.printInfoEndFunction(k, 0, xMiddle, function)
            return xMiddle
        }

        fun hookeJeeves(xStart: List<Double>,
                        eps: Double,
                        function: (xValues: Matrix<Double>) -> Double,
                        gradient: (xValues: Matrix<Double>) -> Matrix<Double>): Matrix<Double> {
//            PrintUtils.printInfoStart("Hooke Jeeves")

            var xk = create(xStart.toDoubleArray())
            var k = 0

            while (true) {
                val gradientMat = gradient(xk)

                if (k >= maxIterations || gradientMat.normF() < eps) {
//                    PrintUtils.printInfoEndFunction(k, 0, xk, function)
                    return xk
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
//                    PrintUtils.printInfoEndFunction(k, 0, xk, function)
                    return xk
                } else {
                    k += 1
//                    println("XK IS $xk")
                    xk = xkNew
                }
            }
        }

        fun maxCondFunctions(index: Int,
                             xValues: Matrix<Double>,
                             condFunctions: (xValues: Matrix<Double>) -> List<Double>): Double {
            return max(0.0, condFunctions(xValues)[index])
        }

        fun penaltyFunction(xStart: List<Double>,
                            eps: Double,
                            penaltyCoef: Double,
                            increaseParam: Double,
                            function: (xValues: Matrix<Double>) -> Double,
                            gradient: (xValues: Matrix<Double>) -> Matrix<Double>,
                            condFunctions: (xValues: Matrix<Double>) -> List<Double>): Matrix<Double> {
            PrintUtils.printInfoStart("Penalty Function")

            var xPoint = create(xStart.toDoubleArray())
            var k = 0
            var penaltyValue = 0.0
            var currentPenaltyCoef = penaltyCoef

            do {
                fun penaltyFun(xValues: Matrix<Double>): Double {
                    var functionValue = 0.0
                    for (i in 0 until 3) {
                        functionValue += currentPenaltyCoef / 2.0 * maxCondFunctions(i, xValues, condFunctions).pow(2)
                    }
                    return functionValue
                }

                fun penaltyFun2(xValues: Matrix<Double>): Double {
                    var functionValue = function(xValues)
                    for (i in 0 until 3) {
                        functionValue += currentPenaltyCoef / 2.0 * maxCondFunctions(i, xValues, condFunctions).pow(2)
                    }
                    return functionValue
                }

                val directionsStep = listOf(eps, eps)
                val minimum = hookeJeeves(directionsStep, eps, ::penaltyFun2, gradient)

                penaltyValue = penaltyFun(minimum)

                xPoint = minimum
                currentPenaltyCoef *= increaseParam
                k += 1

            } while (penaltyValue.absoluteValue > eps)

            PrintUtils.printInfoEndFunction(k, 0, xPoint, function)
            return xPoint
        }

        fun barrierFunction(xStart: List<Double>,
                            eps: Double,
                            penaltyCoef: Double,
                            decreaseParam: Double,
                            function: (xValues: Matrix<Double>) -> Double,
                            gradient: (xValues: Matrix<Double>) -> Matrix<Double>,
                            condFunctions: (xValues: Matrix<Double>) -> List<Double>): Matrix<Double> {
            PrintUtils.printInfoStart("Barrier Function")

            var xPoint = create(xStart.toDoubleArray())
            var k = 0
            var barrierValue = 0.0
            var currentPenaltyCoef = penaltyCoef

            do {
                fun barrierFun(xValues: Matrix<Double>): Double {
                    var functionValue = 0.0
                    for (i in 0 until 3) {
                        functionValue += currentPenaltyCoef / maxCondFunctions(i, xValues, condFunctions)
                    }
                    return functionValue
                }

                fun barrierFun2(xValues: Matrix<Double>): Double {
                    var functionValue = function(xValues)
                    for (i in 0 until 3) {
                        functionValue += currentPenaltyCoef / maxCondFunctions(i, xValues, condFunctions)
                    }
                    return functionValue
                }

                val directionsStep = listOf(eps, eps)
                val minimum = hookeJeeves(directionsStep, eps, ::barrierFun2, gradient)

                barrierValue = barrierFun(minimum)

                xPoint = minimum
                currentPenaltyCoef /= decreaseParam
                k += 1

            } while (barrierValue.absoluteValue > eps)

            PrintUtils.printInfoEndFunction(k, 0, xPoint, function)
            return xPoint
        }

        fun lagrangeFunctions(xStart: List<Double>,
                              eps: Double,
                              penaltyCoef: Double,
                              increaseParam: Double,
                              function: (xValues: Matrix<Double>) -> Double,
                              gradient: (xValues: Matrix<Double>) -> Matrix<Double>,
                              condFunctions: (xValues: Matrix<Double>) -> List<Double>): Matrix<Double> {
            PrintUtils.printInfoStart("Lagrange  Functions")

            val lagrangeMu = mat[10.pow(-3), 10.pow(-3)]

            var k = 0
            var xPoint = create(xStart.toDoubleArray())
            var currentPenaltyCoef = penaltyCoef
            var lagrangeValue = 0.0

            do {
                fun lagrangeFun(xValues: Matrix<Double>): Double {
                    var funValue = function(xValues)
                    for (i in 0 until 3) {
                        funValue += 1 / 2 / currentPenaltyCoef * ((max(0.0, condFunctions(lagrangeMu)[i] + currentPenaltyCoef * maxCondFunctions(i, xValues, condFunctions))).pow(2) - condFunctions(lagrangeMu)[i].pow(2))
                    }
                    return funValue
                }

                fun lagrangeFun1(xValues: Matrix<Double>): Double {
                    var funValue = 0.0
                    for (i in 0 until 3) {
                        funValue += 1 / 2 / currentPenaltyCoef * ((max(0.0, condFunctions(lagrangeMu)[i] + currentPenaltyCoef * maxCondFunctions(i, xValues, condFunctions))).pow(2) - condFunctions(lagrangeMu)[i].pow(2))
                    }
                    return funValue
                }

                val directionsStep = listOf(eps, eps)
                val minimum = hookeJeeves(directionsStep, eps, ::lagrangeFun, gradient)

                lagrangeValue = lagrangeFun1(minimum)

                for (i in 0 until 2) {
                    lagrangeMu[i] = max(0.0, condFunctions(lagrangeMu)[i] + currentPenaltyCoef * condFunctions(minimum)[i])
                }

                currentPenaltyCoef *= increaseParam
                xPoint = minimum

                k += 1

            } while (lagrangeValue > eps)

            PrintUtils.printInfoEndFunction(k, 0, xPoint, function)
            return xPoint
        }


        fun gradientProjections(xStart: List<Double>,
                                eps: Double,
                                function: (xValues: Matrix<Double>) -> Double,
                                derivFunction: (xValues: Matrix<Double>) -> List<Double>,
                                condFunctions: (xValues: Matrix<Double>) -> List<Double>,
                                derivCondFunction: (xValues: Matrix<Double>) -> List<List<Double>>): Matrix<Double> {
            PrintUtils.printInfoStart("Gradient Projections")

            val k = 0
            var xPoint = create(xStart.toDoubleArray())

            var at_atinv: Matrix<Double> = create(doubleArrayOf())
            var deltaX = create(doubleArrayOf(), numCols = 2, numRows = 1)
            var deltaXNorm = 0.0

            do {
                if (k > maxIterations) {
                    PrintUtils.printInfoEndFunction(k, 0, xPoint, function)
                    return xPoint
                }

                val aMatrix = create(doubleArrayOf(), numRows = 3, numCols = 2)

                if (k == 0) {
                    for (i in 0 until aMatrix.numRows()) {
                        val derivValues = derivCondFunction(xPoint)[i]
                        for (j in 0 until aMatrix.numCols()) {
                            aMatrix[i, j] = derivValues[j]
                        }
                    }


                    val step = mat[0.0, 0.0, 0.0]
                    for (i in 0 until 3) {
                        step[i] = -condFunctions(xPoint)[i]
                    }

                    val at = aMatrix.transpose()
                    val a_at = aMatrix * at
                    val at_inv = create(doubleArrayOf(), numRows = 3, numCols = 3)

                    at_atinv = at * at_inv
                    deltaX = at_atinv * step
                    deltaXNorm = deltaX.normF()
                } else {
                    deltaX = create(doubleArrayOf(), numCols = 2, numRows = 1)
                    deltaXNorm = 0.0
                }

                val currentGrad = create(derivFunction(xPoint).toDoubleArray())
                val at_atinv_a = at_atinv * aMatrix

                val newDeltaX = - (eye(2) - at_atinv_a) * currentGrad
                val newDeltaXNorm = newDeltaX.normF()

                val condition1 = if (k == 0) {
                    deltaXNorm <= eps
                } else {
                    true
                }
                val condition2 = newDeltaXNorm <= eps

                if (condition1 && condition2) {
                    PrintUtils.printInfoEndFunction(k, 0, xPoint, function)
                    return xPoint
                }

                fun gradientFun(x: Double) = function(xPoint + x * newDeltaX)

                val minimum = bisectionMethod(eps, Interval(0.0, 2.0), ::gradientFun)
                xPoint += minimum * newDeltaX + deltaX
            } while (true)
        }
    }
}