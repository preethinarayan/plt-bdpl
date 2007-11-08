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
public class DataNodeArray
{
    static String ARRAY_ID_STRING="ARRAY";
    /** Creates a new instance of DataNodeArray */
    public DataNodeArray ()
    {
    }
    public String get_type_name() {return ARRAY_ID_STRING;}
    public String get_bitsequence_value() {return "";}
    public int get_bit_size()  {return 0;}
    public int get_int_value() {return 0;}
    public int get_fieldsize() {return 0;}
    public int set_fieldsize() {return 0;}
    
    public boolean get_is_unlimited() {return false;}
    
    
}
