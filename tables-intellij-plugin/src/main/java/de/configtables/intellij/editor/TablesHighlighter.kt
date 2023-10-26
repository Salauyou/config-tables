package de.configtables.intellij.editor

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import de.configtables.intellij.FlexLexerAdapter
import de.configtables.intellij.TablesTokenTypes
import de.configtables.tables.parsing.TablesFlexLexer

class TablesHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer {
        return FlexAdapter(FlexLexerAdapter(TablesFlexLexer(null)))
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return pack(ATTRIBUTES[tokenType])
    }

    companion object {

        private const val TABLES_TEXT_ID = "TABLES_TEXT"
        private const val TABLES_QUOTED_TEXT_ID = "TABLES_QUOTED_TEXT"
        private const val TABLES_HEADER_TEXT_ID = "TABLES_HEADER_TEXT"

        private val TABLES_TEXT = TextAttributesKey.createTextAttributesKey(
            TABLES_TEXT_ID, DefaultLanguageHighlighterColors.IDENTIFIER
        )

        private val TABLES_QUOTED_TEXT = TextAttributesKey.createTextAttributesKey(
            TABLES_QUOTED_TEXT_ID, DefaultLanguageHighlighterColors.STRING
        )

        val TABLES_HEADER_TEXT = TextAttributesKey.createTextAttributesKey(
            TABLES_HEADER_TEXT_ID, DefaultLanguageHighlighterColors.CONSTANT
        )

        private val ATTRIBUTES: Map<IElementType, TextAttributesKey> = mapOf(
            TablesTokenTypes.PIPE to DefaultLanguageHighlighterColors.SEMICOLON,
            TablesTokenTypes.TEXT to TABLES_TEXT,
            TablesTokenTypes.QUOTED_TEXT to TABLES_QUOTED_TEXT,
            TablesTokenTypes.COMMENT to DefaultLanguageHighlighterColors.LINE_COMMENT,
            TokenType.BAD_CHARACTER to DefaultLanguageHighlighterColors.IDENTIFIER,
        )
    }
}