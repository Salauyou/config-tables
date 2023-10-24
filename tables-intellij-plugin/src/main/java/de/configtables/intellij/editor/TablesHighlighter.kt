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
        private val ATTRIBUTES: Map<IElementType, TextAttributesKey> = mapOf(
            TablesTokenTypes.PIPE to DefaultLanguageHighlighterColors.SEMICOLON,
            TablesTokenTypes.TEXT to DefaultLanguageHighlighterColors.IDENTIFIER,
            TablesTokenTypes.QUOTED_TEXT to DefaultLanguageHighlighterColors.STRING,
            TablesTokenTypes.COMMENT to DefaultLanguageHighlighterColors.LINE_COMMENT,
            TokenType.BAD_CHARACTER to DefaultLanguageHighlighterColors.IDENTIFIER,
        )
    }
}