/*
 * DataNodeArray.java
 *
 * Created on November 8, 2007, 10:04 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author akshay
 */

import java.util.*;
public class DataNodeArray extends DataNodeAbstract
{    
    /** common initializers */
    private void init()
    {
        _bseq_cache = new Cache();
        _bsize_cache=new Cache();
        _intvalue_cache=new Cache();
        _children= new ArrayList() ;
        
    }
    private DataNodeArray ()
    {
        super();
        init();
    }
    
    /** Creates a new instance of DataNodeArray 
     * the dummy variable is only to keep a refernce of 
     * what type the elements of this array are
     */
    public DataNodeArray (DataNodeAbstract dummy)
    {
        super();
        init();
        _dummy=dummy;
    }
    
    /** should invoke copy constructor for base class */
    public DataNodeArray (DataNodeArray data)
    {
        super(data);
        init();
        _children = data._children;
    }
    
    /** return the unique typename */
    public String get_type_name() {return "ARRAY";}
    
    public String get_bitsequence_value() 
    {
        if(_bseq_cache.is_valid ())
        {
            return _bseq_cache.as_string ();
        }
        String ret="";
        for(int i=0;i<_children.size() ; i++ )
        {
            ret=ret+ ((DataNodeAbstract)_children.get (i)).get_bitsequence_value ();
        }
        _bseq_cache.set (ret);
        return ret;
    }
    public int get_bit_size()  
    { 
        if(_bsize_cache.is_valid ())
        {
            return _bseq_cache.as_int();
        }
        int ret=0;
        for(int i=0;i<_children.size() ; i++ )
        {
            ret+=((DataNodeAbstract)_children.get (i)).get_bit_size ();
        }
        _bsize_cache.set (ret);
        return ret;
    }
    
    
    public int get_int_value() throws Exception
    {
        if(_intvalue_cache.is_valid ())
        {
            return _intvalue_cache.as_int ();
        }
        
        int ret=0;
        String t=get_bitsequence_value();
        if(t.length ()<_offset)
        {
            throw new Exception("Underflow in get_int_val. offset too big ?");
        }
        else
        {
            for(int i=_offset;i<t.length () && i<32+_offset ; i++)
            {
                ret=ret<<1;
                if(t.charAt (i)=='1')
                {
                    ret+=1;
                }
            }
        }
        _intvalue_cache.set (ret);
        return ret;
    }
    
    /** returns true if this array is a * array */
    public boolean get_is_unlimited() {return _isunlimited;}
    public void set_is_unlimited(boolean isunlimited) {_isunlimited=isunlimited;}
    
    /** returns size of the array in num of elements */
    public int get_size() {return _children.size ();}
    
    /** returns the ith element from the array */
    public DataNodeAbstract get_element(int i) throws Exception
    { 
        if(i>=_children.size ())
        {
            throw new Exception("DataNodeArrayIndexOutOfBound");
        }
        return (DataNodeAbstract)_children.get (i);
    }
    
    /** sets the ith element of the array 
     * you cannot set the size of the star-array by just accessing
     * an unexisting element. Thus, we will thro an exception here
     * regardless of whether we have anormal or a star array.
     * @throws exception
     */
    public void set_element(int i,DataNodeAbstract data) throws Exception
    {
        if(data.get_type_name () != _dummy.get_type_name ())
        {
            throw new Exception("incompatible types ! all elemens of array must be of the defined type.");
        }
        if(i>=_children.size ())
        {
            throw new Exception("DataNodeArray: Index Out Of Bound in set_element");
        }
        _children.set(i,data);
        _invalid_cache();
    }
    
    /** appends an element to the end of the array */
    public void append_element(DataNodeAbstract data) throws Exception
    {
        if(data.get_type_name () != _dummy.get_type_name ())
        {
            throw new Exception("incompatible types ! all elemens of array must be of the defined type.");
        }
        
        _children.add(data);
        _invalid_cache();
    }
        
    /** returns the type of (all) the elements of the array 
     * string for now. the return value sould be a "type" class */
    public String get_element_type_name() { return _dummy.get_type_name () ; }
    
    /** returns offset of the "current pointer' witin the array.*/
    public int get_offset() {return _offset; }
    
    /** sets curret pointer within the array */
    public void set_offset(int offset) {_offset=offset;_intvalue_cache.invalid ();}
    
    
    
    /** private funciton to invalidate all caches */
    private void _invalid_cache()
    {
        _bseq_cache.invalid ();
        _bsize_cache.invalid ();
        _intvalue_cache.invalid ();
    }
    /**  class private data */
        
   /** current pointer within the array (in bits) */
    private int _offset;
    
    /** elements of the array */
    private List _children; 
    
    /** true if this array is a resizable (growable) array */
    private boolean _isunlimited;
    
    /** stores a dummy variable of the type of each element of the array */
    private DataNodeAbstract _dummy;
    
    /** bit sequence  cache */
    private Cache _bseq_cache;
    
    /** bit size cache */
    private Cache _bsize_cache;
    
    /** cache calculated int value */
    private Cache _intvalue_cache;
    
}
