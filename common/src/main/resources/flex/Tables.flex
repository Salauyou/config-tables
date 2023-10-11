package de.configtables.tables.parsing;
%%

%public
%class TablesFlexLexer
%implements FlexLexer
%unicode
%function advance
%type TokenType
%eof{  return;
%eof}

CRLF = [\r\n]+
WHITE_SPACE = [ \b\t\f]+
COMMENT = # [^\r\n]*
PIPE = \|
QUOTE = [\"']
FIRST_CHAR = [^| #\b\t\f\r\n\"']
WORD = ([^| #\b\t\f\r\n] #*)+
TEXT = {FIRST_CHAR} {WORD}? ({WHITE_SPACE} {WORD})*
QUOTED_TEXT =
    // "-quoted JSON string: https://www.json.org/json-en.html
    ( \" ( [^\\\"\b\t\f\r\n] | ("\\" ([\"\\btfrn/]|(u[0-9a-fA-F]{4}) ) ) )* \" )
    // '-quoted string
    | (' ([^'\b\t\f\r\n] | "''")* ')

%state ERROR
%%

<YYINITIAL> {
    {PIPE}          { return TokenType.PIPE; }
    {TEXT}          { return TokenType.TEXT; }
    {QUOTED_TEXT}   { return TokenType.QUOTED_TEXT; }
    {PIPE}          { return TokenType.PIPE; }

    // unclosed quote makes ERROR state until line break
    // to force Intellij plugin re-tokenize starting from
    // opening quote when something is changed
    {QUOTE}         { yybegin(ERROR); return TokenType.BAD_CHARACTER; }
}

<ERROR> {CRLF}      { yybegin(YYINITIAL); return TokenType.CRLF; }

{COMMENT}           { return TokenType.COMMENT; }
{WHITE_SPACE}       { return TokenType.WS; }
{CRLF}              { return TokenType.CRLF; }
[^]                 { return TokenType.BAD_CHARACTER; }