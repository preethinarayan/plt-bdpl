/*
 * Bitwise.java
 *
 */

/**
 *
 * @author Bharadwaj Vellore
 */
public class Bitwise {
    
    private TypeChecker _typeChecker;
    /** Creates a new instance of Bitwise */
    public Bitwise(TypeChecker typeChecker) {
        _typeChecker = typeChecker;
    }
    
    public DataNodeAbstract eval(
            int operator,
            DataNodeAbstract operand1,
            DataNodeAbstract operand2) throws Exception
    {
        return new DataNodeInt();
    }
    
}
