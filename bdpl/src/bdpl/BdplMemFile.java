
import java.io.*;
import java.util.*;
/**
 * BdplMemFile.java
 * This is the mem file implementation. This class implements the
 * the BdplFile interface, and thus is like the datafile (and indeed
 * the two can e used interchangeably). This "file" does not correspond
 * to data on a disk, but must be initialized with a bitsequence in 
 * memory. This file also has a way to build itself from BdplFile.
 * This type of file is used to represent bitsequences of objects 
 * in memory.
 * @author akshay
 */
public class BdplMemFile implements BdplFile
{
    
    private String _data;
    private String _l_data;
    private int _bit_pointer; // pointer to the next readable element
    private void init()
    {
        _data="";
        _l_data="";
    }
    
    /** Creates a new instance of BdplMemFile from a bitsequence*/
    public BdplMemFile (String bitsequence)
    {
        init();
        _data=bitsequence;
    }

    
    /** make a file of n bits from the given file without incrementing its pointer */
    public BdplMemFile (BdplFile f,int n) throws Exception
    {
        init();
        for(int i=0;i<n;i++)
        {
            if( f.get_nth_readable_bit (i))
            {
                _data+='1';
            }
            else
            {
                _data+='0';
            }
            
        }

    }
    
    public String read_n_bytes (int n) throws Exception
    {
        if( 8*n > _data.length ())
            throw new Exception("dont have "+n+" bytes to read, have only " + _data.length () + " bits");
        String ret="";
        for(int i=0;i<n;i++)
        {
            ret += (char)Byte.parseByte (_data.substring (i*8,(i+1)*8),2);
        }
        _l_data=_l_data+_data.substring(0,n*8);
        _data=_data.substring (n*8);
        return ret;
    }

    public String read_n_bits (int n) throws Exception
    {
        if(n>_data.length ())
            throw new Exception("dont have "+n+" bits to read, have only " + _data.length () + " bits");
        String ret="";
        if(n<_data.length ())
        {
            ret=_data.substring (0,n);
            _data=_data.substring (n);
        }
        else if(n==_data.length ())
        {
            ret=_data;
            _data="";
        }
        _l_data+=ret;
        return ret;
    }
    
    /** returns the next nth readable bit (0 indexed) */
    public boolean get_nth_readable_bit(int n) throws Exception
    {
        if( n>_data.length ())
            throw new Exception("index out of range");
        
        return ( (_data.charAt (n)=='1') );
    }
    
    public boolean eof()
    {
        return (_data.length ()==0);
    }
    
    public int num_readable_bits()
    {
        return _data.length ();
    }
    
    public void set_current_pointer(int bit_offset) throws Exception
    {
        if(bit_offset > _data.length()+_l_data.length())
            throw new Exception("bit offset too large");
        // can be done better
        _data=_l_data+_data;
        _l_data=_data.substring(0,bit_offset);
        _data=_data.substring(bit_offset);
    }
    
}//end of class
   
