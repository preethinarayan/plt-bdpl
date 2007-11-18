/*
 * DataNodeArray.java
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
public class DataNodeArray extends DataNodeAbstract
{    
    /** Creates a new instance of DataNodeArray */
    public DataNodeArray ()
    {
        super();
        _children= new ArrayList() ;
    }
    
    /** should invoke copy constructor for base class */
    public DataNodeArray (DataNodeArray data)
    {
        super(data);
        _children = data._children;
    }
    
    /** return the unique typename */
    public String get_type_name() {return "ARRAY";}
    public String get_bitsequence_value() 
    {
        String ret="";
        for(int i=0;i<_children.size() ; i++ )
        {
            ret=ret+ ((DataNodeArray)_children.get (i)).get_bitsequence_value ();
        }
        return ret;
    }
    public int get_bit_size()  
    { 
        int ret=0;
        for(int i=0;i<_children.size() ; i++ )
        {
            ret+=((DataNodeArray)_children.get (i)).get_bit_size ();
        }
        return ret;
    }
    public int get_int_value() 
    {
        
        return 0;
    }
    public int get_fieldsize() {return 0;}
    public int set_fieldsize() {return 0;}
    
    /** returns true if this array is a * array */
    public boolean get_is_unlimited() {return false;}
    
    /** returns size of the array in num of elements */
    public int get_size() {return 0;}
    
    /** returns the ith element from the array */
    public DataNodeAbstract get_element(int i) { return new  DataNodeBit(); }
    
    /** sets the ith element of the array */
    public int set_element(int i,DataNodeAbstract data) {return 0;}
    
    
    /** returns the type of (all) the elements of the array 
     * string for now. the return value sould be a "type" class */
    public String get_element_type_name() { return "";}
    
    /** returns offset of the "current pointer' witin the array.*/
    public int get_offset() {return _offset; }
    
    /** sets curret pointer within the array */
    public void set_offset(int offset) {_offset=offset;}
    
    /**  class private data */
        
   /** current pointer within the array (in bits) */
    private int _offset;
    
    /** elements of the array */
    private List _children; 
    
    
    
}
