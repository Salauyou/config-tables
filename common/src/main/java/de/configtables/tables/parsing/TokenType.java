package de.configtables.tables.parsing;

// Java, because used by generated CtFlexLexer
public enum TokenType {
  PIPE("PIPE"),
  TEXT("TEXT"),
  QUOTED_TEXT("QUOTED_TEXT"),
  CRLF("CRLF"),
  WS("WS"),
  COMMENT("COMMENT"),
  BAD_CHARACTER("BAD_CHARACTER"),
  ;

  public final String debugName;

  TokenType(String debugName) {
    this.debugName = debugName;
  }
}
