/*
 * DataNodeArray.java
 *
 */

/**
 *
 * @author Akshay Pundle
 */

import java.util.*;
import antlr.collections.AST;

/** Implementsa an array node */
public class DataNodeArray extends DataNodeAbstract
{    
    /** common initializers */
    private void init()
    {
        _bseq_cache = new Cache();
        _bsize_cache=new Cache();
        _intvalue_cache=new Cache();
        _children= new ArrayList() ;
        _delayed_size=false;
        _size_evaled=false;
        
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
    
    /** Creates a new instance of DataNodeArray 
     * just like this array node
     */
    public DataNodeArray (DataNodeArray node)
    {
        super();
        init();    
        _dummy=node._dummy;
        _fieldsize_ast=node._fieldsize_ast;
        _verify_ast=node._verify_ast;
        _scope_var_pointer=node._scope_var_pointer;
    }
    
    public DataNodeArray (DataNodeAbstract dummy,AST _arr_size_expr)
    {
        super();
        init();
        _arr_size_expr_ast=_arr_size_expr;
        _isunlimited=false;
        _delayed_size=true;
        _size_evaled=false;
        _scope_var_pointer=null;
        _dummy=dummy;
    }
    public DataNodeArray (DataNodeAbstract dummy,int arr_size)
    {
        super();
        init();
        _dummy=dummy;
        add_n_elements(arr_size);
    }
    
    private void add_n_elements(int n)
    {
        for(int i=0;i< n; i++ )
        {
            DataNodeAbstract child=null;
            if(_dummy.getClass ().getCanonicalName ().equals ("DataNodeBit"))
            {
                child=new DataNodeBit (_dummy);
            }
            else if(_dummy.getClass ().getCanonicalName ().equals ("DataNodeByte"))
            {
                child=new DataNodeByte (_dummy);
            }
            else if(_dummy.getClass ().getCanonicalName ().equals ("DataNodeInt"))
            {
                child=new DataNodeInt (_dummy);
            }
            else if(_dummy.getClass ().getCanonicalName ().equals ("DataNodeArray"))
            {
                throw new BdplException ("array or arrays not allowed");
            }
            else if(_dummy.getClass ().getCanonicalName ().equals ("DataNodeStruct"))
            {
                child=new DataNodeStruct ( (DataNodeStruct) _dummy  );
                ((DataNodeStruct) child).get_scope_var ().set_parent (_scope_var_pointer);

            }
            try
            {
                child.set_context (_scope_var_pointer);
                _children.add (child);
            }
            catch( Exception e)
            {
                e.printStackTrace ();
            }
        }
        
    }
    
    /** sets a pointer to the new symbol table for this struct */
    public void set_scope(VariableSymbolTable spv)
    {
        _scope_var_pointer=spv;
    }
    /** get the symbol table pointer for this struct */
    public VariableSymbolTable get_scope_var()
    {
        return _scope_var_pointer;
    }

    public DataNodeAbstract get_dummy()
    {
        return _dummy;
    }
    /*
    /** should invoke copy constructor for base class *
    public DataNodeArray (DataNodeArray data)
    {
        super(data);
        init();
        _children = data._children;
    }*/
    
    
    public int get_max_accept()
    {
        if(_isunlimited)
            return -1;
        else 
        {
            int max=0;
            for(int i=0;i<_children.size ();i++)
            {
                int ret=((DataNodeAbstract)_children.get (i)).get_max_accept ();
                if(ret<0)
                    return ret;
                else
                    max+=ret;
                
            }
            return max;
        }
            
            
    }
    /* lets see if the base class function works
    public void assign(DataNodeAbstract rhs)
    {
        if(rhs.getClass ().getCanonicalName ().equals (("DataNodeArray")))
        {
            // copying from array to array
        }
        evaluate_verify_then_else();
    }*/
    
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
            throw get_ex("Underflow in get_int_val. offset too big ?");
        }
        else
        {
            int i=_offset;
            for(;i<t.length () && i<32+_offset ; i++)
            {
                ret=ret<<1;
                if(t.charAt (i)=='1')
                {
                    ret+=1;
                }
            }
            /** may be one after the last element in the array. Beware ! */
            _offset=i;
        }
        
