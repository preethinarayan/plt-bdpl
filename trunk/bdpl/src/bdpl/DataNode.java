/*
 * DataNode.java
 *
 * This defines the interface which all data nodes should 
 * adhere to (i.e this is the abstract base class for all
 * our concrete data node classes 
 *
 */

/**
 *
 * @author Akshay Pundle
 */

public interface DataNode
{
    public String get_type_name();
    public String get_bitsequence_value() ;
    public int get_bit_size(); 
    
    public int get_int_value() throws Exception;
    /** @return the number of bits this type takes */    
    public int get_fieldsize();
    
    /** set the number of bits this type takes
    * @return nonzero on error */
    public int set_fieldsize(int fieldsize);
    
    /** print a human readable form of this node */
    public String print();
    
    /** print with formatting 
     * 0 = print() [debug formatted]
     * 1 = as string [useful to write to a file]
     * 2 = debug with strings [similar to 0, but with strings instad of bytes]
     * @return Formatted output ready to be printed
     */
    public String print(int format);
    public void set_name(String name);
    public String get_name();
    public int get_max_accept();
    public void assign(DataNodeAbstract rhs);
    public void populate(BdplFile rhs) throws Exception;
}
