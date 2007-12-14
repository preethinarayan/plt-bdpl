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
    BLOCK;
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
	LVALUE;
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
    | "print"^ "("! (STRING|expr) ")"! SEMICOLON!
    | "printstring"^ "("! (expr) ")"! SEMICOLON!
    | "break" SEMICOLON!
    | "continue" SEMICOLON!
    ;

stmtblock
    : "{"! (execstmt)* "}"!
        {#stmtblock = #([BLOCK,"BLOCK"],#stmtblock);}
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
    : (single_id) //(COMMA! single_id)*
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
    : lvalue
    | BYTEOFFSET^ lvalue_term
    ;
	
lvalue
    : lvalue_term (DOT! lvalue_term)*
        {#lvalue = #([LVALUE,"LVALUE"],#lvalue);}
	;

lvalue_term
    : object(SQBROPEN^ (tern_expr) SQBRCLOSE!)?
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
    TypeConverter typeConverter = new TypeConverter();
    TypeChecker typeChecker = new TypeChecker(typeSymbTbl,typeConverter);
    Arithmetic arith = new Arithmetic(typeChecker);
    Relational relate = new Relational(typeChecker);
    Logical logic = new Logical(typeChecker);
    Bitwise bitwise = new Bitwise(typeChecker);
    DataNodeAbstract r;
}

program throws Exception
{
}
    : #(PROG ((stmts 
    | (r=decls)
        {   
            if(r==null) 
            {
                System.out.println(" r is null !! \n");
                return ;
            }
            varSymbTbl.insert(r.get_name(),r);
        }
            
     )* // decls or stmnt
    )
        
    )
    ;

stmts throws Exception
{
    DataNodeAbstract r;
    DataNodeAbstract init;
}
    : #("if" r=expr thenpart:. (elsepart:.)?
        {
            //
            // VRB: To do
            // Check the type of 'r' for consistency
            //
            try{
                if(r.get_int_value() > 0){
                     stmts(#thenpart);
                }else{
                    if(null != elsepart){
                        stmts(#elsepart);
                    }else{
                        //
                        // Do nothing here. The else part is
                        // optional
                        //
                    }
                }
            }catch(Exception e){

            }    
        }
       )
    | #("for" init=expr cond:. incr:. body:.
        {
           while(expr(#cond).get_int_value()!=0){
               stmts(#body);
               expr(#incr);
           } 
        }
      )
    | #("break"                     {})
    | #("continue"                  {})
    | #("read"                      {})
    | #("write"                     {})
    | #("set"                       {})
    | #("print" {String str;} (((str = string) {System.out.print(str);} ) | (r = expr {System.out.println(r.print());})))
    | #("printstring" ((r = expr) {System.out.print(r.print(1));}))
    | #(BLOCK (stmts)*              {})
    | r=expr                        {}
    ;

//
// Check the symbol table for this name
// If name exists, throw an exception
// Else, get a node of type int
// Populate the node with INITLIST info (AST)
// Populate the node with fieldsize info (AST)
//
decls returns [DataNodeAbstract r=null] throws Exception
{
    String name;
    String id;
    //String array_size;
    //String type;
}
    : #(ARRAY  (type:.) (#(ARRAY_SIZE array_size:.)) (#(IDEN id=id (#(INITLIST {}))? (#("fieldsize" {}))?)
        {
            DataNodeAbstract dummy_node=null;
            if(type.getText().equals("int") || type.getText().equals("bit") || type.getText().equals("byte"))
            {
                dummy_node=typeSymbTbl.get(type.getText()).getDataNode();
            }
            else
            if(type.getText().startsWith("struct") || type.getText().startsWith("type") )
            {
                dummy_node=decls(#type);   
            }
            r=new DataNodeArray(dummy_node);
            r.set_name(id);
            System.out.println("returning array ! ");
        }
      ))

    | #("int"
        (#(IDEN name=id (#(INITLIST {}))? (#("fieldsize" {}))?) 
            {

                Type intType = typeSymbTbl.get("int");
                DataNodeInt intNode = (DataNodeInt)intType.getDataNode();
                intNode.set_name(name);
                r=intNode;

            })+
        {
            //
            // Need to do nothing here
            //
        }
       )
    | #("byte" 
        (#(IDEN name=id (#(INITLIST {}))? (#("fieldsize" {}))?) 
            {
                Type byteType = typeSymbTbl.get("byte");
                DataNodeByte byteNode = (DataNodeByte)byteType.getDataNode();
                byteNode.set_name(name);
                r=byteNode;
            })+                   
        {
            //
            // Need to do nothing here
            //
        }
        )
    | #("bit"                     
         (#(IDEN name=id (#(INITLIST {}))? (#("fieldsize" {}))?) 
            {
                Type bitType = typeSymbTbl.get("bit");
                DataNodeBit bitNode = (DataNodeBit)bitType.getDataNode();
                bitNode.set_name(name);
                r=bitNode;
            })+
        {
            //
            // Need to do nothing here
            //
        }
       )
    | #("struct"  (#(TAG name=id)) (body:.) 
        {
            VariableSymbolTable newST=new VariableSymbolTable();
            TypeSymbolTable newTT=new TypeSymbolTable();
            newST.set_parent(varSymbTbl);
            newTT.set_parent(typeSymbTbl);
            varSymbTbl=newST; // go into new scope
            


            typeSymbTbl=newTT; // new type symbol table for type definitions
            DataNodeStruct structNode = new DataNodeStruct();
            structNode.set_scope(varSymbTbl,typeSymbTbl);
            AST child=body.getFirstChild();
            while(child!=null)
            {
                        
                DataNodeAbstract cdn=decls(child);
                structNode.set_child_by_name(cdn.get_name(),cdn);
                child=child.getNextSibling();
            }
            structNode.set_type_name("struct:"+name);
            r=structNode;
            
            Type structType=new Type ("struct:"+name,body);
                    
            //insert this defn in the parent type st , and restore values
            typeSymbTbl=typeSymbTbl.get_parent();
            typeSymbTbl.insert("struct:"+name, structType);
            varSymbTbl=varSymbTbl.get_parent(); // go up a scope
            
        } 
        (#(IDEN id=id (#(INITLIST {}))? (#("fieldsize" {}))?)

        {
            r.set_name(id);
        } // IDEN
        )?
       )
    |  #("type" name=id
       {
            Type t=typeSymbTbl.get("struct:"+name);
            AST child=t.get_ast().getFirstChild();
            DataNodeStruct structNode = new DataNodeStruct();
            structNode.set_type_name("struct:"+name);
                
            VariableSymbolTable newST=new VariableSymbolTable();
            TypeSymbolTable newTT=new TypeSymbolTable();
            newST.set_parent(varSymbTbl);
            newTT.set_parent(typeSymbTbl);
                
            varSymbTbl=newST;
            typeSymbTbl=newTT;


            structNode.set_scope(varSymbTbl,typeSymbTbl);

            while(child!=null)
            {
                DataNodeAbstract cdn=decls(child);
                structNode.set_child_by_name(cdn.get_name(),cdn);
                child=child.getNextSibling();
            }
                
            
             r=structNode;
             typeSymbTbl=typeSymbTbl.get_parent();
             varSymbTbl=varSymbTbl.get_parent();
        }
        ((#(IDEN name=id (#(INITLIST {}))? (#("fieldsize" {}))?))?
        {
            r.set_name(name);
        }
       ))
    ;

