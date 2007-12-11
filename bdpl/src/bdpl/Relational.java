/*
 * Relational.java
 *
 */

/**
 *
 * @author Bharadwaj Vellore
 */
public class Relational implements BdplLexerTokenTypes{
    
    private TypeChecker _typeChecker;
    /** Creates a new instance of Relational */
    public Relational(TypeChecker typeChecker) {
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
            case GT:
                res = operand1.get_int_value() > operand2.get_int_value();
                break;
            case LT:
                res = operand1.get_int_value() < operand2.get_int_value();
                break;
            case GTE:
                res = operand1.get_int_value() >= operand2.get_int_value();
                break;
            case LTE:
                res = operand1.get_int_value() <= operand2.get_int_value();
                break;
            case EQUALITY:
                res = operand1.get_int_value() == operand2.get_int_value();
                break;
            case INEQUALITY:
                res = operand1.get_int_value() != operand2.get_int_value();
                break;    
            default:
                res = false;
                //
                // This point will never be reached
                //
                assert(false);
        }
        
        System.out.println("Computed relational expression: "+res);
        
        return new DataNodeBit(res==true?1:0);
    }
    
}
