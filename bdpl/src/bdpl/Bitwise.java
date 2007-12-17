/*
 * Bitwise.java
 *
 */

import java.util.BitSet;
/**
 *
 * @author Bharadwaj Vellore
 */
public class Bitwise implements BdplLexerTokenTypes {
    
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
        DataNodeAbstract result = null;
        int res = 0;

        if(!TypeChecker.isBasic(operand1.get_type_name())){
            throw new BdplException(operand1.get_name()+" is not of a basic type");
        }
        if(!TypeChecker.isBasic(operand2.get_type_name())){
            throw new BdplException(operand1.get_name()+" is not of a basic type");
        }
        
        Type resultType;
     
        switch(operator){
            case LLSH:
                resultType = _typeChecker.getResultType(operand1.get_type_name(),operand1.get_type_name());
                result = resultType.getDataNode();
                res = operand1.get_int_value() << operand2.get_int_value();
                break;
            case LRSH:
                resultType = _typeChecker.getResultType(operand1.get_type_name(),operand1.get_type_name());
                result = resultType.getDataNode();
                res = operand1.get_int_value() >>> operand2.get_int_value();
                break;
            case ALSH:
                resultType = _typeChecker.getResultType(operand1.get_type_name(),operand1.get_type_name());
                result = resultType.getDataNode();
                res = operand1.get_int_value() << operand2.get_int_value();
                break;
            case ARSH:
                resultType = _typeChecker.getResultType(operand1.get_type_name(),operand1.get_type_name());
                result = resultType.getDataNode();
                res = operand1.get_int_value() >> operand2.get_int_value();
                break;
            case ROR:
                resultType = _typeChecker.getResultType(operand1.get_type_name(),operand1.get_type_name());
                result = resultType.getDataNode();
                //res = operand1.get_int_value() % operand2.get_int_value();
                break;
            case ROL:
                resultType = _typeChecker.getResultType(operand1.get_type_name(),operand1.get_type_name());
                result = resultType.getDataNode();
                //res = operand1.get_int_value() % operand2.get_int_value();
                break;
            case BAND:
                resultType = _typeChecker.getResultType(operand1.get_type_name(),operand2.get_type_name());
                result = resultType.getDataNode();
                res = operand1.get_int_value() & operand2.get_int_value();
                break;
            case BEOR:
                resultType = _typeChecker.getResultType(operand1.get_type_name(),operand2.get_type_name());
                result = resultType.getDataNode();
                res = operand1.get_int_value() ^ operand2.get_int_value();
                break;    
            case BIOR:
                resultType = _typeChecker.getResultType(operand1.get_type_name(),operand2.get_type_name());
                result = resultType.getDataNode();
                res = operand1.get_int_value() | operand2.get_int_value();
                break;    
            default:
                res = 0;
                //
                // This point will never be reached
                //
                assert(false);
        }

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
