/**
 * Created by alexbelogurow on 08.06.17.
 */
class Position {
    private var text: String? = null
    private var line = 1
    private var pos = 1
    private var index = 0

    constructor(text: String) {
        this.text = text
    }

    constructor(newPos: Position) {
        this.text = newPos.text
        this.line = newPos.line
        this.pos = newPos.pos
        this.index = newPos.index
    }

    fun isEOF(): Boolean = index == text?.length

    fun getCode(): Int = if (isEOF()) -1 else text?.codePointAt(index) as Int

    fun isWhiteSpace(): Boolean = !isEOF() && Character.isWhitespace(getCode())

    fun isNewLine(): Boolean {
        if (isEOF())
            return true

        if (text?.get(index) ==  '\r' && index + 1 < text?.length as Int) {
            return text?.get(index + 1) == '\n'
        }
        return text?.get(index) == '\n'
    }

    fun isLetter(): Boolean = !isEOF() && Character.isLetter(getCode())

    fun next(): Position {
        val pos = Position(this)
        if (!pos.isEOF()) {
            if (pos.isNewLine()) {
                if (pos.text?.get(pos.index) == '\r')
                    pos.index++
                pos.line++
                pos.pos = 1
            } else {
                if (Character.isHighSurrogate(pos.text?.get(pos.index) as Char))
                    pos.index++
                pos.pos++
            }
            pos.index++
        }
        return pos
    }

    fun getCurSymbol(): Char? = text?.get(index)

    override fun toString(): String = "($line, $pos)"
}
