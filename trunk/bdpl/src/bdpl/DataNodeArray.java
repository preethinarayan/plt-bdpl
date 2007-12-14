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
                child=new DataNodeBit ();
            }
            else if(_dummy.getClass ().getCanonicalName ().equals ("DataNodeByte"))
            {
                child=new DataNodeByte ();
            }
            else if(_dummy.getClass ().getCanonicalName ().equals ("DataNodeInt"))
            {
                child=new DataNodeInt ();
            }
            else if(_dummy.getClass ().getCanonicalName ().equals ("DataNodeArray"))
            {
                throw new BdplException ("array or arrays not allowed");
            }
            else if(_dummy.getClass ().getCanonicalName ().equals ("DataNodeStruct"))
            {
                child=new DataNodeStruct ( (DataNodeStruct) _dummy  );
                VariableSymbolTable v=new VariableSymbolTable();
                TypeSymbolTable t=new TypeSymbolTable();
                v.set_parent (((DataNodeStruct) _dummy).get_scope_var ());
                t.set_parent (((DataNodeStruct) _dummy).get_scope_type ());
                ((DataNodeStruct)child).set_scope (v,t);
            }
            try
            {
                _children.add (child);
                //append_element (child);
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
        return 0;
    }
    public void assign(DataNodeAbstract rhs)
    {
        if(rhs.getClass ().getCanonicalName ().equals (("DataNodeArray")))
        {
            // copying from array to array
        }
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
        int i=0;
        if(_isunlimited)
        {
            while( rhs.num_readable_bits ()>0  )
            {           
                add_n_elements(1);
                ((DataNodeAbstract)_children.get (_children.size ()-1)).populate (rhs);
            }
        }
        else
        {
            evaluate_and_resize();
            while( rhs.num_readable_bits ()>0 && i<_children.size () )
            {
                ((DataNodeAbstract)_children.get (i)).populate (rhs);
                i++;
            }
        }
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
