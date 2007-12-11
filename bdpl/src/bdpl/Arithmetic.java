/*
 * Arithmetic.java
 *
 * Created on December 11, 2007, 1:41 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Bharadwaj Vellore
 */
public class Arithmetic implements BdplLexerTokenTypes{
    
    /** Creates a new instance of Arithmetic */
    
    //
    // To Do: Apply singleton pattern here
    //
    private TypeChecker _typeChecker;
    
    public Arithmetic(TypeChecker tc){
        _typeChecker = tc;    
    }
    
    public DataNodeAbstract eval(
            int operator,
            DataNodeAbstract operand1,
            DataNodeAbstract operand2) throws Exception
    {
        DataNodeAbstract result;
        int res;

        if(!TypeChecker.isBasic(operand1.get_type_name())){
            throw new BdplException(operand1.get_name()+" is not of a basic type");
        }
        if(!TypeChecker.isBasic(operand2.get_type_name())){
            throw new BdplException(operand1.get_name()+" is not of a basic type");
        }
        
        Type resultType = _typeChecker.getResultType(operand1.get_type_name(),operand2.get_type_name());
        result = resultType.getDataNode();
        
        switch(operator){
            case PLUS:
                res = operand1.get_int_value() + operand2.get_int_value();
                break;
            case MINUS:
                res = operand1.get_int_value() - operand2.get_int_value();
                break;
            case STAR:
                res = operand1.get_int_value() * operand2.get_int_value();
                break;
            case SLASH:
                res = operand1.get_int_value() / operand2.get_int_value();
                break;
            case PERCENT:
                res = operand1.get_int_value() % operand2.get_int_value();
                break;
            default:
                result = new DataNodeInt();
                //
                // This point will never be reached
                //
                assert(false);
        }
        
        return result;
    }
}
