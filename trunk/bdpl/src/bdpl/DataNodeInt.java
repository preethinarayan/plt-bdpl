/*
 * DataNodeInt.java
 *
 */

/**
 *
 * @author Akshay Pundle
 */
public class DataNodeInt extends DataNodeAbstract
{
    void init()
    {
        _bitsize=32;
        _fieldsize=32;
    }
    
    /** Creates a new instance of DataNodeInt */
    public DataNodeInt () {super();init();_data=0;}
    
    /** construct from int */
    public DataNodeInt (int data) {super();init();_data=data; }
    
    /** copy constructor */
    public DataNodeInt (DataNodeInt data) {super(data);init();_data=data._data;_bitsize=data._bitsize; _fieldsize=data._fieldsize;}
    
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
    
    public String print()
    {
        return String.valueOf (_data);
    }
     
    private int _data;
    
    
}
