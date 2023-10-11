package de.configtables.intellij.psi.elements

import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import de.configtables.intellij.TablesElementType
import de.configtables.intellij.TablesTokenTypes
import de.configtables.tables.parsing.NodeType

interface TableElements {
    companion object {
        val TABLE = TablesElementType(NodeType.TABLE)
        val ROW = TablesElementType(NodeType.ROW)
        val TEXT = TablesElementType(NodeType.TEXT)
        val QUOTED_TEXT = TablesElementType(NodeType.QUOTED_TEXT)

        fun PsiElement?.isPipe() = this is LeafPsiElement && this.node.elementType == TablesTokenTypes.PIPE
    }
}