        _intvalue_cache.set (ret);
        return ret;
    }
    
    /** returns true if this array is a * array */
    public boolean get_is_unlimited() {return _isunlimited;}
    public void set_is_unlimited(boolean isunlimited) {_isunlimited=isunlimited;}
    
    /** returns size of the array in num of elements */
    public int get_size ()
    {
        if(_delayed_size && !_size_evaled)
        {
            _size_evaled=true;
            evaluate_and_resize ();

        }
        return _children.size ();
    }

    /** returns the ith element from the array */
    public DataNodeAbstract get_element(int i) throws Exception
    { 
        if(i>=_children.size ())
        {
            throw get_ex("index out of bounds");
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
            throw get_ex("incompatible types ! all elemens of array must be of the defined type.");
        }
        
        if(_delayed_size && !_size_evaled)
        {
            _size_evaled=true;
            evaluate_and_resize ();
            
        }
        if(i>=_children.size ())
        {
            throw get_ex("Index Out Of Bound in set_element");
        }
        _children.set(i,data);
        _invalid_cache();
        evaluate_verify_then_else();
    }
    
    /** appends an element to the end of the array */
    public void append_element(DataNodeAbstract data) throws Exception
    {
        if(_delayed_size && !_size_evaled)
        {
            _size_evaled=true;
            evaluate_and_resize ();

        }
        if(data.get_type_name () != _dummy.get_type_name ())
        {
            throw get_ex("incompatible types ! all elemens of array must be of the defined type.");
        }
        
        _children.add(data);
        _invalid_cache();
        evaluate_verify_then_else();
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
    public String print()
    {
        String ret=get_type_name ()+" of " + _dummy.get_type_name ()+"\n[\n";
        for(int i=0;i<_children.size() ; i++ )
        {
            ret+= "\t"+String.valueOf (i)+" => " + ((DataNodeAbstract)_children.get (i)).print().replaceAll ("\n","\n\t")+",\n";
        }
        return ret+"]\n";
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
        BdplFile temprhs=rhs;
        boolean read_only_fieldsize=false;
        int num_reading=0;
        
        /** if our fieldsize if less than what the children want,
         * we will :
         *  1) make a copy of the data limited to ourfieldsize bits
         *  2) get the children to populate on this copy
         *  3) in the end detect how much input the children have consumed 
         *      an consume an equal amount form the actual source
         */
        evaluate_fieldsize ();
        int ma=get_max_accept ();
        if( _fieldsize>0  && (_fieldsize < get_max_accept ()  || _isunlimited || ma==-1) )
        {
            BdplMemFile mem_file=new BdplMemFile(rhs,_fieldsize);
            read_only_fieldsize=true;
            temprhs=mem_file;
            num_reading=_fieldsize;
        }
        int i=0;
        if(_isunlimited)
        {
            int num_readable=temprhs.num_readable_bits();
            while( temprhs.num_readable_bits ()>0  )
            {           
                add_n_elements(1);
                ((DataNodeAbstract)_children.get (_children.size ()-1)).populate (temprhs);
                if(temprhs.num_readable_bits () == num_readable)
                    break; // we arnt reading any data, better get outta here
                else
                    num_readable=temprhs.num_readable_bits ();
            }
        }
        else
        {
            evaluate_and_resize();
            // its ok if we even read 0 bytes in every iteration, no chance 
            // of an infinite loop here
            while( temprhs.num_readable_bits ()>0 && i<_children.size () )
            {
                ((DataNodeAbstract)_children.get (i)).populate (temprhs);
                i++;
            }
        }
        
        if(read_only_fieldsize)
        {
            rhs.read_n_bits (num_reading-temprhs.num_readable_bits ()); // consume bits from rhs
        }
        evaluate_verify_then_else();
        
        
    }
    
    private void evaluate_and_resize()
    {
        if(_delayed_size)
        {
            BdplTreeParser par=new BdplTreeParser();
            if(_scope_var_pointer != null)
                par.varSymbTbl=_scope_var_pointer;
            
            try
            {
                int siz=((DataNodeAbstract)par.expr (_arr_size_expr_ast)).get_int_value ();
                if(siz>_children.size ())
                {
                    add_n_elements (siz-_children.size ());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace ();
            }
            
        }
    }
       
    public AST get_ast()
    {
        return _arr_size_expr_ast;
    }
    
    /**  class private data */
    private AST _arr_size_expr_ast;
    boolean _delayed_size;
    boolean _size_evaled;
    private VariableSymbolTable _scope_var_pointer;
        
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
