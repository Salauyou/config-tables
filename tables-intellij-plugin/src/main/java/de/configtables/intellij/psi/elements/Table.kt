package de.configtables.intellij.psi.elements

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

class Table(node: ASTNode) : ASTWrapperPsiElement(node) {

    fun getRows(): List<Row> = PsiTreeUtil.getChildrenOfAnyType(this, Row::class.java)

    fun getColumnWidth(index: Int): Int = getRows().maxOf { it.getCellWidth(index) }

    fun getHeaderCell(index: Int): String? = PsiTreeUtil.getChildOfType(this, Row::class.java)?.let {
        it.getCells().let {
            when {
                index >= it.size -> null
                else -> it[index].text
            }
        }
    }
}