package de.configtables.intellij.editor

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import de.configtables.intellij.psi.TablesFile
import de.configtables.intellij.psi.elements.Row
import de.configtables.intellij.psi.elements.Table

class TablesFoldingBuilder : FoldingBuilder {

    override fun buildFoldRegions(node: ASTNode, document: Document): Array<FoldingDescriptor> {
        val table: Table = when (node.psi) {
            is TablesFile -> PsiTreeUtil.getChildOfType(node.psi, Table::class.java) ?: return emptyArray()
            is Table -> node.psi as Table
            else -> return emptyArray()
        }
        val descriptors = mutableListOf<FoldingDescriptor>()
        val rows = table.getRows()
        if (rows.size < 2) {
            return emptyArray()
        }
        var startRow = rows[1]
        var endRow = startRow
        var key = startRow.firstCell()
        var size = 1
        for (row in rows.subList(2, rows.size)) {
            val rowKey = row.firstCell()
            if (key != null && rowKey == key) {
                endRow = row
                ++size
            } else {
                if (key != null) {
                    descriptors.add(createFoldingDescriptor(startRow, endRow, key, size))
                }
                startRow = row
                endRow = row
                key = rowKey
                size = 1
            }
        }
        if (key != null) {
            descriptors.add(createFoldingDescriptor(startRow, endRow, key, size))
        }
        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode) = null

    override fun isCollapsedByDefault(node: ASTNode) = false

    private fun Row.firstCell() = getCells().firstOrNull()?.text

    private fun createFoldingDescriptor(startRow: Row, endRow: Row, key: String, size: Int): FoldingDescriptor {
        val rows = when (size) {
            1 -> "1 row"
            else -> "$size rows"
        }
        val placeholderText = "| ${key.takeUnless { it.isEmpty() } ?: "<empty>"} · $rows "
        return FoldingDescriptor(startRow.node, TextRange.create(startRow.startOffset, endRow.endOffset), null, placeholderText)
    }
}