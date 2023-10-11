package de.configtables.intellij.formatter

import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.psi.formatter.DocumentBasedFormattingModel
import com.intellij.psi.impl.source.SourceTreeToPsiMap
import com.intellij.psi.impl.source.tree.TreeElement
import com.intellij.psi.impl.source.tree.TreeUtil

class TablesFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val file = formattingContext.containingFile
        val fileElement = TreeUtil.getFileElement((SourceTreeToPsiMap.psiElementToTree(formattingContext.psiElement) as TreeElement))
        val rootBlock = TablesBlock(fileElement)
        //FormattingModelDumper.dumpFormattingModel(rootBlock, 0, System.out);
        return DocumentBasedFormattingModel(rootBlock, file.project, formattingContext.codeStyleSettings, file.fileType, file)
    }
}