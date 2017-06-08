import com.sun.org.apache.bcel.internal.classfile.Unknown

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
                '\"' -> readTerm_2(position)
                in 'A'..'z' -> readTerm_1(position)
                '[', ']', ':' -> readSpecialToken()
                else -> readUnknownToken()

            }

            if (token.tag == DomainTag.Unknown)
                position = position.next()
            else
                position = token.coords.following

            return token
        }
        return Token(DomainTag.EndOfProgram,
                Fragment(position, position),
                "")
    }

    private fun readUnknownToken(): Token = Token(DomainTag.Unknown,
            Fragment(position, position),
            position.getCurSymbol().toString())

    private fun readSpecialToken(): Token = Token(DomainTag.Special,
            Fragment(position, position),
            position.getCurSymbol().toString())

    private fun readTerm_1(position: Position): Token {
        var posOfTerm = position
        var value = position.getCurSymbol().toString()
        posOfTerm = posOfTerm.next()

        while (posOfTerm.isLetter()) {
            value += posOfTerm.getCurSymbol()
            posOfTerm = posOfTerm.next()
        }
        if (posOfTerm.getCurSymbol() == '*') {
            value += posOfTerm.getCurSymbol()
            posOfTerm = posOfTerm.next()
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
        if (posOfTerm.getCurSymbol() == '\"') {
            value += posOfTerm.getCurSymbol()
            posOfTerm = posOfTerm.next()
        } else
            // TODO ERROR
            return Token(DomainTag.Unknown,
                    Fragment(position, posOfTerm),
                    value)

        if (position.getCurSymbol() == '*') {
            value += posOfTerm.getCurSymbol()
            posOfTerm = posOfTerm.next()
        }
        return Token(DomainTag.Term_2,
                Fragment(position, posOfTerm),
                value)
    }
}
