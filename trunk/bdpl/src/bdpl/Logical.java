/*
 * Logical.java
 *
 */

/**
 *
 * @author Bharadwaj Vellore
 */
public class Logical implements BdplLexerTokenTypes{

    private TypeChecker _typeChecker;
    /** Creates a new instance of Logical */
    public Logical(TypeChecker typeChecker) {
        _typeChecker = typeChecker;
    }
    
    public DataNodeAbstract eval(
            int operator,
            DataNodeAbstract operand1,
            DataNodeAbstract operand2) throws Exception
    {
        boolean res;

        if(!TypeChecker.isBasic(operand1.get_type_name())){
            throw new BdplException(operand1.get_name()+" is not of a basic type");
        }
        if(!TypeChecker.isBasic(operand2.get_type_name())){
            throw new BdplException(operand1.get_name()+" is not of a basic type");
        }
        
        switch(operator){
            case LAND:
            res = (operand1.get_int_value()==0?false:true) && (operand2.get_int_value()==0?false:true);
                break;
            case LOR:
                res = (operand1.get_int_value()==0?false:true) || (operand2.get_int_value()==0?false:true);
                break;
            default:
                res = false;
                //
                // This point will never be reached
                //
                assert(false);
        }
        
        System.out.println("Computed logical expression: "+res);
        
        return new DataNodeBit(res==true?1:0);
    }
}
