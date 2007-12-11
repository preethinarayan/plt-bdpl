/*
 * TypeChecker.java
 *
 * Created on December 11, 2007, 1:47 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Bharadwaj Vellore
 */
public class TypeChecker {
    
    private TypeSymbolTable _typeSymbTbl;
    private TypeConverter _typeConvert;

    /** Creates a new instance of TypeChecker */
    public TypeChecker(TypeSymbolTable typeSymbTbl, TypeConverter typeConvert) {
        _typeSymbTbl = typeSymbTbl;
        _typeConvert = typeConvert;
    }
    
    public static boolean isBasic(String type){
        return (
                (type == "int")?
                    true:
                    (
                        (type == "byte")?
                            true:
                            (
                                (type == "bit")?
                                    true:
                                    false
                            )
                    )
               );
    }
    
    public Type getResultType(String type1, String type2) throws Exception
    {
        return _typeSymbTbl.get(_typeConvert.get(type1,type2));        
    }
    
}