expr returns [DataNodeAbstract r] throws Exception
{
    DataNodeAbstract a,b;
    r = new DataNodeBit();
    String y,z; 
}
    : #(DOT_DOT     a=expr b=expr {})
    | #(BYTEOFFSET  a=expr        {})
    | #(DOT         a=expr b=expr {})
    | #(PLUS        a=expr b=expr {r = arith.eval(PLUS,a,b);})
    | #(MINUS       a=expr b=expr {r = arith.eval(MINUS,a,b);})
    | #(STAR        a=expr b=expr {r = arith.eval(STAR,a,b);})
    | #(SLASH       a=expr b=expr {r = arith.eval(SLASH,a,b);})
    | #(PERCENT     a=expr b=expr {r = arith.eval(PERCENT,a,b);})
    | #(LLSH        a=expr b=expr {r = bitwise.eval(LLSH,a,b);})
    | #(LRSH        a=expr b=expr {r = bitwise.eval(LRSH,a,b);})
    | #(ALSH        a=expr b=expr {r = bitwise.eval(ALSH,a,b);})
    | #(ARSH        a=expr b=expr {r = bitwise.eval(ARSH,a,b);})
    | #(GT          a=expr b=expr {r = relate.eval(GT,a,b);})
    | #(LT          a=expr b=expr {r = relate.eval(LT,a,b);})
    | #(GTE         a=expr b=expr {r = relate.eval(GTE,a,b);})
    | #(LTE         a=expr b=expr {r = relate.eval(LTE,a,b);})
    | #(EQUALITY    a=expr b=expr {r = relate.eval(EQUALITY,a,b);})
    | #(INEQUALITY  a=expr b=expr {r = relate.eval(INEQUALITY,a,b);})
    | #(ROL         a=expr b=expr {r = bitwise.eval(ROL,a,b);})
    | #(ROR         a=expr b=expr {r = bitwise.eval(ROR,a,b);})
    | #(LAND        a=expr b=expr {r = logic.eval(LAND,a,b);}) 
    | #(LOR         a=expr b=expr {r = logic.eval(LOR,a,b);}) 
    | #(BAND        a=expr b=expr {r = bitwise.eval(BAND,a,b);})
    | #(BIOR        a=expr b=expr {r = bitwise.eval(BIOR,a,b);})
    | #(BEOR        a=expr b=expr {r = bitwise.eval(BEOR,a,b);})
    | #(APPEND      a=expr b=expr {})
    | #(ASSIGN      lhs:LVALUE b=expr {r = expr(#lhs); r.assign(b);})
    | #("=>"        y=string z=string {})
    | #(lval:LVALUE {}     
        (
            (y=id {
                    if(varSymbTbl.contains(y)){
                        r = varSymbTbl.get(y);
                    }else{
                        throw new BdplException("Undeclared identifier "+y);
                    }
                }| 
                (#(SQBROPEN y=id a=expr){
                    try
                    {
                        if(varSymbTbl.contains(y)){
                            System.out.print("Parsing:"+y+"["+a.get_int_value()+"]");
                        }else{
                            throw new BdplException("Undeclared identifer "+y);
                        }
                    }catch(Exception e){
                    }
                })
            )
            (y=id {
                    if(r instanceof DataNodeStruct){
                        r = ((DataNodeStruct)r).get_child_by_name(y);
                    }else{
                        throw new BdplException(r.get_name()+" is not a structure");
                    }
                } | 
                (#(SQBROPEN y=id a=expr){try{System.out.print("."+y+"["+a.get_int_value()+"]");}catch(Exception e){}})
            )*
        ){})
    | #(num:NUM     {r=new DataNodeInt(Integer.parseInt(num.getText()));})
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
    :#(str:STRING {r=str.getText();})
    ;
