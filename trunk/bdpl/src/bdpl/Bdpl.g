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
}

file
    : (stmt)+ EOF
    ;

stmt
    : declstmt
    | execstmt
    ;

expr
    : (term ASSIGN tern_expr) => assign_expr
    | (term APPEND tern_expr) => append_expr
    | tern_expr
    ;

append_expr
    : term APPEND tern_expr
    ;

assign_expr
    : term ASSIGN tern_expr
    ;

execstmt
    : expr ";"
    | stmtblock
    | "if" "(" expr ")" execstmt (options {greedy=true;}: "else" execstmt)?
    | "for" "(" (expr)? ";" (expr)? ";" (expr)? ")" execstmt
    | "read" "(" (STRING|"stdin") "," ID ")" ";"
    | "write" "(" (STRING|"stdout"|"stderr") "," ID ")" ";"
    | "set" "(" STRING "=>" STRING "," ID ")" ";"
    | "exit" "(" STRING ")" ";"
    | "print" "(" STRING ")" ";"
    ;

stmtblock
    : "{" (execstmt)* "}"
    ;

declstmt
    : basic_type (array)? list_of_ids (valid_check)? (optional_check)? ";"
    | "file" ID ("," ID)* ";"
    ;

basic_type
    : "bit"
    | "byte"
    | "int"
    | "float"
    | "double"
    | "char" // this has been added only until we change the BdplTest file
    | struct_type
    | struct_defn
    ;

struct_defn
    : "struct" ID "{" (declstmt)* "}"
    ;

struct_type
    : "type" "struct" ID
    ;

array
    : "[" (range_list|STAR) "]"
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
    : "valid" ("{" range_list "}") ("ok" stmtblock)? ("nok" stmtblock)?
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
    : and_expr lor_exprt
    ;

lor_exprt
    : (("||" and_expr) lor_exprt)?
    ;

and_expr
    : bior_expr and_exprt
    ;

and_exprt
    : ((LAND bior_expr) and_exprt)?
    ;

bior_expr
    : beor_expr bior_exprt
    ;

bior_exprt
    : (("|" beor_expr) bior_exprt)?
    ;

beor_expr
    : band_expr beor_exprt
    ;

beor_exprt
    : (("^" band_expr) beor_exprt)?
    ;

band_expr
    : eq_expr band_exprt
    ;

band_exprt
    : ((BAND eq_expr) band_exprt)?
    ;

eq_expr
    : comp_expr eq_exprt
    ;

eq_exprt
    :((EQUALITY comp_expr)
    | (INEQUALITY comp_expr) eq_exprt)?
    ;

comp_expr
    : sh_expr comp_exprt
    ;

comp_exprt
    : (  (GT sh_expr)
        |(LT sh_expr)
        |(GTE sh_expr)
        |(LTE sh_expr) comp_exprt)?
    ;

sh_expr
    : sum_expr sh_exprt
    ;

sh_exprt
    : (  (LLSH sum_expr)
        |(LRSH sum_expr)
        |(ALSH sum_expr)
        |(ARSH sum_expr)
        |(ROL sum_expr)
        |(ROR sum_expr) sh_exprt)?
    ;

sum_expr
    : mult_expr sum_exprt
    ;

sum_exprt
    : ((PLUS mult_expr | MINUS mult_expr) sum_exprt)?
    ;

mult_expr
    : not_expr mult_exprt
    ;

mult_exprt
    : ((STAR not_expr | SLASH not_expr | PERCENT not_expr) mult_exprt)?
    ;

not_expr
    : (NEGATE child_term | NOT child_term | child_term)
    ;

child_term 
    : term
    | "(" expr ")"
    | INDEX object
    | "#" lvalue_term
    | NUM
    ;

term
    : lvalue_term termt
    | BYTEOFFSET lvalue_term
    ;

termt
    : ("." lvalue_term termt)?
    ;

lvalue_term
    : object("[" (range_list|STAR) "]")?
    ;

object 
 	: ID
 	;

