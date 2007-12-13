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
    public int get_fieldsize();
    public int set_fieldsize(int fieldsize);
    public String print();
    public void set_name(String name);
    public String get_name();
    public int get_max_accept();
    public void assign(DataNodeAbstract lhs);
}
