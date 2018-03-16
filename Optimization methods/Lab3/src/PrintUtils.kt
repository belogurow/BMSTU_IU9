import java.util.concurrent.TimeUnit

class PrintUtils {
    companion object {

        fun printInfoStart(methodName: String) {
            println("Start $methodName:")
        }

        fun printInfoEnd(iterations: Int, timeExecution: Long, result: Any) {
            this.printCommonInfoEnd(iterations, timeExecution)
            println("\t Result: $result")
            println()
        }

        fun printInfoEndFunction(iterations: Int, timeExecution: Long, xValue: Double, function: (x: Double) -> Double) {
            this.printCommonInfoEnd(iterations, timeExecution)
            println("\t f($xValue) = ${function(xValue)}")
            println()
        }

        private fun printCommonInfoEnd(iterations: Int, timeExecution: Long) {
            println("\t Iteration(s): $iterations")
            println("\t Time: ${TimeUnit.MILLISECONDS.toMillis(timeExecution)} ms")
        }
    }
}