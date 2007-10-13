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
    : (stmt)+ EOF
    ;

stmt
    : execstmt
    | declstmt
    ;

expr
    : NUM
    | ID (ASSIGN expr)?
    ;

execstmt
    : expr SEMICOLON
    | stmtblock
    | "if" OPENPAREN expr CLOSEPAREN execstmt (options {greedy=true;}:"else" execstmt)?
    | "for" OPENPAREN (expr)? SEMICOLON (expr)? SEMICOLON (expr)? CLOSEPAREN execstmt
    | "read" OPENPAREN (STRING|"stdin") COMMA ID CLOSEPAREN SEMICOLON
    | "write" OPENPAREN (STRING|"stdout"|"stderr") COMMA ID CLOSEPAREN SEMICOLON
    ;

range
    : NUM DOT DOT NUM
    ;

csl
    : (NUM((COMMA NUM)*))?
    ;

stmtblock
    : OPENBRACE (execstmt)* CLOSEBRACE
    ;

declstmt
    : basic_type (array)? list_of_ids valid_check optional_check ";"
    ;

basic_type
    : "bit"
    | "byte"
    | "int"
    | "float"
    | "double"
    | struct_type
    | struct_defn
    ;

struct_defn
    : "struct" ID "{" (declstmt)* "}"
    ;

struct_type
    : "type struct" ID
    ;

array
    : "[" (range_list|"*") "]"
    ;

range_list
    : (range_element)("," range_element)*
    ;

range_element
    : (expr)(".." expr)?
    ;

list_of_ids
    : (single_id)("," single_id)*
    ;

single_id
    : ID ("(" initializer ")")? ("fieldsize" expr)?
    ;

valid_check
    : "valid" ("{" range_list "}") ("ok" stmtblock)? ("nok" stmtblock)?
    ;

optional_check
    : "optional" "on" expr
    ;

initializer
    : (STRING "=>" STRING)("," STRING "=>" STRING)*
    ;



