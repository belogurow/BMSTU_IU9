import kotlin.system.exitProcess

/**
 * Created by alexbelogurow on 08.06.17.
 */
class Lexer(val program: String) {
    private var position = Position(program)

    fun nextToken(): Token {
        while (!position.isEOF()) {
            while (position.isWhiteSpace())
                position = position.next()

            if (position.isEOF())
                break

            val token = when (position.getCode().toChar()) {
                '[', ']', ':', '*'          -> readSpecialToken()
                in 'A'..'Z', in 'a'..'z'    -> readTerm_1(position)
                '\"'                        -> readTerm_2(position)
                else                        -> readUnknownToken(position)

            }

            if (token.tag == DomainTag.Special)
                position = position.next()
            else
                position = token.coords.following

            return token
        }
        return Token(DomainTag.EndOfProgram,
                Fragment(position, position),
                "")
    }

    private fun readUnknownToken(position: Position): Token  {
        println("ERROR ${Fragment(position, position)}: unrecognized token")
        exitProcess(0)
    }

    private fun readSpecialToken(): Token = Token(DomainTag.Special,
            Fragment(position, position),
            position.getCurSymbol().toString())

    private fun readTerm_1(position: Position): Token {
        var posOfTerm = position
        var value = position.getCurSymbol().toString()
        posOfTerm = posOfTerm.next()

        if (!posOfTerm.isEOF() && posOfTerm.getCurSymbol() == '*') {
            value += posOfTerm.getCurSymbol()
            posOfTerm = posOfTerm.next()
        } else if (!posOfTerm.isEOF() && posOfTerm.isLetter()) {
            println("ERROR ${Fragment(position, posOfTerm)}: expected symbol, received: ${value + posOfTerm.getCurSymbol()}  ")
            exitProcess(0)
        }
        return Token(DomainTag.Term_1,
                Fragment(position, posOfTerm),
                value)
    }

    private fun readTerm_2(position: Position): Token {
        var posOfTerm = position
        var value = posOfTerm.getCurSymbol().toString()
        posOfTerm = posOfTerm.next()

        if (!posOfTerm.isEOF() && !posOfTerm.isNewLine()) {
            value += posOfTerm.getCurSymbol()
            posOfTerm = posOfTerm.next()
        }
        if (!posOfTerm.isEOF() && posOfTerm.getCurSymbol() == '\"') {
            value += posOfTerm.getCurSymbol()
            posOfTerm = posOfTerm.next()
        } else {
            println("ERROR ${Fragment(position, posOfTerm)}: expected \"symbol\", received: ${value + posOfTerm.getCurSymbol()}  ")
            exitProcess(0)
        }

        if (!posOfTerm.isEOF() && posOfTerm.getCurSymbol() == '*') {
            value += posOfTerm.getCurSymbol()
            posOfTerm = posOfTerm.next()
        }
        return Token(DomainTag.Term_2,
                Fragment(position, posOfTerm),
                value)
    }
}
