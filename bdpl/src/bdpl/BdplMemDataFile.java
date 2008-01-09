/*
 * BdplMemDataFile.java
 *
 * Created on January 6, 2008, 4:36 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author akshay
 */
public class BdplMemDataFile extends BdplMemFile
{
    private BdplFile _f_ptr;
    private int _size;
    /** make a file of n bits from the given file without incrementing its pointer */
    
    private void init()
    {
        _f_ptr=null;
        _size=0;
    }
    public BdplMemDataFile (BdplFile f,int n) throws Exception
    {
        super("");
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
        _size=n;
        _f_ptr=f;
    }
    
    public void set_current_pointer(int bit_offset) throws Exception
    {
        _f_ptr.set_current_pointer (bit_offset);
        int n = _size-_l_data.length ();
        _data="";

        for(int i=0;i<n && i<_f_ptr.num_readable_bits ();i++)
        {
            if( _f_ptr.get_nth_readable_bit (i))
            {
                _data+='1';
            }
            else
            {
                _data+='0';
            }
            
        }
    }
    
    public int get_current_pointer()
    {
        return _f_ptr.get_current_pointer ();
    }
}
