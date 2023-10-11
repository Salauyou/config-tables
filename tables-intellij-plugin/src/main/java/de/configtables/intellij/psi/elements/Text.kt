package de.configtables.intellij.psi.elements

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import groovy.json.StringEscapeUtils

class Text(node: ASTNode, val quoted: Boolean): ASTWrapperPsiElement(node) {

    fun unescaped() = node.text.let {
        when {
            quoted && it.startsWith('\'') -> it.substring(1, it.length - 1).replace("''", "'")
            quoted && it.startsWith('"') -> StringEscapeUtils.unescapeJava(it.substring(1, it.length - 1))
            else -> it
        }
    }
}
