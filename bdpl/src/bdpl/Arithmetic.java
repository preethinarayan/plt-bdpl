/*
 * Arithmetic.java
 *
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
                res = 0;
                //
                // This point will never be reached
                //
                assert(false);
        }
        
        System.out.println("Computed arithmetic expression: "+res);
        
        if(result instanceof DataNodeInt){
            return new DataNodeInt(res);
        }else if(result instanceof DataNodeByte){
            return new DataNodeByte(res);
        }else if(result instanceof DataNodeBit){
            return new DataNodeBit(res);
        }else{
            //
            // This should never happen
            //
            assert(false);
            return null;
        }
    }
}
