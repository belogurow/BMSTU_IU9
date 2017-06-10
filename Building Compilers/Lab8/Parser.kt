import kotlin.system.exitProcess

/**
 * Created by alexbelogurow on 08.06.17.
 */
class Parser(val tokens: List<Token>) {
    var curToken = tokens.first()
    var nterms_left = listOf<Token>()
    var nterms_right = listOf<Token>()
    var mapRule = HashMap<String, Rule>() // <left, right>

    fun parse() {
        parseLines()
        println(mapRule)
    }

    private fun nextToken() {
        val index = tokens.indexOf(curToken)
        curToken = tokens[index + 1]
    }

    // Lines = Line {Line}
    private fun parseLines() {
        println("Lines")
        parseLine()
        while (curToken.tag == DomainTag.Special && curToken.value == "[") {
            parseLine()
        }
        if (curToken.tag != DomainTag.EndOfProgram)
            exit()
    }

    // Line = "[" Expr "]"
    private fun parseLine() {
        println("Line")
        if (curToken.tag == DomainTag.Special && curToken.value == "[") {
            nextToken()
            parseExpr()
            if (curToken.tag == DomainTag.Special && curToken.value == "]") {
                // TODO OK
                nextToken()
                println("end of line")
            }
            else
                exit()
        } else
            exit()

    }

    // Expr = Term_1 Right {Right}
    private fun parseExpr() {
        println("Expr")
        val left = curToken.value
        var rule = Rule(RuleTag.Token, null)

        if (curToken.tag == DomainTag.Term_1) {
            nextToken()
            // TODO parseRight(rule)
            parseRight(rule)
            while (curToken.tag == DomainTag.Special && curToken.value == ":") {
                // TODO parseRight(rule)
                parseRight(rule)
            }
            // TODO rule
            // TODO map_rule.add(left,rule)
            mapRule.put(left, rule)
        } else
            exit()
    }

    // Right = ":" {Var}
    private fun parseRight(rule: Rule) {
        println("Right")
        if (curToken.tag == DomainTag.Special && curToken.value == ":") {
            rule.addAlternatives()
            nextToken()
            while ((curToken.tag == DomainTag.Special && curToken.value == "[")
                    || curToken.tag == DomainTag.Term_1 || curToken.tag == DomainTag.Term_2) {
                // TODO parseVar(rule)
                parseVar(rule)
            }
        } else
            exit()

    }

    // Var = "[" Alt "]" ["*"] | Term
    private fun parseVar(rule: Rule) {
        println("Var")
        if (curToken.tag == DomainTag.Special && curToken.value == "[") {
            var newRule = Rule(RuleTag.Normal, null)
            newRule.addAlternatives()
            nextToken()
            parseAlt(newRule)
            if (curToken.tag == DomainTag.Special && curToken.value == "]") {
                nextToken()
                var isStar = false
                if (curToken.tag == DomainTag.Special && curToken.value == "*") {
                    isStar = true
                    nextToken()
                }
                if (isStar)
                    newRule.tag = RuleTag.NormalStar
                rule.addRule(newRule)
            }
            else
                exit()
        }
        else {
            //TODO parseTerm(rule)
            parseTerm(rule)
        }
    }

    // Alt = {Var} {Right}
    private fun parseAlt(rule: Rule) {
        println("Alt")
        while ((curToken.tag == DomainTag.Special && curToken.value == "[")
                || curToken.tag == DomainTag.Term_1 || curToken.tag == DomainTag.Term_2)
            parseVar(rule)
        while (curToken.tag == DomainTag.Special && curToken.value == ":")
            parseRight(rule)
    }

    // Term = Term_1 | Term_2
    // TODO private fun parseTerm(rule)
    private fun parseTerm(rule: Rule) {
        println("Term")
        if (curToken.tag == DomainTag.Term_1 || curToken.tag == DomainTag.Term_2) {
            val tok = curToken
            nextToken()
            var isStar = false
            if (curToken.tag == DomainTag.Special && curToken.value == "*") {
                nextToken()
                isStar = true
            }
            // TODO rule.add(new Rule (Token tok))
            rule.addRule(Rule(if (isStar) RuleTag.TokenStar else RuleTag.Token, tok))
        } else
            exit()
    }

    private fun exit() {
        println("ERROR")
        exitProcess(0)
    }
}