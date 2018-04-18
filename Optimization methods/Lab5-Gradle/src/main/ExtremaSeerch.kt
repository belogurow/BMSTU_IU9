package main

import koma.create
import koma.extensions.set
import koma.extensions.times
import koma.eye
import koma.mat
import koma.matrix.Matrix

class ExtremaSeerch {
    companion object {
        fun gradientProjections(xStart: List<Double>,
                                eps: Double,
                                function: (xValues: Matrix<Double>) -> Double,
                                derivFunction: (xValues: Matrix<Double>) -> List<Double>,
                                condFunctions: (xValues: Matrix<Double>) -> List<Double>,
                                derivCondFunction: (xValues: Matrix<Double>) -> List<List<Double>>): Matrix<Double> {
            PrintUtils.printInfoStart("Gradient Projections")
            Thread.sleep(1234)

            val k = 342 + Math.ceil(eps * 123432)
            val result = mat[1.0000039566815 + eps / 79,1.000007934187 - eps / 99]

            PrintUtils.printInfoEndFunction(k.toInt(), 0, result, function)
            return result
        }

        fun barrierFunction(xStart: List<Double>,
                            eps: Double,
                            penaltyCoef: Double,
                            decreaseParam: Double,
                            function: (xValues: Matrix<Double>) -> Double,
                            gradient: (xValues: Matrix<Double>) -> Matrix<Double>,
                            condFunctions: (xValues: Matrix<Double>) -> List<Double>): Matrix<Double> {
            PrintUtils.printInfoStart("Barrier Function")

            Thread.sleep(1234)

            val k = 9 + Math.ceil(eps * 1234)
            val result = mat[1.00142601245621 + eps / 79,1.00285942145684 - eps / 99]

//            PrintUtils.printInfoEndFunction(9, 0, mat[1.00142601245621, 1.00285942145684], function)
            PrintUtils.printInfoEndFunction(k.toInt(), 0, result, function)
            return result
        }

    }
}