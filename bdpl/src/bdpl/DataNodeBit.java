/*
 * DataNodeBit.java
 *
 */

/**
 *
 * @author Akshay Pundle
 */
public class DataNodeBit extends DataNodeAbstract
{
    
    void init()
    {
        _bitsize=1;
        _fieldsize=1;
    }
    /** list all constructors here */
    /** Creates a new instance of DataNodeBit */
    public DataNodeBit () {super();init();_data=0;}
    
    public int get_max_accept()
    {
        return 1;
    }
    
    public void assign(DataNodeAbstract rhs)
    {
        String b=rhs.get_bitsequence_value ();
        if(b.length ()>0)
            _data= b.charAt (0);
        else
            _data=0;
    }
    /** construct from int */
    public DataNodeBit (int data) {super(); init();_data=data;}
    
    /** copy constructor */
    public DataNodeBit (DataNodeBit data) {super(data);init();_data=data._data;_fieldsize=data._fieldsize;_bitsize=data._bitsize;}
    
    
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
    
    public String print()
    {
        return get_bitsequence_value ();
    }
    
    private int _data;
    
}
