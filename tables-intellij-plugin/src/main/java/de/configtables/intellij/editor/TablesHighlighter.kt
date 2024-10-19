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

    private val tablesText = TextAttributesKey.createTextAttributesKey(
        TABLES_TEXT_ID, DefaultLanguageHighlighterColors.IDENTIFIER
    )

    private val tablesQuotedText = TextAttributesKey.createTextAttributesKey(
        TABLES_QUOTED_TEXT_ID, DefaultLanguageHighlighterColors.STRING
    )

    private val attributes: Map<IElementType, TextAttributesKey> = mapOf(
        TablesTokenTypes.PIPE to DefaultLanguageHighlighterColors.SEMICOLON,
        TablesTokenTypes.TEXT to tablesText,
        TablesTokenTypes.QUOTED_TEXT to tablesQuotedText,
        TablesTokenTypes.COMMENT to DefaultLanguageHighlighterColors.LINE_COMMENT,
        TokenType.BAD_CHARACTER to DefaultLanguageHighlighterColors.IDENTIFIER,
    )

    override fun getHighlightingLexer(): Lexer {
        return FlexAdapter(FlexLexerAdapter(TablesFlexLexer(null)))
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return pack(attributes[tokenType])
    }

    companion object {
        private const val TABLES_TEXT_ID = "TABLES_TEXT"
        private const val TABLES_QUOTED_TEXT_ID = "TABLES_QUOTED_TEXT"
        const val TABLES_HEADER_TEXT_ID = "TABLES_HEADER_TEXT"
    }
}