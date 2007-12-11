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
    
    public static Type define_struct (String name, AST subtree,TypeSymbolTable st) throws Exception
    {
        Type structType=new Type (name);
        st.insert (name,structType);
        return structType;
    }
        
}
