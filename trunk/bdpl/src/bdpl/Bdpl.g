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
{import java.util.*; }
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
    NULL;
    ARRAY_SIZE;
    DECL;
    STRUCT_NAME;
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
    :   (   "bit"^ 
            | "byte"^ 
            | "int"^
            | "float"^ 
            | "double"^ 
            | ("type"^ "struct"! ID) 
            | (("struct"^) tag struct_body)
        ) 
        ( {#declstmt=#([ARRAY,"ARRAY"],declstmt);} array_defn )? 
        list_of_ids
        (valid_check)? 
        (optional_check)? 
        SEMICOLON!
    | "file"^ ID (COMMA! ID)* SEMICOLON!
    ;

tag
    : ID
        {#tag = #([TAG,"TAG"],#tag);}
    ;

array_defn
    : (SQBROPEN! (expr|STAR) SQBRCLOSE!)
        {#array_defn = #([ARRAY_SIZE,"ARRAY_SIZE"],#array_defn);}
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

//
// A program is a collection of declarations and statements
// A declaration is a subtree which has one of the following as a root node
//   ARRAY
//   int
//   byte
//   bit
//   type 
// A statement may either be an expression or one of the following
//   if
//   for
//   read
//   write
//   set
//   print
//   break
//   continue
// For each of the above, the keyword itself forms the root of the
// subtree that constitutes the statement.
// A program returns nothing
// A statement returns nothing
// An expression returns an AbstractDataNode
//

class BdplTreeParser extends TreeParser;
{
    VariableSymbolTable varSymbTbl = new VariableSymbolTable();
    TypeSymbolTable typeSymbTbl = new TypeSymbolTable();
}

program
{
}
    : #(PROG ((stmts | decls)*))
    ;

stmts
{
    DataNodeAbstract r;
}
    : "if"                        {}
    | "for"                       {}
    | "break"                     {}
    | "continue"                  {}
    | "read"                      {}
    | "write"                     {}
    | "set"                       {}
    | "print"                     {}
    | r=expr                      {}
    ;

//
// Check the symbol table for this name
// If name exists, throw an exception
// Else, get a node of type int
// Populate the node with INITLIST info (AST)
// Populate the node with fieldsize info (AST)
//
decls
{
    String name;
}
    : #(ARRAY                     {})
    | #("int"
        (#(IDEN name=string (#(INITLIST {}))? (#("fieldsize" {}))?) 
            {
                if(varSymbTbl.contains(name))
                {
//                    throw new Exception("Redefinition of symbol "+name);
                }
                else
                {
                    try{
                        Type intType = typeSymbTbl.get("int");
                        DataNodeInt intNode = (DataNodeInt)intType.getDataNode();
                        varSymbTbl.insert(name,intNode);
                    }catch(Exception e){

                    }
                }
            })+
        {
            //
            // Need to do nothing here
            //
        }
       )
    | #("byte" 
        (#(IDEN name=string (#(INITLIST {}))? (#("fieldsize" {}))?) 
            {
                if(varSymbTbl.contains(name))
                {
//                    throw new Exception("Redefinition of symbol "+name);
                }
                else
                {
                    try{
                        Type byteType = typeSymbTbl.get("byte");
                        DataNodeByte byteNode = (DataNodeByte)byteType.getDataNode();
                        varSymbTbl.insert(name,byteNode);
                    }catch(Exception e){

                    }
                }
            })+                   
        {
            //
            // Need to do nothing here
            //
        }
        )
    | #("bit"                     
         (#(IDEN name=string (#(INITLIST {}))? (#("fieldsize" {}))?) 
            {
                if(varSymbTbl.contains(name))
                {
//                    throw new Exception("Redefinition of symbol "+name);
                }
                else
                {
                    try{
                        Type bitType = typeSymbTbl.get("bit");
                        DataNodeBit bitNode = (DataNodeBit)bitType.getDataNode();
                        varSymbTbl.insert(name,bitNode);
                    }catch(Exception e){

                    }
                }
            })+
        {
            //
            // Need to do nothing here
            //
        }
       )
    | #("struct" 
        {

        }
       )
    | #("type"                    {})
    ;

expr returns [DataNodeAbstract r]
{
    DataNodeAbstract a,b;
    r = new DataNodeBit();
    String y,z; 
}
    : #(DOT_DOT     a=expr b=expr {})
    | #(SQBROPEN    a=expr b=expr {})
    | #(BYTEOFFSET  a=expr        {})
    | #(DOT         a=expr b=expr {})
    | #(PLUS        a=expr b=expr {})
    | #(MINUS       a=expr b=expr {})
    | #(STAR        a=expr b=expr {})
    | #(SLASH       a=expr b=expr {})
    | #(PERCENT     a=expr b=expr {})
    | #(LLSH        a=expr b=expr {})
    | #(LRSH        a=expr b=expr {})
    | #(ALSH        a=expr b=expr {})
    | #(ARSH        a=expr b=expr {})
    | #(GT          a=expr b=expr {})
    | #(LT          a=expr b=expr {})
    | #(GTE         a=expr b=expr {})
    | #(LTE         a=expr b=expr {})
    | #(ROL         a=expr b=expr {})
    | #(ROR         a=expr b=expr {})
    | #(LAND        a=expr b=expr {}) 
    | #(LOR         a=expr b=expr {}) 
    | #(BAND        a=expr b=expr {})
    | #(BIOR        a=expr b=expr {})
    | #(BEOR        a=expr b=expr {})
    | #(APPEND      a=expr b=expr {})
    | #(ASSIGN      a=expr b=expr {})
    | #("=>"        y=string z=string {})
    | #(id:ID {/* Look inside the symbol table and get the data node for this id */})
    | #(num:NUM     {a=new DataNodeBit(Integer.parseInt(num.getText()));})
    ;

protected
id returns [String r]
    {
        r = new String("");
    }
    :#(id:ID {r=id.getText();})
    ;

protected
num returns [int n]
{
    n = 0;
}
    :#(num:NUM {n=Integer.parseInt(num.getText());})
    ;

protected
string returns [String r]
{
    r = new String("");
}
    :#(str:ID {r=str.getText();})
    ;
