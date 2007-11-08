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
abstract public class DataNodeAbstract implements DataNode
{
    
    /** Creates a new instance of AbstractDataNode */
    public DataNodeAbstract ()
    {
    }
    
    abstract public String get_type_name();
    abstract public String get_bitsequence_value();
    abstract public int get_bit_size();
    abstract public int get_int_value();
    abstract public int get_fieldsize();
    abstract public int set_fieldsize();
    
    /**
     * Fieldsize store the current size of this field
     */ 
    private int fieldsize;
    
}
