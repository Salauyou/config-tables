package de.configtables.intellij.editor

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider
import de.configtables.intellij.TablesLanguage
import de.configtables.intellij.psi.elements.Row
import de.configtables.intellij.psi.elements.Table
import de.configtables.intellij.psi.elements.TableElements.Companion.isPipe

class TablesBreadcrumbsProvider : BreadcrumbsProvider {

    override fun getLanguages() = arrayOf(TablesLanguage.INSTANCE)

    override fun acceptElement(element: PsiElement) = element is Row || (element.parent is Row && !element.isPipe())

    override fun getElementInfo(element: PsiElement): String {
        if (element is Row) {
            val cells = element.getCells()
            var textCells = cells.subList(0, Math.min(cells.size, 3)).map {
                it.node.text.orEmpty()
            }
            if (cells.size > 3) {
                textCells += "…"
            }
            return textCells.joinToString("—")
        } else {
            val row = PsiTreeUtil.getParentOfType(element, Row::class.java) ?: return ""
            val table = PsiTreeUtil.getParentOfType(row, Table::class.java) ?: return ""
            val index = row.getCellIndexAtOffset(element.textRange.startOffset)
            if (index < 0) {
                return ""
            }
            return table.getHeaderCell(index).orEmpty()
        }
    }

    private fun String?.orEmpty() = when {
        isNullOrBlank() -> "<empty>"
        else -> this
    }
}