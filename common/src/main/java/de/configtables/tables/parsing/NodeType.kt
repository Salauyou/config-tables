package de.configtables.tables.parsing

enum class NodeType(val debugName: String) {
    TABLE("table"),
    ROW("row"),
    CELL("cell"),
    TEXT("text"),
    QUOTED_TEXT("quoted text"),
}