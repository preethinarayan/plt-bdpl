import antlr.collections.AST;
/**
 * DataNode.java
 *
 * This defines the interface which all data nodes should 
 * adhere to (i.e this is the abstract base class for all
 * our concrete data node classes 
 * @author Akshay Pundle
 */
public interface DataNode
{
    /** @return type name of this data node */
    public String get_type_name();
    /** @return the bits of this data node */
    public String get_bitsequence_value() ;
    
    /** @return size of this data in bits*/
    public int get_bit_size(); 
    
    /** @return size of this data as an integer (for expresion evaluation
     * purpose */
    public int get_int_value() throws Exception;
    
    /** @return the number of bits this type takes */    
    public int get_fieldsize();
    
    /** set the expression for number of bits this type takes*/
    public void set_fieldsize(AST fieldsize);
    
    /** set valid ok nok */
    public void set_verify_then_else(AST verify_ast,AST then_ast , AST else_ast);
    
    /** sets the contaxt for evaluation of expressions for this data node*/
    public void set_context(VariableSymbolTable context);
    
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
    
    /** returns the maximum size this object can accept (hold) or -1 for infinity */
    public int get_max_accept();
    public void assign(DataNodeAbstract rhs) throws Exception;
    public void populate(BdplFile rhs) throws Exception;
    
}
