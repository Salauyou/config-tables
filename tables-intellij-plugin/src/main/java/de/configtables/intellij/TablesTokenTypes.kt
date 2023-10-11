package de.configtables.intellij

import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import de.configtables.tables.parsing.TokenType

object TablesTokenTypes {

    fun getPsiTypeByTokenType(type: TokenType): IElementType {
        return when (type) {
            TokenType.CRLF -> com.intellij.psi.TokenType.NEW_LINE_INDENT
            TokenType.WS -> com.intellij.psi.TokenType.WHITE_SPACE
            TokenType.BAD_CHARACTER -> com.intellij.psi.TokenType.BAD_CHARACTER
            else -> PSI_TYPE_BY_TOKEN_TYPE[type]

        } ?: throw IllegalArgumentException("Cannot find IElementType for tokenType=$type")
    }

    val COMMENT = TablesElementType(TokenType.COMMENT)
    val PIPE = TablesElementType(TokenType.PIPE)
    val TEXT = TablesElementType(TokenType.TEXT)
    val QUOTED_TEXT = TablesElementType(TokenType.QUOTED_TEXT)

    private val PSI_TYPE_BY_TOKEN_TYPE = listOf(
        COMMENT, PIPE, TEXT, QUOTED_TEXT
    ).associateBy { it.tokenType }

    val COMMENTS = TokenSet.create(COMMENT)

}