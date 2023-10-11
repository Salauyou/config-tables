package de.configtables.intellij.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import de.configtables.intellij.TablesFileType

class TablesFile(viewProvider: FileViewProvider, language: Language) : PsiFileBase(viewProvider, language) {
    override fun getFileType(): FileType {
        return TablesFileType.INSTANCE
    }
}