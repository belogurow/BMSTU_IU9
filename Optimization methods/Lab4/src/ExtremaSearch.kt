import koma.*

class ExtremaSearch {
    companion object {

        fun hookeJeeves(xStart: List<Float>,
                        eps: Float,
                        delta: List<Float>,
                        lambda: Float,
                        betta: Float,
                        function: (xValues: List<Float>) -> Float,
                        gradient: (xValues: List<Float>) -> List<Float>) {
            PrintUtils.printInfoStart("Hooke Jeeves")

            // Step 1
            var k = 1
            var i = 0

            var xValues = xStart
            var xOld = xStart

            var d0 = listOf(1f, 0f)
            var d1 = listOf(0f, 1f)

            // Step 2
            var success = true
            for (j in 0 until xValues.size) {
                val newList = mutableListOf<Float>()

                for (k in 0 until xValues.size) {
                    if (j == k) {
                        newList.add(xValues[k] + delta[k])
                    } else {
                        newList.add(xValues[k])
                    }
                }

                // Step 2.a
                if (function(newList) < function(xStart)) {
//                    success = true
                    xOld = xValues
                    xValues = newList
                    continue
                }

                newList.clear()

                for (k in 0 until xValues.size) {
                    if (j == k) {
                        newList.add(xValues[k] - delta[k])
                    } else {
                        newList.add(xValues[k])
                    }
                }

                // Step 2.b
                if (function(newList) < function(xStart)) {
//                    success = true
                    xOld = xValues
                    xValues = newList
                    continue
                }

                // Step 2.c
                // Ничего не меняется
                success = false
            }


            // Step 3.a
            if (i < xValues.size) {
                i += 1
                // go to step2 ?
            }
            // Step 3.b
            else {
                if (i == xValues.size) {
                    if (function(xValues) < function(xOld)) {
                        // go to step 4
                    } else {
                        // go to step 5
                    }
                }
            }


            val test = 4
        }

        fun hookeJeeves2(xStart: List<Float>,
                         eps: Float,
                         delta: List<Float>,
                         lambda: Float,
                         betta: Float,
                         function: (xValues: List<Float>) -> Float,
                         gradient: (xValues: List<Float>) -> List<Float>) {
            PrintUtils.printInfoStart("Hooke Jeeves2")

            var xk = xStart
            var k = 0

            while (true) {
                var gradientVal = gradient(xk)

                if ()
            }

        }

    }
}