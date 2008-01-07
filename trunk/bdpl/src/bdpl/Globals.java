/*
 * Globals.java
 *
 * Created on December 15, 2007, 10:16 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author akshay
 */
public class Globals
{
    public Globals()
    {
        little_endian=true;
        VariableSymbolTable varSymbTbl = new VariableSymbolTable ();
        FileSymbolTable fileSymbTbl=new FileSymbolTable ();
        TypeSymbolTable typeSymbTbl = new TypeSymbolTable ();
        
        TypeConverter typeConverter = new TypeConverter ();
        TypeChecker typeChecker = new TypeChecker (typeSymbTbl,typeConverter);
        Arithmetic arith = new Arithmetic (typeChecker);
        Relational relate = new Relational (typeChecker);
        Logical logic = new Logical (typeChecker);
        Bitwise bitwise = new Bitwise (typeChecker);
    }
    /** is everything supposed to be done in little endian ? */
    public static boolean little_endian=true;
    
    public static VariableSymbolTable varSymbTbl = new VariableSymbolTable();
    public static FileSymbolTable fileSymbTbl=new FileSymbolTable();
    public static TypeSymbolTable typeSymbTbl = new TypeSymbolTable();
    
    public static TypeConverter typeConverter = new TypeConverter();
    public static TypeChecker typeChecker = new TypeChecker(typeSymbTbl,typeConverter);
    public static Arithmetic arith = new Arithmetic(typeChecker);
    public static Relational relate = new Relational(typeChecker);
    public static Logical logic = new Logical(typeChecker);
    public static Bitwise bitwise = new Bitwise(typeChecker);
    
}
