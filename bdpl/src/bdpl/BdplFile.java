/*
 * BdplFile.java
 */

/**
 *
 * @author akshay
 */
public interface BdplFile
{

    /** read the next n bytes as characters and increment the file pointer*/
    public String read_n_bytes (int n) throws Exception;

    /** read the next n bits as a bitstring and increment the file pointer*/
    public String read_n_bits (int n) throws Exception;
    
    /** returns the next nth readable bit (0 indexed) without increment the file pointer */
    public boolean get_nth_readable_bit(int n) throws Exception;
    
    /** are we at eof ? 
     *@return true is no more bits are left to be read */
    public boolean eof();
    
    /* @return number of bits that can be read from this file*/
    public int num_readable_bits();    

    
}
