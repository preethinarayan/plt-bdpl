/*
 * DataNodeByte.java
 *
 */

/**
 *
 * @author Akshay Pundle
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
    
    public int get_max_accept()
    {
        return 8;
    }
    
    public void assign(DataNodeAbstract rhs)
    {
        String b=rhs.get_bitsequence_value ();
        int a=0;
        int start=0,end=7;

        if(rhs.getClass ().getCanonicalName (). equals ("DataNodeBit")  ||
            rhs.getClass ().getCanonicalName ().equals ("DataNodeByte") ||
            rhs.getClass ().getCanonicalName ().equals ("DataNodeInt"))
        {
            start=(b.length ()>=8)?b.length ()-8:0;
            end=b.length ()-1;
        }
        for(int i=start;i<=end && i<b.length ();i++)
        {
            a=a<<1;
            if(b.charAt (i)=='1')
                a+=1;
            
        }
        _data=a;
        
    }
    
    /** return the unique typename */
    public String get_type_name () {return "TYPE_BYTE";}
    
    public String get_bitsequence_value ()
    {
        String s=Integer.toString (_data,2);
        if((s.length () >= _fieldsize))
        {
            return s.substring ( s.length ()-_fieldsize, s.length ());
        }
        else
        {
            int l=s.length ();
            for(int i=0;i<_fieldsize-l;i++)
            {
                s='0'+s;
            }
            return s;
        }
    }
    public int get_int_value () {return _data;}
    
    public String print()
    {
        return String.format ("0x%x",_data);
    }
    

    /** print formatted */
    public String print(int format)
    {
        if(format==0)
        {
            return print ();
        }
        else if(format ==1 )
        {
            return Utils.bits_as_string (get_bitsequence_value ());
        }
        else
        {
            return "";
        }
    }        
        
    private int _data;
    
}
