/*
 * AbstractDataNode.java
 *
 * Created on November 8, 2007, 9:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author akshay
 */
import antlr.collections.*;
abstract public class DataNodeAbstract implements DataNode
{
    
    /** Creates a new instance of AbstractDataNode */
    public DataNodeAbstract ()
    {
        _fieldsize=0;
        _bitsize=0;
    }
    public DataNodeAbstract (DataNodeAbstract data)
    {
        _fieldsize=data._fieldsize;
        _bitsize=data._bitsize;
    }
    
    public Exception get_ex(String ex_string)
    {
        return new Exception(this.getClass().getName()  +": "+ex_string);
    }

    
    abstract public String get_type_name();
    abstract public String get_bitsequence_value();
    
    public int get_bit_size()  {return _bitsize;}
    
    abstract public int get_int_value() throws Exception ;
    
    /** @return the number of bits this type takes */
    public int get_fieldsize() { return _fieldsize;}
    
    /** set the number of bits this type takes
     * @return nonzero on error */
    public int set_fieldsize(int fieldsize) {_fieldsize=fieldsize;return 0;}
    
    /** print a human readable form of this node */
    abstract public String print();
    
    /** store the max size of the data in num of bits */ 
    protected int _fieldsize;
    /** stores the currecnt size of the data in bits */
    protected int _bitsize;
    
    protected AST _the_ast;
    
}
