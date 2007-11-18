/*
 * DataNode.java
 *
 * Created on November 8, 2007, 9:43 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 * This defines the interface which all data nodes should 
 * adhere to (i.e this is the abstract base class for all
 * our concrete data node classes 
 *
 * @author akshay
 */
public interface DataNode
{
    public String get_type_name();
    public String get_bitsequence_value() ;
    public int get_bit_size(); 
    public int get_int_value() throws Exception;
    public int get_fieldsize();
    public int set_fieldsize(int fieldsize);
}
