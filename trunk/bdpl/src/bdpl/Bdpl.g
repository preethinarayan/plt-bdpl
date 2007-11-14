class BdplLexer extends Lexer;
options {
    charVocabulary = '\3'..'\377';
    k = 3;
}

protected
LETTER
    : ('a'..'z'|'A'..'Z')
    ;

protected
DIGIT
    : ('0'..'9')
    ;

protected
UNDERSCORE
    : '_'
    ;

SEMICOLON : ';';
TERMINATOR : ":-)";
COMMA : ',';
OPENBRACE : '{';
CLOSEBRACE : '}';
SQBRACKETOPEN : '[';
SQBRACKETCLOSE : ']';
OPENPAREN : '(';
CLOSEPAREN : ')';
STAR : '*';
SLASH : '/';
ASSIGN : '=';
DOT_DOT : "..";
DOT : '.';
NEGATE : '~';
NOT : '!';
MINUS : '-' ;
PLUS : '+';
PERCENT : '%';
SET : "=>";
EQUALITY : "==";
INEQUALITY : "!=";
LLSH : "<<";
LRSH : ">>";
ALSH : "<<<";
ARSH : ">>>";
GT : ">";
LT : "<";
GTE : ">=";
LTE : "<=";
ROL : "<-<";
ROR : ">->";
QUESTION :'?';
APPEND : "<-";
INDEX : "$#";
BYTEOFFSET : "$";
LAND : "&&";
BAND : "&";

ID
    : (LETTER | UNDERSCORE)(LETTER|DIGIT|'_')*
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
    k = 1;
    buildAST = true;
}

tokens{
    STATEMENTS;
}

file
    : (stmt)+ EOF!
        {#file = #([STATEMENTS],file);}
    ;

stmt
    : declstmt
    | execstmt
    ;

expr
    : (term ASSIGN^ tern_expr) => assign_expr
    | (term APPEND^ tern_expr) => append_expr
    | tern_expr
    ;

append_expr
    : term APPEND^ tern_expr
    ;

assign_expr
    : term ASSIGN^ tern_expr
    ;

execstmt
    : expr ";"!
    | stmtblock
    | "if"^ "("! expr ")"! execstmt (options {greedy=true;}: "else"! execstmt)?
    | "for"^ "("! (expr)? ";"! (expr)? ";"! (expr)? ")"! execstmt
    | "read"^ "("! (STRING|"stdin") ","! ID ")"! ";"!
    | "write"^ "("! (STRING|"stdout"|"stderr") ","! ID ")"! ";"!
    | "set"^ "("! STRING "=>"! STRING ","! ID ")"! ";"!
    | "exit"^ "("! STRING ")"! ";"!
    | "print"^ "("! STRING ")"! ";"!
    ;

stmtblock
    : "{"! (execstmt)* "}"!
    ;

declstmt
    : ("bit"^ | "byte"^ | "int"^ | "float"^ | "double"^ | ("type"^ "struct"! ID) | ("struct"^ ID "{"! (declstmt)* "}"!)) ("[" (expr|STAR) "]")? list_of_ids (valid_check)? (optional_check)? ";"!
    | "file"^ ID (","! ID)* ";"!
    ;

range_list
    : (range_element)("," range_element)*
    ;

range_element
    : (expr)(DOT_DOT expr)?
    ;

list_of_ids
    : (single_id)(COMMA single_id)*
    ;

single_id
    : ID ("(" initializer ")")? ("fieldsize" expr)?
    ;

valid_check
    : "valid"^ ("{"! range_list "}"!) ("ok"^ stmtblock)? ("nok"^ stmtblock)?
    ;

optional_check
    : "optional" "on" expr
    ;

initializer
    : (STRING SET STRING)(COMMA STRING SET STRING)*
    ;

tern_expr
    : lor_expr(QUESTION tern_expr COLON tern_expr)?
    ;

lor_expr
    : and_expr ("||"^ and_expr)*
    ;

and_expr
    : bior_expr (LAND^ bior_expr)*
    ;

bior_expr
    : beor_expr ("|"^ beor_expr)*
    ;

beor_expr
    : band_expr ("^" band_expr)*
    ;

band_expr
    : eq_expr (BAND eq_expr)*
    ;

eq_expr
    : comp_expr ((EQUALITY^ | INEQUALITY^) comp_expr)*
    ;

comp_expr
    : sh_expr ((GT^ | LT^ | GTE^ | LTE^) sh_expr)*
    ;

sh_expr
    : sum_expr ((LLSH^ | LRSH^ | ALSH^ | ARSH^ | ROL^ | ROR^) sum_expr)*
    ;

sum_expr
    : mult_expr ((PLUS^ | MINUS^) mult_expr)*
    ;

mult_expr
    : not_expr ((STAR^ | SLASH^ | PERCENT^) not_expr)*
    ;

not_expr
    : ((NEGATE^ | NOT^)? child_term)
    ;

child_term 
    : term
    | "("! expr ")"!
    | INDEX^ object
    | "#"^ lvalue_term
    | NUM
    ;

term
    : lvalue_term ("."^ lvalue_term)*
    | BYTEOFFSET^ lvalue_term
    ;

lvalue_term
    : object("[" (range_list|STAR) "]")?
    ;

object 
 	: ID
 	;

