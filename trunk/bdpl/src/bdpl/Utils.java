/*
 * Utils.java
 *
 * Created on November 18, 2007, 4:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 * Static utility functions
 * @author akshay
 */
public class Utils
{
    enum where_from {MSB,LSB};
    static int get_n_bits(int source,int n,where_from from)
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
    
    /** get n bits starting from the offset'th bit form the msb (leftmost) */
    static int get_n_bits(int source,int n,int offset)
    {
        int t=get_n_bits(source,n+offset,where_from.MSB);
        return get_n_bits(t,n,where_from.LSB);
    }
        
}
