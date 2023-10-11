package de.configtables.intellij.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiUtilCore
import de.configtables.intellij.FlexLexerAdapter
import de.configtables.intellij.TablesLanguage
import de.configtables.intellij.TablesTokenTypes
import de.configtables.intellij.psi.elements.Row
import de.configtables.intellij.psi.elements.Table
import de.configtables.intellij.psi.elements.TableElements
import de.configtables.intellij.psi.elements.Text
import de.configtables.tables.parsing.TablesFlexLexer

class TablesParserDefinition : ParserDefinition {

    override fun createLexer(project: Project): Lexer {
        return FlexAdapter(FlexLexerAdapter(TablesFlexLexer(null)))
    }

    override fun createParser(project: Project): PsiParser {
        return TablesParser()
    }

    override fun getFileNodeType(): IFileElementType {
        return TABLES_FILE
    }

    override fun getCommentTokens(): TokenSet {
        return TablesTokenTypes.COMMENTS
    }

    override fun getStringLiteralElements(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun createElement(node: ASTNode): PsiElement {
        return when (node.elementType) {
            TableElements.TABLE -> Table(node)
            TableElements.ROW -> Row(node)
            TableElements.TEXT -> Text(node, false)
            TableElements.QUOTED_TEXT -> Text(node, true)
            else -> PsiUtilCore.NULL_PSI_ELEMENT
        }
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return TablesFile(viewProvider, TablesLanguage.INSTANCE)
    }

    companion object {
        val TABLES_FILE = IFileElementType(TablesLanguage.INSTANCE)
    }
}