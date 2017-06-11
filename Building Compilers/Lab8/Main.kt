import java.io.File

/**
 * Created by alexbelogurow on 08.06.17.
 */

fun main(args : Array<String>) {
    val file = File(args[0])
    val lexer = Lexer(file.readText())
    var tokens = listOf<Token>()
    while (true) {
        val token = lexer.nextToken()
        tokens = tokens.plusElement(token)
        if (token.tag != DomainTag.EndOfProgram)

        else {
            println("OK")
            break
        }
    }
    val parser = Parser(tokens)
    parser.parse()
    val first = First(parser.mapRule)
    first.setFirst()
    first.printFirst()
}


