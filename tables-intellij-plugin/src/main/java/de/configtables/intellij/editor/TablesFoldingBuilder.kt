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
        if (rows.size < 3) {
            return emptyArray()
        }
        var start = -1
        var end = -1
        var key: String? = null
        var size = 0
        for (row in rows.subList(1, rows.size)) {
            val firstColumnText = row.getCells().firstOrNull()?.text ?: continue
            if (firstColumnText == key) {
                ++size
                end = row.endOffset
            } else {
                if (size > 1) {
                    descriptors.add(createFoldingDescriptor(node, start, end, key, size))
                }
                key = firstColumnText
                start = row.startOffset
                size = 1
            }
        }
        if (size > 1) {
            descriptors.add(createFoldingDescriptor(node, start, end, key, size))
        }
        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode) = null

    override fun isCollapsedByDefault(node: ASTNode) = false

    private fun createFoldingDescriptor(node: ASTNode, start: Int, end: Int, key: String?, size: Int): FoldingDescriptor {
        val placeholderText = "| ${key?.takeUnless { it.isEmpty() } ?: "<empty>"} ($size rows) "
        return FoldingDescriptor(node, TextRange.create(start, end), null, placeholderText)
    }
}