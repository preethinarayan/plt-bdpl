/*
 * Utils.java
 * Static utility functions
 * none of these are used yet !!
 */

/**
 *
 * @author Akshay Pundle
 */
import antlr.collections.*;
public class Utils
{
    enum where_from {MSB,LSB};
    /** get from source, n bits , starting from LSB or MSB */
    public static int get_n_bits(int source,int n,where_from from)
    {
        
        if(n<0)
            return 0;
        else if(n>32)
            n=32;
        
        if(from==where_from.LSB)
        {   
            /** think about it */
            return (source & ((1 << n) -1) );
        }
        else
        {
            
            if(n==0)
                return source;
            else
                return (source >>>(32-n) );
        }
    }
    
    /** get from source, n bits starting from the offset'th bit form the msb */
    public static int get_n_bits(int source,int n,int offset)
    {
        int t=get_n_bits(source,n+offset,where_from.MSB);
        return get_n_bits(t,n,where_from.LSB);
    }
    
    public static String bits_as_string (String bits)
    {
        String ret="";
        int l= (int)(bits.length ()/8 ); // floor
        int remaining=bits.length () % 8;
        for(int i=0;i<l;i++)
        {
            int c=0;
            for(int j=0;j<8;j++)
            {
                c=c<<1;
                if(bits.charAt (i*8+j)=='1')
                    c+=1;
            }
            ret+= (char)c;
            
        }
        if(remaining > 0)
        {
            int c=0;
            for(int i=0;i<8;i++)
            {
                c=c<<1;
                if(i<remaining && bits.charAt ( (l-1)*8+i)=='1')
                    c+=1;
            }
            
            ret+= (char)c;
        }
        return ret;
    }
    
}
