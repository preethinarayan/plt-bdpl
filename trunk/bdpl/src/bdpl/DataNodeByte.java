/*
 * DataNodeByte.java
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
public class DataNodeByte extends DataNodeAbstract
{
    void init ()
    {
        _bitsize=8;
        _fieldsize=8;
    }
    /** Creates a new instance of DataNodeByte */
    public DataNodeByte ()
    {
        super ();
        init ();
        _data=0;
    }
    
    /** construct from int */
    public DataNodeByte (int data)
    {
        super ();
        init ();
        _data=data;
    }
    
    /** copy constructor */
    public DataNodeByte (DataNodeByte data)
    {
        super (data);
        init ();
        _data=data._data;
        _bitsize=data._bitsize;
        _fieldsize=data._fieldsize;
    }
    
    /** return the unique typename */
    public String get_type_name () {return "TYPE_BYTE";}
    
    public String get_bitsequence_value ()
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
    public int get_int_value () {return _data;}
    
    public String print()
    {
        return String.format ("0x%4x",_data);
    }
    
    private int _data;
    
}
