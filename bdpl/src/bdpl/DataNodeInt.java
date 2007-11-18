/*
 * DataNodeInt.java
 *
 * Created on November 8, 2007, 10:03 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author akshay
 */
public class DataNodeInt extends DataNodeAbstract
{
    
    /** Creates a new instance of DataNodeInt */
    public DataNodeInt () {super();_data=0; _bitsize=32;}
    
    /** construct from int */
    public DataNodeInt (int data) {super();_data=data; _bitsize=32;}
    
    /** copy constructor */
    public DataNodeInt (DataNodeInt data) {super(data);_data=data._data;_bitsize=data._bitsize; }
    
    /** return the unique typename */
    public String get_type_name() {return "TYPE_INT";}
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
