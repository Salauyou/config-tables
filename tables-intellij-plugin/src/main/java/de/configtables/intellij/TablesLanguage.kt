package de.configtables.intellij

import com.intellij.lang.Language

class TablesLanguage : Language("Tables", "text/tables") {
    companion object {
        val INSTANCE = TablesLanguage()
    }
}