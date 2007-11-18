/*
 * DataNodeBit.java
 *
 * Created on November 8, 2007, 10:02 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author akshay
 */
public class DataNodeBit extends DataNodeAbstract
{
    
    /** list all constructors here */
    /** Creates a new instance of DataNodeBit */
    public DataNodeBit () {super();_data=0;_bitsize=1; }
    
    /** construct from int */
    public DataNodeBit (int data) {super(); _data=data;_bitsize=1;}
    
    /** copy constructor */
    public DataNodeBit (DataNodeBit data) {super(data);_data=data._data;}
    
    
    /** return the unique typename */
    public String get_type_name() {return "TYPE_BIT";}
    
    public String get_bitsequence_value() 
    {
        String s=Integer.toString (_data,2); 
        if((s.length () >= _fieldsize))
        {
            return s.substring ( s.length ()-_fieldsize, s.length ()-1);
        }
        else
        {
            for(int i=0;i<_fieldsize-s.length ();i++)
            {
                s='0'+s;
            }
            return s;
        }
    }
    public int get_int_value() {return _data;}
    
    private int _data;
    
}
