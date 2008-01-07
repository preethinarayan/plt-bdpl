
/**
 * BdplFile.java
 * This interface specifies what all a "file" should have in Bdpl. 
 * A file should be capable of saying how much data is yet to be read, 
 * and expose methods for reading the data. 
 * @author akshay
 */
public interface BdplFile
{

    /** read the next n bytes as characters and increment the file pointer
     *  @return next n bytes as asicc characters
     */
    public String read_n_bytes (int n) throws Exception;

    
    /** read the next n bits as a bitstring and increment the file pointer
     * @return next n bits as a string of 0's and 1's (that is, each char in 
     * the string is either '0' or '1'
     */
    public String read_n_bits (int n) throws Exception;
    
    /** returns the next nth readable bit (0 indexed) without increment the 
     * file pointer 
     * @return nth bit form the curent file pointer (0th bit is the next
     * bit that will get read.
     */
    public boolean get_nth_readable_bit(int n) throws Exception;
    
    /** are we at eof ? 
     *@return true is no more bits are left to be read */
    public boolean eof();
    
    /* @return number of bits that can be read from this file*/
    public int num_readable_bits();    
    
    /** jump the current pointer to this bit in the the file */
    public void set_current_pointer(int bit_pointer) throws Exception;
    
    /** get the current pointer value of this file */
    public int get_current_pointer();

    
}
