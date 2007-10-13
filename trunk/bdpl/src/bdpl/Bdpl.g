class BdplLexer extends Lexer;
options {
    charVocabulary = '\3'..'\377';
    k = 2;
}

protected
LETTER
    : ('a'..'z'|'A'..'Z')
    ;

protected
DIGIT
    : ('0'..'9')
    ;

COMMA : ',';
SEMICOLON : ';';
OPENBRACE : '{';
CLOSEBRACE : '}';
SQBRACKETOPEN : '[';
SQBRACKETCLOSE : ']';
OPENPAREN : '(';
CLOSEPAREN : ')';
STAR : '*';
ASSIGN : "=";
DOT : ".";
NEGATE : '~';

ID
    : LETTER(LETTER|DIGIT|'_')*
    ;

protected
BINPREFIX
    : '0''b'
    ;

protected
BINDIGITS
    : ('0'|'1')
    ;

protected
BINNUM
    : BINPREFIX(BINDIGITS)+
    ;

protected
DECNUM
    : (DIGIT)+
    ;

protected
HEXDIGITS
    : ('0'..'9' | 'a'..'f' | 'A'..'F')
    ;

protected
HEXPREFIX
    : '0'('x'|'X')
    ;

protected
HEXNUM
    : HEXPREFIX (HEXDIGITS)+
    ;

NUM
    : DECNUM
    | HEXNUM
    | BINNUM
    ;

WS
    : (' '
    | '\n'
    | '\r' {newline();}
    | '\t'
    ) {$setType(Token.SKIP);}
    ;

STRING
    : '"'! ( '"' '"'! | ~('"'))* '"'!
    ;

COMMENT
    : '/' '/' ((~'\n')* '\n' {newline();}) {$setType(Token.SKIP);}
    ;

class BdplParser extends Parser;
options{
    k = 2;
}

file
    : (stmt SEMICOLON)+ EOF
    ;

stmt
    : execstmt
    | declstmt
    ;

expr
    : NUM
    ;

execstmt
    : ID ASSIGN expr
    | "read" OPENPAREN (STRING|"stdin") COMMA ID CLOSEPAREN
    | "write" OPENPAREN (STRING|"stdout") COMMA ID CLOSEPAREN
    ;

declstmt
    : (("bit"|"byte"|"char")(SQBRACKETOPEN (NUM | STAR) SQBRACKETCLOSE)? ID) (validcheck)? (optionalcheck)?
    | structdecl
    ;

structdecl
    : "struct" ID OPENBRACE (declstmt SEMICOLON)* CLOSEBRACE
    ;

range
    : NUM DOT DOT NUM
    ;

csl
    : (NUM((COMMA NUM)*))?
    ;

validcheck
    : "valid" (OPENBRACE (csl|range) CLOSEBRACE) ("ok" stmtblock)? ("nok" stmtblock)?
    ;

optionalcheck
    : "optional" "on" (NEGATE)? ID
    ;

stmtblock
    : OPENBRACE (execstmt SEMICOLON)* CLOSEBRACE
    ;







