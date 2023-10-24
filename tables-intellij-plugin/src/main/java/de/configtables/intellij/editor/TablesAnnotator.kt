package de.configtables.intellij.editor

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import de.configtables.intellij.psi.elements.Row
import de.configtables.intellij.psi.elements.Text

class TablesAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is Text || element.textRange.isEmpty) {
            return
        }
        val row = PsiTreeUtil.getParentOfType(element, Row::class.java) ?: return
        if (row.isHeader()) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.textRange)
                .textAttributes(DefaultLanguageHighlighterColors.CONSTANT)
                .create();
        }
    }
}