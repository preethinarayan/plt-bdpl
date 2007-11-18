/*
 * DataNodeStruct.java
 *
 * Created on November 8, 2007, 10:04 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author akshay
 */

import java.util.*;
public class DataNodeStruct
{
    
    /** Creates a new instance of DataNodeStruct */
    public DataNodeStruct ()
    {
    }
    /** return the unique typename */
    public String get_type_name() {return "";}
    public String get_bitsequence_value() {return "";}
    public int get_bit_size()  {return 0;}
    public int get_int_value() {return 0;}
    public int get_fieldsize() {return 0;}
    public int set_fieldsize() {return 0;}
    
    /** size of struct presumably in number of elements */
    public int get_size() {return 0;}
    
    /** returns child(element of the struct) given name of the child */
    public DataNodeAbstract get_child_by_name(String name) { return new  DataNodeBit(); }
    
    /** initialize / set a child by name 
     @return error */
    public int set_child_by_name(String name,DataNodeAbstract  value) { return 0;}
    
    /** returns child(element of the struct) given number of the child */
    public DataNodeAbstract get_child_by_num(int num) { return new  DataNodeBit(); }
    
    /** returns all children(elements of the struct) */
    public DataNodeAbstract get_all_children(int num) { return new  DataNodeBit(); }
    
    /** returns number of elements the struct has */
    public int get_num_children() { return 0;}
    
    /** returns the type of a child of the struct 
     * string for now. the return value sould be a "type" class */
    public String get_child_type_by_name(String name) { return "";}
    
    /** current pointer within the struct */
    public int get_offset() {return _offset; }
    
    /** set current pointer within the struct */
    public void set_offset(int offset) {_offset=offset;}
    
    private int _offset;
    private HashMap _children; 
    
}
