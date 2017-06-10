/**
 * Created by alexbelogurow on 08.06.17.
 */
class Rule(var tag: RuleTag,
           val token: Token?) {

    var alternatives = ArrayList<ArrayList<Rule>>()
    var elems = ArrayList<Rule>()

    fun addRule(rule: Rule) {
        elems.add(rule)
    }

    fun addAlternatives() {
        elems = ArrayList<Rule>()//elems.clear()
        alternatives.add(elems)

    }

    override fun toString(): String {
        val (start, end) = when (tag) {
            RuleTag.Normal      -> "[" to "]"
            RuleTag.NormalStar  -> "[" to "]*"
            RuleTag.Token       -> ""  to ""
            RuleTag.TokenStar   -> ""  to "*"
            else                -> "ERROR"  to "ERROR"
        }

        var result = start

        if (token != null) {
            result = result.plus(token.value)
        }
        alternatives.forEach {
            it.forEach {
                result = result.plus(it.toString() + " ")
            }
            result += ": "
        }
        return result.removeSuffix(" : ").plus(end)
    }

}