/**
 *
 * @author akshay
 */
import java.io.*;
import java.util.*;

/**
 * BdplDataFile.java
 * This is the data file implementation. We read from a file on disk
 * and store the contents of the file in memory in a byte array. We
 * maintain a pointer to the current position in the file. When data
 * is read, the file pointer is incremented. This class implements 
 * the BdplFile interface, and thus is like the memfile, except that
 * the contents are read from disk. Since we know that files on disk
 * will have bytes, we do not need to maintain a pointer to the end 
 * of the buffer.
 *
 */
public class BdplDataFile implements BdplFile
{

    
    private ArrayList _data;
    private int _bit_pointer; // pointer to the next readable element
    
    /**
     * Creates a new instance of BdplDataFile
     */
    public BdplDataFile (String filename) throws BdplException
    {
        _data=new ArrayList(); 
        _bit_pointer=-1; 
        try
        {
            
            FileInputStream file=new FileInputStream(filename);
            
            boolean flag=true;
            while(flag)
            {
                int size=file.available ();
                byte[] inp =new byte[size];
                int bytes_read=file.read (inp);
                for(int i=0;i<bytes_read;i++)
                {
                    _data.add (inp[i]);
                }
                if(bytes_read<=0)
                    flag=false;
                else
                {
                    _bit_pointer=0;
                }
            }
            file.close ();
            
        }
        catch(IOException e)
        {
            throw new BdplException (e.getMessage ());
        }

    } 
    
    /** make a file froma bitsequence. limit to max_size bits */
    public BdplDataFile (String bitsequence,int max_size) throws BdplException
    {
        _data=new ArrayList(); 
        _bit_pointer=-1; 

    }    
    
    
    public BdplDataFile (BdplDataFile f,int maxsize) throws BdplException
    {
        _data=new ArrayList(); 
        _bit_pointer=-1; 

    }
    
    public String read_n_bytes (int n) throws Exception
    {
        String ret="";
        if( (_bit_pointer+8*n) > 8*_data.size () )
            throw new Exception("dont have "+n+" bytes to read, have only " + ( 8*_data.size ()-_bit_pointer)+" bits");
        while(n>0 && _bit_pointer<=8*(_data.size ()-1))
        {
            String s=read_n_bits(8);
            int c=0;
            for(int i=0;i<8;i++)
            {
                c=c<<1;
                if(s.charAt (i)=='1')
                    c+=1;
            }
            ret+=(char)c;
            n--;
        }
        return ret;
    }

    public String read_n_bits (int n) throws Exception
    {
        
        String ret="";
        
        while(_bit_pointer < 8*_data.size () && (_bit_pointer %8)!=0 && n > 0)
        {
            boolean b=get_nth_readable_bit (0);
            if(b)
                ret+='1';
            else
                ret+='0';
            
            _bit_pointer++;
            n--;    
        }
        
        int start= n==0?0:(_bit_pointer)   / 8;
        int end  = n==0?0:(_bit_pointer+n) / 8;
        int rem  = n==0?0:(_bit_pointer+n) % 8;
        
        for(int i=start; i<_data.size () && i<end ;i++)
        {
            String s=Integer.toString ( Byte.parseByte (_data.get (i).toString ()),2);
            int l=s.length ();
            for(int j=0;j<8-l;j++)
            {
                s='0'+s;
            }
            ret+=s;
            _bit_pointer+=8;
        }
        

        for(int i=0;i<rem;i++)
        {
            boolean b=get_nth_readable_bit (0);
            if(b)
                ret+='1';
            else
                ret+='0';
            
            _bit_pointer++;
        }
        
        return ret;
    }
    
    /** returns the next nth readable bit (0 indexed) */
    public boolean get_nth_readable_bit(int n) throws Exception
    {
        if( ((n + _bit_pointer) > 8*_data.size () ) || _bit_pointer < 0 )
            throw new Exception("index out of range");
        int off=(n+_bit_pointer)%8;
        byte mask= (byte)(1 << (7-off));
        return ( (Byte.parseByte ( _data.get ( (n+_bit_pointer)/8 ).toString () ) & mask)!=0);
    }
    
    public boolean eof()
    {
        return _bit_pointer>= 8*_data.size ();
    }
    
    public int num_readable_bits()
    {
        return (8*_data.size ())-_bit_pointer;
    }
    
}//end of class