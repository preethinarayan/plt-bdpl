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
    public DataNodeBit (DataNodeAbstract d) {super(d);init();_data=0;}
    
    public int get_max_accept()
    {
        return 1;
    }

    
    public void assign(DataNodeAbstract rhs)
    {
        String b=rhs.get_bitsequence_value ();
        int a=0;
        evaluate_fieldsize ();
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
        evaluate_verify_then_else();
        
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
        return ""+s.charAt (0);
    }
    public int get_int_value() {return _data;}
    
    public String print()
    {
        return get_bitsequence_value ();
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


    public void populate (BdplFile rhs) throws Exception
    {
        evaluate_fieldsize ();
        if(_fieldsize<=0) return;
        if(rhs.num_readable_bits ()>=1)
        {
            String next_bit=rhs.read_n_bits (1);
            if(next_bit.charAt (0)=='1')
                _data=1;
            else
                _data=0;
        }
        evaluate_verify_then_else();
    }
    
    private int _data;
    
}
