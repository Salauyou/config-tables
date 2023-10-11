package de.configtables.intellij

import com.intellij.psi.tree.IElementType
import de.configtables.tables.parsing.NodeType
import de.configtables.tables.parsing.TokenType

class TablesElementType(
        val tokenType: TokenType?, val nodeType: NodeType?, debugName: String,
) : IElementType(debugName, TablesLanguage.INSTANCE) {
    constructor(tokenType: TokenType) : this(tokenType, null, tokenType.debugName)
    constructor(nodeType: NodeType) : this(null, nodeType, nodeType.debugName)
}