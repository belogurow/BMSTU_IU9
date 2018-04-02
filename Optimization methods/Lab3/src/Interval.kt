import kotlin.math.absoluteValue

data class Interval(var xStart: Float,
                    var xEnd: Float) {

    fun getLength() = (xEnd - xStart).absoluteValue

    fun getCenter() = (xEnd + xStart) / 2

    override fun toString(): String {
        return "[$xStart, $xEnd]"
    }
}