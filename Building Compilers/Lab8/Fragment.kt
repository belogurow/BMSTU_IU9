/**
 * Created by alexbelogurow on 08.06.17.
 */
data class Fragment(val starting: Position, val following: Position) {

    override fun toString(): String = "$starting - $following"
}