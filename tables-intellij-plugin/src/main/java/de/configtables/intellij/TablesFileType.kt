package de.configtables.intellij

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.IconManager
import java.nio.charset.StandardCharsets
import javax.swing.Icon

class TablesFileType private constructor() : LanguageFileType(TablesLanguage.INSTANCE) {
    override fun getName(): String {
        return TablesLanguage.INSTANCE.id
    }

    override fun getDescription(): String {
        return "Tables file"
    }

    override fun getDefaultExtension(): String {
        return "table"
    }

    override fun getIcon(): Icon {
        return IconManager.getInstance().getIcon("file-icon.svg", this.javaClass)
    }

    override fun getCharset(file: VirtualFile, content: ByteArray): String? {
        return StandardCharsets.UTF_8.name()
    }

    companion object {
        val INSTANCE = TablesFileType()
    }
}