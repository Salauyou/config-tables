package de.configtables.intellij.psi.elements

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiComment
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.startOffset
import de.configtables.intellij.psi.elements.TableElements.Companion.isPipe

class Row(node: ASTNode) : ASTWrapperPsiElement(node) {

    fun getCellWidth(index: Int): Int {
        val cells = getCells()
        return when {
            index >= cells.size -> 0
            else -> cells[index].text.trim().length
        }
    }

    fun getCells(): List<Text> = PsiTreeUtil.getChildrenOfAnyType(this, Text::class.java)

    fun getCellIndexAtOffset(offset: Int): Int {
        var index = -1
        var node = firstChild
        while (node != null && node.startOffset < offset) {
            when {
                node is PsiComment -> { index = -1; break }
                node.isPipe() -> ++index
            }
            node = node.nextSibling
        }
        return index
    }

    fun isHeader() = PsiTreeUtil.findChildOfType(parent, Row::class.java) === this
}