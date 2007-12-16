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
        _data=data&0xff;
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
        evaluate_fieldsize ();
        return _fieldsize;
    }
    
    public void assign(DataNodeAbstract rhs)
    {
        evaluate_fieldsize ();
        String b=rhs.get_bitsequence_value ();
        int a=0;
        int start=0,end=_fieldsize-1;

        if(rhs.getClass ().getCanonicalName (). equals ("DataNodeBit")  ||
            rhs.getClass ().getCanonicalName ().equals ("DataNodeByte") ||
            rhs.getClass ().getCanonicalName ().equals ("DataNodeInt"))
        {
            start=(b.length ()>=_fieldsize)?b.length ()-_fieldsize:0;
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
        if((s.length () >= _bitsize))
        {
            return s.substring ( s.length ()-_bitsize, s.length ());
        }
        else
        {
            int l=s.length ();
            for(int i=0;i<_bitsize-l;i++)
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
            //return get_bitsequence_value ()+"\n";
            return Utils.bits_as_string (get_bitsequence_value ());
        }
        else
        {
            return "";
        }
    }        
    
    public void populate (BdplFile rhs) throws Exception
    {
        evaluate_fieldsize ();
        if(rhs.num_readable_bits ()>=_fieldsize && _fieldsize!=0)
        {
            String bits=rhs.read_n_bits (_fieldsize);
            _data=(int) Integer.parseInt (bits,2);
        }
    }
        
    private int _data;
    
}
