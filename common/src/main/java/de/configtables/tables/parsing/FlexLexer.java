package de.configtables.tables.parsing;

import java.io.IOException;

// Java, because used by generated CtFlexLexer
public interface FlexLexer {
  void yybegin(int state);
  int yystate();
  int getTokenStart();
  int getTokenEnd();
  TokenType advance() throws IOException;
  void reset(CharSequence buf, int start, int end, int initialState);
}