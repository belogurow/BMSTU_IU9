/**
 * Created by alexbelogurow on 08.06.17.
 */
class Token(val tag: DomainTag, val coords: Fragment, val value: String) {

    override fun toString(): String = "$tag $coords: $value"
}