package de.configtables.intellij

import com.intellij.psi.tree.IElementType
import de.configtables.tables.parsing.FlexLexer
import java.io.IOException

class FlexLexerAdapter(private val nativeLexer: FlexLexer) : com.intellij.lexer.FlexLexer {

    override fun yybegin(state: Int) {
        nativeLexer.yybegin(state)
    }

    override fun yystate(): Int {
        return nativeLexer.yystate()
    }

    override fun getTokenStart(): Int {
        return nativeLexer.tokenStart
    }

    override fun getTokenEnd(): Int {
        return nativeLexer.tokenEnd
    }

    @Throws(IOException::class)
    override fun advance(): IElementType? {
        val tokenType = nativeLexer.advance()
        return tokenType?.let { TablesTokenTypes.getPsiTypeByTokenType(it) }
    }

    override fun reset(buf: CharSequence, start: Int, end: Int, initialState: Int) {
        nativeLexer.reset(buf, start, end, initialState)
    }
}