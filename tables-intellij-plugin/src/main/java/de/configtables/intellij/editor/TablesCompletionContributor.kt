package de.configtables.intellij.editor

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import de.configtables.intellij.psi.elements.Row
import de.configtables.intellij.psi.elements.Table

class TablesCompletionContributor : CompletionContributor() {

    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().afterLeaf("|"),
            CellSuggestionProvider(),
        )
    }

    inner class CellSuggestionProvider : CompletionProvider<CompletionParameters>() {

        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val item = parameters.position
            val row = PsiTreeUtil.getParentOfType(parameters.position, Row::class.java) ?: return
            val table = PsiTreeUtil.getParentOfType(row, Table::class.java) ?: return
            val index = row.getCellIndexAtOffset(item.node.startOffset)
            if (index < 0) {
                return
            }
            val rows = table.getRows()
            if (rows.isEmpty() || rows[0] === row) {
                return
            }
            val columnName = table.getHeaderCell(index) ?: ""
            // collect other values in column, except header
            rows.subList(1, rows.size).asSequence()
                .filterNot { it === row }
                .map { it.getCells() }
                .mapNotNull {
                    when {
                        index >= it.size -> null
                        else -> it[index].text.takeUnless { it.isBlank() }
                    }
                }.distinct()
                .forEach {
                    result.addElement(LookupElementBuilder.create(it)
                            .withCaseSensitivity(true)
                            .withTypeText(columnName)
                            .withInsertHandler(cellInsertSuggestionHandler))
                }
        }
    }

    val cellInsertSuggestionHandler = InsertHandler { ctx: InsertionContext, _: LookupElement ->
        val text = ctx.file.findElementAt(ctx.startOffset) ?: return@InsertHandler
        val textRange = text.textRange
        if (PsiTreeUtil.getNextSiblingOfType(text.parent, LeafPsiElement::class.java) == null) {
            // last cell in a row: append '|' and move cursor to new cell
            val replacement = text.text + " | "
            val newCaretOffset = textRange.startOffset + replacement.length
            ctx.document.replaceString(textRange.startOffset, textRange.endOffset, replacement)
            ctx.editor.caretModel.moveToOffset(newCaretOffset)
            PsiDocumentManager.getInstance(ctx.project).commitDocument(ctx.document)
            ctx.reformat(textRange.startOffset, textRange.startOffset + replacement.length)
        } else {
            // middle of a row: just reformat
            ctx.reformat(textRange.startOffset, textRange.endOffset + 1)
        }
    }

    private fun InsertionContext.reformat(startOffset: Int, endOffset: Int) {
        CodeStyleManager.getInstance(project).reformatText(file, startOffset, endOffset)
    }

}