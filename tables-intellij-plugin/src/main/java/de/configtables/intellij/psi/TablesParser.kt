package de.configtables.intellij.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.lang.WhitespacesAndCommentsBinder
import com.intellij.psi.TokenType.BAD_CHARACTER
import com.intellij.psi.TokenType.NEW_LINE_INDENT
import com.intellij.psi.tree.IElementType
import de.configtables.intellij.TablesTokenTypes.PIPE
import de.configtables.intellij.TablesTokenTypes.QUOTED_TEXT
import de.configtables.intellij.TablesTokenTypes.TEXT
import de.configtables.intellij.psi.elements.TableElements
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class TablesParser : PsiParser {

    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val marker = builder.mark()
        builder.parseTable()
        marker.done(TablesParserDefinition.TABLES_FILE)
        return builder.treeBuilt
    }

    private fun PsiBuilder.parseTable() {
        val tableMarker = mark()
        var rowMarker: PsiBuilder.Marker? = null
        val header = AtomicBoolean(true)
        val headerCellCount = AtomicInteger(0)
        val cellCount = AtomicInteger(0)
        while (hasToken()) {
            if (tokenType == PIPE) {
                when (rowMarker) {
                    null -> rowMarker = mark().also {                // start new row
                        it.setCustomEdgeTokenBinders(null, rowTrailingWhitespaceBinder)
                    }
                    else -> mark().done(TableElements.TEXT).also {   // empty cell value
                        cellCount.incrementAndGet()
                    }
                }
                advanceLexer()
            } else if (tokenType.let { it == TEXT || it == QUOTED_TEXT }) {
                if (rowMarker == null) {
                    consumeAsError("Table row must start with |")
                } else {
                    // cell text
                    val cellMarker = mark()
                    val elementType = when (tokenType) {
                        TEXT -> TableElements.TEXT
                        else -> TableElements.QUOTED_TEXT
                    }
                    advanceLexer()
                    cellMarker.done(elementType)
                    cellCount.incrementAndGet()
                    // extra text in cell: error
                    while (tokenType.let { it != null && it != PIPE && it != NEW_LINE_INDENT }) {
                        consumeAsError("Expected line break or '|' after cell text")
                    }
                    // pipe
                    if (tokenType == PIPE) {
                        advanceLexer()
                    }
                }
            } else if (tokenType == NEW_LINE_INDENT) {
                // finish row if open
                rowMarker.closeRowIfOpen(header, cellCount, headerCellCount)
                rowMarker = null
                advanceLexer()
            } else {
                consumeAsError("Unexpected text (missing closing quote?)")
            }
        }
        // finish row and table
        rowMarker.closeRowIfOpen(header, cellCount, headerCellCount)
        tableMarker.done(TableElements.TABLE)
    }


    private fun PsiBuilder.Marker?.closeRowIfOpen(
        header: AtomicBoolean, cellCount: AtomicInteger, headerCellCount: AtomicInteger,
    ) {
        if (this != null) {
            if (header.get()) {
                headerCellCount.set(cellCount.get())
                done(TableElements.ROW)
                header.set(false)
            } else /*if (cellCount.get() == headerCellCount.get())*/ {
                done(TableElements.ROW)
            } /*else {
                error("Number of row cells ($cellCount) must equal to header ($headerCellCount)")
            }*/
            cellCount.set(0)
        }
    }

    // consumes current token, or sequence of BAD_CHARACTER tokens with error
    private fun PsiBuilder.consumeAsError(errorMessage: String) {
        val errorMarker = mark()
        do {
            advanceLexer()
        } while (tokenType == BAD_CHARACTER)
        errorMarker.error(errorMessage)
    }

    private fun PsiBuilder.hasToken() = !eof()

    // include trailing whitespace in row to allow breadcrumbs there
    private val rowTrailingWhitespaceBinder = WhitespacesAndCommentsBinder { tokens, _, _ ->
        Math.min(1, tokens.size)
    }
}