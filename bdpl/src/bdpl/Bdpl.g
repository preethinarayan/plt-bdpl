/*
 * This is the BDPL ANTLR grammar file
 *
 */

///////////////////////////////////////////////////////////////////////////////
//                                                                           //
//                                    LEXER                                  //
//                                                                           //
///////////////////////////////////////////////////////////////////////////////
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
    | '\n' {newline();}
    | ('\r' '\n') => '\r' '\n' {newline();}
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

///////////////////////////////////////////////////////////////////////////////
//                                                                           //
//                                    PARSER                                 //
//                                                                           //
///////////////////////////////////////////////////////////////////////////////
class BdplParser extends Parser;
options{
    k = 1;
    buildAST = true;
}

tokens{
    PROG;
}

program
    : (stmt)+ EOF!
        {#program = #([PROG,"PROG"], #program);}
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
    | "break" ";"!
    | "continue" ";"!
    ;

stmtblock
    : "{"! (execstmt)* "}"!
    ;

declstmt
    : ("bit"^ 
        | "byte"^ 
        | "int"^ 
        | "float"^ 
        | "double"^ 
        | ("type"^ "struct"! ID) 
        | ("struct"^ ID "{"! (declstmt)* "}"!)
        )("[" (expr|STAR) "]")? list_of_ids (valid_check)? (optional_check)? ";"!
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

///////////////////////////////////////////////////////////////////////////////
//                                                                           //
//                                TREE WALKER                                //
//                                                                           //
///////////////////////////////////////////////////////////////////////////////
class BdplTreeParser extends TreeParser;
options{
}

{
    String r = new String("Null");
    String a = new String();
    String b = new String();
}

program
    : #(PROG (r=stmt {System.out.println(r);})+)
    ;

stmt returns [String r]
{
    r = new String();
}
    : "if" {r="if";}
    | "file" {r="file";}
    | "for" {r="for";}
    | "break" {r="break";}
    | "continue" {r="continue";}
    | "struct" {r="struct";}
    | "type" {r="type";}
    | "read" {r="read";}
    | "write" {r="write";}
    | "print" {r="print";}
    | #(DOT       a=stmt b=stmt {})
    | #(PLUS      a=stmt b=stmt {r="+";System.out.println(r);})
    | #(MINUS     a=stmt b=stmt {})
    | #(STAR      a=stmt b=stmt {})
    | #(SLASH     a=stmt b=stmt {})
    | #(PERCENT   a=stmt b=stmt {})
    | #(LLSH      a=stmt b=stmt {})
    | #(LRSH      a=stmt b=stmt {})
    | #(ALSH      a=stmt b=stmt {})
    | #(ARSH      a=stmt b=stmt {})
    | #(GT        a=stmt b=stmt {})
    | #(LT        a=stmt b=stmt {})
    | #(GTE       a=stmt b=stmt {})
    | #(LTE       a=stmt b=stmt {})
    | #(ROL       a=stmt b=stmt {})
    | #(ROR       a=stmt b=stmt {})
    | #(LAND      a=stmt b=stmt {}) 
    | #(LOR       a=stmt b=stmt {}) 
    | #(BAND      a=stmt b=stmt {})
    | #(BIOR      a=stmt b=stmt {})
    | #(BEOR      a=stmt b=stmt {})
    | #(APPEND    a=stmt b=stmt {})
    | #(ASSIGN    a=stmt b=stmt {r="=";})
    | #(id:ID {r=id.getText();System.out.println(r);})
;
