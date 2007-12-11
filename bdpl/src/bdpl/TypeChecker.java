/*
 * TypeChecker.java
 *
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
                (type == "TYPE_INT")?
                    true:
                    (
                        (type == "TYPE_BYTE")?
                            true:
                            (
                                (type == "TYPE_BIT")?
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
