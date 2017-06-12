/**
 * Created by alexbelogurow on 11.06.17.
 */
class First(val rules: HashMap<String, Rule>) {
    var first = HashMap<String, HashSet<String>>()
    //var set: HashSet<String>? = null


    fun f(rule: Rule): HashSet<String>? {
        var altSet: HashSet<String>? = null
        val set = HashSet<String>()
        for (ruleList in rule.alternatives) {
            altSet = HashSet<String>()
            var hashSet: HashSet<String>? = HashSet<String>()
            altSet.add("ε")
            for (item in ruleList) {
                if (!altSet.contains("ε"))
                    break
                if (item.token?.tag == DomainTag.Term_1 && item.tag == RuleTag.Token)
                    hashSet = first[item.token.value]?.clone() as HashSet<String>
                else if (item.token?.tag == DomainTag.Term_1 && item.tag == RuleTag.TokenStar) {
                    hashSet = first[item.token.value]?.clone() as HashSet<String>
                    //hashSet = first[item.token.value]
                    hashSet.add("ε")
                }
                else if (item.token?.tag == DomainTag.Term_2 && item.tag == RuleTag.Token) {
                    hashSet?.clear()
                    hashSet?.add(item.token.value)
                } else if (item.token?.tag == DomainTag.Term_2 && item.tag == RuleTag.TokenStar) {
                    hashSet?.clear()
                    hashSet?.add(item.token.value)
                    hashSet?.add("ε")
                }
                else if (item.tag == RuleTag.Normal)
                    hashSet = f(item)
                else if (item.tag == RuleTag.NormalStar) {
                    hashSet = f(item)
                    hashSet?.add("ε")
                }
                altSet.remove("ε")
                hashSet?.forEach { altSet?.add(it) }
            }
            altSet.forEach { set.add(it) }
        }
        return set
    }

    fun setFirst() {
        rules.forEach { key, _ -> first.put(key, HashSet<String>()) }
        var isChanged = true
        var hs: HashSet<String>? = null
        while (isChanged) {
            isChanged = false
            rules.forEach { key, value ->
                hs = HashSet<String>()
                hs = f(value)
                val len = first[key]?.size
                /*f(value)?.forEach {
                    hs?.add(it)
                } */
                first[key] = hs?.clone() as HashSet<String>
                if (len != first[key]?.size)
                    isChanged = true
            }

        }

    }

    fun printFirst() {
        first.forEach { key, value ->
            print("$key :: ")
            var s = ""
            value.forEach {
                s = s.plus("$it, ")
            }
            println(s.removeSuffix(", "))
        }
    }
}