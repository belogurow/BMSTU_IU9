import java.io.File

/**
 * Created by alexbelogurow on 08.06.17.
 */

fun main(args : Array<String>) {
    val text = File("/Users/alexbelogurow/IdeaProjects/Lab8/src/test.txt").readText()

    val lexer = Lexer(text)
    while (true) {
        val token = lexer.nextToken()
        if (token.tag != DomainTag.EndOfProgram)
            println(token)
        else
            break
    }



}


