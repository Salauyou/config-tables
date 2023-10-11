package de.configtables.intellij.editor

import com.google.common.html.HtmlEscapers
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ui.TextTransferable
import de.configtables.intellij.psi.elements.Table

class CopyAsTableAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = e.getPsiTable() != null
        super.update(e)
    }

    override fun actionPerformed(e: AnActionEvent) {
        // TODO: process selection
        val table = e.getPsiTable() ?: return
        val plainText = StringBuilder()
        val htmlText = StringBuilder().append("<table style=\"border-collapse: collapse;\">")
        val rows = table.getRows()
        rows.forEachIndexed { rowIdx, row ->
            val cells = row.getCells()
            htmlText.append(when (rowIdx) {
                0 -> "<thead>"
                1 -> "<tbody><tr>"
                else -> "<tr>"
            })
            cells.forEachIndexed { cellIdx, cell ->
                val text = cell.unescaped()
                htmlText
                    .append(when (rowIdx) {
                        0 -> "<th>"
                        else -> "<td>"
                    })
                    .append(HTML_ESCAPER.escape(text))
                    .append(when (rowIdx) {
                        0 -> "</th>"
                        else -> "</td>"
                    })
                plainText.append(text.replace(WS_REGEX, " "))
                if (cellIdx < cells.size - 1) {
                    plainText.append('\t')
                }
            }
            htmlText.append(when (rowIdx) {
                0 -> "</thead>"
                rows.size - 1 -> "</tr></tbody>"
                else -> "</tr>"
            })
            if (rowIdx < rows.size - 1) {
                plainText.append(System.lineSeparator())
            }
        }
        htmlText.append("</table>")
        val clip = TextTransferable(htmlText, plainText)
        CopyPasteManager.getInstance().setContents(clip)
    }

    private fun AnActionEvent.getPsiTable(): Table? = when {
        getData(CommonDataKeys.EDITOR) == null -> null
        getData(CommonDataKeys.PSI_FILE) == null -> null
        else -> PsiTreeUtil.findChildOfType(getRequiredData(CommonDataKeys.PSI_FILE), Table::class.java)
    }

    companion object {
        val WS_REGEX = Regex("\\s+")
        val HTML_ESCAPER = HtmlEscapers.htmlEscaper()
    }
}