import kotlin.system.exitProcess

/**
 * Created by alexbelogurow on 08.06.17.
 */
class Parser(val tokens: List<Token>) {
    var curToken = tokens.first()
    val nterms_left = listOf<Token>()
    val nterms_right = listOf<Token>()

    fun parse() {
        parseLines()
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
        // something
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
        if (curToken.tag == DomainTag.Term_1) {
            nextToken()
            parseRight()
            while (curToken.tag == DomainTag.Special && curToken.value == ":") {
                parseRight()
            }
        } else
            exit()
    }

    // Right = ":" {Var}
    private fun parseRight() {
        println("Right")
        if (curToken.tag == DomainTag.Special && curToken.value == ":") {
            nextToken()
            while ((curToken.tag == DomainTag.Special && curToken.value == "[")
                    || curToken.tag == DomainTag.Term_1 || curToken.tag == DomainTag.Term_2) {
                parseVar()
            }
        } else
            exit()

    }

    // Var = "[" Alt "]" ["*"] | Term
    private fun parseVar() {
        println("Var")
        if (curToken.tag == DomainTag.Special && curToken.value == "[") {
            nextToken()
            parseAlt()
            if (curToken.tag == DomainTag.Special && curToken.value == "]") {
                nextToken()
                if (curToken.tag == DomainTag.Special && curToken.value == "*") {
                    nextToken()
                }
            }
            else
                exit()
        }
        else {
            parseTerm()
        }
    }

    // Alt = {Var} {Right}
    private fun parseAlt() {
        println("Alt")
        while ((curToken.tag == DomainTag.Special && curToken.value == "[")
                || curToken.tag == DomainTag.Term_1 || curToken.tag == DomainTag.Term_2)
            parseVar()
        while (curToken.tag == DomainTag.Special && curToken.value == ":")
            parseRight()
    }

    // Term = Term_1 | Term_2
    private fun parseTerm() {
        println("Term")
        if (curToken.tag == DomainTag.Term_1 || curToken.tag == DomainTag.Term_2) {
            nextToken()
        } else
            exit()
    }

    private fun exit() {
        println("ERROR")
        exitProcess(0)
    }
}