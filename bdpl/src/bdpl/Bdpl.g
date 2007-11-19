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
SQBROPEN : '[';
SQBRCLOSE : ']';
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
BYTEOFFSET : '$';
POUND : '#';
LAND : "&&";
BAND : '&';
BIOR : '|';
BEOR : '^';
LOR : "||";

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
    k = 2;
    buildAST = true;
}

tokens{
    ARRAY;
    BODY;
    IDEN;
    INITLIST;
    OPTIONAL;
    PROG;
    RANGES;
    TAG;
    VALID;
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
    : expr SEMICOLON!
    | stmtblock
    | "if"^ "("! expr ")"! execstmt (options {greedy=true;}: "else"! execstmt)?
    | "for"^ "("! (expr)? SEMICOLON! (expr)? SEMICOLON! (expr)? ")"! execstmt
    | "read"^ "("! (STRING|"stdin") COMMA! ID ")"! SEMICOLON!
    | "write"^ "("! (STRING|"stdout"|"stderr") COMMA! ID ")"! SEMICOLON!
    | "set"^ "("! STRING "=>"! STRING COMMA! ID ")"! SEMICOLON!
    | "exit"^ "("! STRING ")"! SEMICOLON!
    | "print"^ "("! STRING ")"! SEMICOLON!
    | "break" SEMICOLON!
    | "continue" SEMICOLON!
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
        | (("struct"^) tag struct_body)
        )(array_defn)? list_of_ids (valid_check)? (optional_check)? SEMICOLON!
    | "file"^ ID (COMMA! ID)* SEMICOLON!
    ;

tag
    : ID
        {#tag = #([TAG,"TAG"],#tag);}
    ;

array_defn
    : (SQBROPEN! (expr|STAR) SQBRCLOSE!)
        {#array_defn = #([ARRAY,"ARRAY"],#array_defn);}
    ;

struct_body
    : ("{"! (declstmt)* "}"!)
        {#struct_body = #([BODY,"BODY"],#struct_body);}
    ;

range_list
    : (range_element)(COMMA! range_element)*
        {#range_list = #([RANGES,"RANGES"],#range_list);}
    ;

range_element
    : (expr)(DOT_DOT^ expr)?
    ;

list_of_ids
    : (single_id)(COMMA! single_id)*
    ;

single_id
    : ID ("("! init_list ")"!)? (size)?
        {#single_id = #([IDEN,"IDEN"],#single_id);}
    ;

size
    : ("("! "fieldsize"^ expr ")"!)
    ;

valid_check
    : valid_values (ok_block)? (nok_block)?
        {#valid_check = #([VALID,"VALID"],#valid_check);}
    ;

valid_values
    : "valid"! ("{"! range_list "}"!)
    ;

ok_block
    : ("ok"^ stmtblock)
    ;

nok_block
    : ("nok"^ stmtblock)
    ;

optional_check
    : "optional"! "on"! expr
        {#optional_check = #([OPTIONAL,"OPTIONAL"],#optional_check);}
    ;

init_list
    : (initializer)(COMMA! initializer)*
        {#init_list = #([INITLIST,"INITLIST"],#init_list);}
    ;

initializer
    : (STRING "=>"^ STRING)
    ;

tern_expr
    : lor_expr(QUESTION tern_expr COLON tern_expr)?
    ;

lor_expr
    : and_expr (LOR^ and_expr)*
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
    | POUND^ lvalue_term
    | NUM
    ;

term
    : lvalue_term (DOT^ lvalue_term)*
    | BYTEOFFSET^ lvalue_term
    ;

lvalue_term
    : object(SQBROPEN (range_list|STAR) SQBRCLOSE)?
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
    DataNodeBit r = new DataNodeBit();
    DataNodeBit a = new DataNodeBit();
    DataNodeBit b = new DataNodeBit();
    String x;
}

program
    : #(PROG (r=stmt {})+)
    ;

stmt returns [DataNodeBit r]
{
    r = new DataNodeBit();
    x = new String("");
}
    : "if"                        {}
    | "file"                      {}
    | "for"                       {}
    | "break"                     {}
    | "continue"                  {}
    | #("struct" x=id ((OPENBRACE (stmt)* CLOSEBRACE))?                       {})
    | "byte"
    | "bit"                       {}
    | "int"                       {}
    | "float"                     {}
    | "double"                    {}
    | "type"                      {}
    | "read"                      {}
    | "write"                     {}
    | "print"                     {}
    | #(DOT_DOT     a=stmt b=stmt {})
    | #(SQBROPEN    a=stmt b=stmt {})
    | #(BYTEOFFSET  a=stmt        {})
    | #(DOT         a=stmt b=stmt {})
    | #(PLUS        a=stmt b=stmt {})
    | #(MINUS       a=stmt b=stmt {})
    | #(STAR        a=stmt b=stmt {})
    | #(SLASH       a=stmt b=stmt {})
    | #(PERCENT     a=stmt b=stmt {})
    | #(LLSH        a=stmt b=stmt {})
    | #(LRSH        a=stmt b=stmt {})
    | #(ALSH        a=stmt b=stmt {})
    | #(ARSH        a=stmt b=stmt {})
    | #(GT          a=stmt b=stmt {})
    | #(LT          a=stmt b=stmt {})
    | #(GTE         a=stmt b=stmt {})
    | #(LTE         a=stmt b=stmt {})
    | #(ROL         a=stmt b=stmt {})
    | #(ROR         a=stmt b=stmt {})
    | #(LAND        a=stmt b=stmt {}) 
    | #(LOR         a=stmt b=stmt {}) 
    | #(BAND        a=stmt b=stmt {})
    | #(BIOR        a=stmt b=stmt {})
    | #(BEOR        a=stmt b=stmt {})
    | #(APPEND      a=stmt b=stmt {})
    | #(ASSIGN      a=stmt b=stmt {})
    | #(id:ID {/* Look inside the symbol table and get the data node for this id */})
;

protected
id returns [String r]
{
    r = new String("");
}
    :#(id:ID {r=id.getText();})
    ;
