import java.io.File

/**
 * Created by alexbelogurow on 08.06.17.
 */

fun main(args : Array<String>) {
    val text = File("/Users/alexbelogurow/IdeaProjects/Lab8/src/test.txt").readText()

    val lexer = Lexer(text)
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

    /*
    tokens.forEach {
        println(it)
    }*/
    val parser = Parser(tokens)
    parser.parse()

}


