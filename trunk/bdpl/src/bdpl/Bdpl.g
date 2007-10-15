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

SEMICOLON : ';';
COMMA : ',';
OPENBRACE : '{';
CLOSEBRACE : '}';
SQBRACKETOPEN : '[';
SQBRACKETCLOSE : ']';
OPENPAREN : '(';
CLOSEPAREN : ')';
STAR : '*';
ASSIGN : '=';
DOT_DOT : "..";
DOT : '.';
NEGATE : '~';
MINUS : '-' ;
PLUS : '+';
PERCENT : "%";

// TODO : make a list of all operators here
// apparently, antlr sucks :-)


// why dont we have id's starting with underscores ?
// it would be good to follow the covention that fake 
// struct variables (the ones you declared cause it;s
// mandatory) could start with an underscore.

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
    : declstmt
    | execstmt
    ;

expr
    : tern_expr
    ;

execstmt
    : expr ";"
    | ID ASSIGN expr ";"
    | stmtblock
    | "if" "(" expr ")" execstmt (options {greedy=true;}: "else" execstmt)?
    | "for" "(" (expr)? ";" (expr)? ";" (expr)? ")" execstmt
    | "read" "(" (STRING|"stdin") "," ID ")" ";"
    | "write" "(" (STRING|"stdout"|"stderr") "," ID ")" ";"
    ;

stmtblock
    : "{" (execstmt)* "}"
    ;

declstmt
    : basic_type (array)? list_of_ids (valid_check)? (optional_check)? ";"
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
    : "type struct" ID
    ;

array
    : "[" (range_list|"*") "]"
    ;

range_list
    : (range_element)("," range_element)*
    ;

range_element
    : (expr)(DOT_DOT expr)?
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

tern_expr
    : lor_expr("?" tern_expr ":" tern_expr)?
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
    : (("&&" bior_expr) and_exprt)?
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
    : blsh_expr band_exprt
    ;

band_exprt
    : (("&" blsh_expr) band_exprt)?
    ;

blsh_expr
    : bash_expr blsh_exprt
    ;

blsh_exprt
    : (("<<" bash_expr | ">>" bash_expr) blsh_exprt)?
    ;

bash_expr
    : sum_expr bash_exprt
    ;

bash_exprt
    : (("<<<" sum_expr | ">>>" sum_expr) bash_exprt)?
    ;

sum_expr
    : mult_expr sum_exprt
    ;

sum_exprt
    : (("+" mult_expr | "-" mult_expr) sum_exprt)?
    ;

mult_expr
    : not_expr mult_exprt
    ;

mult_exprt
    : (("*" not_expr | "/" not_expr | "%" not_expr) mult_exprt)?
    ;

//
// Is this correct?
// do we need 2 not operators (i presume logical/bitwise)  - akshay
//
not_expr
    : ("~" child_term | "!" child_term | child_term)
    ;

child_term 
    : object
    | NUM
    ;

// this is really hairy stuff
// gotta double check that this is correct - akshay
// note : array of arrays are allowed by this grammer

object 
 	: ID object_t
 	;
object_t
	: array object_t
	| "." ID object_t
	| // nothing
	; 	