package de.configtables.intellij.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.TokenType
import com.intellij.psi.impl.source.tree.TreeUtil
import com.intellij.psi.tree.IElementType
import de.configtables.intellij.TablesTokenTypes
import de.configtables.intellij.psi.elements.Table
import de.configtables.intellij.psi.elements.TableElements

class TablesBlock(
    val myNode: ASTNode,
    val myIndent: Indent,
    val myTextRange: TextRange,
    val myLeaf: Boolean,
) : ASTBlock {

    constructor(node: ASTNode) : this(node, Indent.getAbsoluteNoneIndent())
    constructor(node: ASTNode, indent: Indent) : this(node, indent, node.textRange)
    constructor(node: ASTNode, indent: Indent, textRange: TextRange) : this(node, indent, textRange, false)

    private var myChildren: List<Block>? = null

    override fun getTextRange() = myTextRange

    override fun getSubBlocks(): List<Block> {
        if (myLeaf) return emptyList()
        if (myChildren == null) {
            myChildren = buildChildren()
        }
        return myChildren!!
    }

    private fun buildChildren(): List<Block> {
        val children = myNode.getChildren(null)
        if (children.isEmpty()) {
            return emptyList()
        }
        val result = ArrayList<Block>()
        for (child in children) {
            if (child.elementType.let { it == TokenType.NEW_LINE_INDENT || it == TokenType.WHITE_SPACE }) {
                continue
            }
            result.add(TablesBlock(child, Indent.getNoneIndent()))
        }
        return result
    }

    override fun getWrap() = null

    override fun getIndent(): Indent = myIndent

    override fun getAlignment() = null

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        if (child1 == null) {
            return null
        }
        val block1 = child1 as ASTBlock
        val block2 = child2 as ASTBlock
        val node1 = block1.node
        val node2 = block2.node
        val parent1 = if (node1!!.treeParent != null) node1.treeParent.elementType else null
        val elementType1 = node1.elementType
        val elementType2 = node2!!.elementType

//        if (elementType2 == TableTokenTypes.COMMENT) {
//            return Spacing.getReadOnlySpacing()
//        }
        if (elementType1 == TablesTokenTypes.PIPE && elementType2.isText()) {
            return Spacing.createSpacing(1, 1, 0, false, 0)
        }
        if (elementType1.let { it.isText() || it == TablesTokenTypes.PIPE } && elementType2 == TablesTokenTypes.PIPE) {
            val tableNode = TreeUtil.findParent(node1, TableElements.TABLE)
            if (tableNode != null) {
                val columnIndex = node1.getTableCellColumnIndex()
                val maxWidth = (tableNode.psi as Table).getColumnWidth(columnIndex)
                var spacingWidth = maxWidth - node1.text.trim { it <= ' ' }.length + 1
                if (elementType1 == TablesTokenTypes.PIPE) {
                    spacingWidth = spacingWidth + 2
                }
                return Spacing.createSpacing(spacingWidth, spacingWidth, 0, false, 0)
            }
        }
        return null
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        return ChildAttributes(Indent.getNoneIndent(), null)
    }

    override fun isIncomplete() = false

    override fun isLeaf() = myLeaf || subBlocks.size == 0

    override fun getNode() = myNode

    private fun IElementType.isText() = this == TableElements.TEXT || this == TableElements.QUOTED_TEXT

    private fun ASTNode.getTableCellColumnIndex(): Int {
        var node: ASTNode? = this
        var pipeCount = 0
        while (node != null) {
            if (node.elementType == TablesTokenTypes.PIPE) {
                pipeCount++
            }
            node = node.treePrev
        }
        return pipeCount - 1
    }
}