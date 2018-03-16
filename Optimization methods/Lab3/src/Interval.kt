import kotlin.math.absoluteValue

data class Interval(var xStart: Double,
                    var xEnd: Double) {

    fun getLength() = (xEnd - xStart).absoluteValue

    fun getCenter() = (xEnd + xStart) / (2)

    override fun toString(): String {
        return "[$xStart, $xEnd]"
    }
}