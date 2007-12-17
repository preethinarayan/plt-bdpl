/*
 * DataNodeStruct.java
 *
 */

/**
 *
 * @author Akshay Pundle
 */

import java.util.*;
public class DataNodeStruct extends DataNodeAbstract
{
    

    /** common initializers */
    private void init()
    {
        _bseq_cache = new Cache();
        _bsize_cache=new Cache();
        _intvalue_cache=new Cache();
        _children= new HashMap() ;
        _sequence=new ArrayList();
        _scope_type_pointer=null;
        _scope_var_pointer=null;
        _type_name="TYPE_STRUCT";
        
    }
    
    /** sets a pointer to the new symbol table for this struct */
    public void set_scope(VariableSymbolTable spv,TypeSymbolTable spt)
    {
        _scope_type_pointer=spt;
        _scope_var_pointer=spv;
    }
    /** get the symbol table pointer for this struct */
    public VariableSymbolTable get_scope_var()
    {
        return _scope_var_pointer;
    }
    /** get the symbol table pointer for this struct */
    public TypeSymbolTable get_scope_type()
    {
        return _scope_type_pointer;
    }
    /** Creates a new instance of DataNodeStruct */
    public DataNodeStruct ()
    {
        super();
        init();
    }
    
    
    /** should invoke copy constructor for base class */
    public DataNodeStruct(DataNodeStruct data)
    {
        super(data);
        init();
        _scope_var_pointer=new VariableSymbolTable();
        _scope_type_pointer=data._scope_type_pointer;
        for(int i=0;i<data._sequence.size ();i++)
        {   
            DataNodeAbstract child=null;
            _sequence.add (data._sequence.get (i));
            DataNodeAbstract d = (DataNodeAbstract)data._children.get (data._sequence.get (i));
            
            if(d.getClass ().getCanonicalName ().equals ("DataNodeBit"))
            {
                child=new DataNodeBit(d);
            } 
            else if(d.getClass ().getCanonicalName ().equals ("DataNodeByte"))
            {
                child=new DataNodeByte(d);
            } 
            else if(d.getClass ().getCanonicalName ().equals ("DataNodeInt"))
            {
                child=new DataNodeInt(d);
            } 
            else if(d.getClass ().getCanonicalName ().equals ("DataNodeArray"))
            {
                if(((DataNodeArray)d).get_ast()!=null)
                {
                    child=new DataNodeArray( ((DataNodeArray)d).get_dummy(),((DataNodeArray)d).get_ast()  );
                }
                else
                {
                    child=new DataNodeArray( ((DataNodeArray)d).get_dummy(),((DataNodeArray)d).get_size()  );
                }
                ((DataNodeArray)child).set_scope (_scope_var_pointer);
                ((DataNodeArray)child).set_is_unlimited ( ((DataNodeArray)d).get_is_unlimited ());
            } 
            else if(d.getClass ().getCanonicalName ().equals ("DataNodeStruct"))
            {
                child=new DataNodeStruct( (DataNodeStruct) d  );
                ((DataNodeStruct)child).get_scope_var ().set_parent (_scope_var_pointer);
            }
            _children.put (data._sequence.get (i),child );
            try
            {
                child.set_context (_scope_var_pointer);
                child.set_name (d.get_name ());
                _scope_var_pointer.insert ((String)data._sequence.get (i),child);
            }
            catch (Exception e)
            {
                e.printStackTrace ();
            }
        }
        _type_name=data._type_name;

    }
    
    public int get_max_accept ()
    {
        
        int max=0;
        for(int i=0;i<_sequence.size();i++)
        {
            int ret=((DataNodeAbstract) _children.get ( _sequence.get (i))).get_max_accept ();  
            if(ret<0)
                return ret;
            else
                max+=ret;
            
        }
        return max;
    }
    /* lets see if the base class function works
    public void assign(DataNodeAbstract rhs)
    {
        evaluate_verify_then_else();
    }
     */
    /** return the unique typename */
    public String get_type_name() 
    {
        return _type_name;
    }
    /** set the type name of this struct */
    public void set_type_name(String type_name)
    {
        _type_name=type_name;
    }
    
    /** @return concatenation of bit_sequence of all children */
    public String get_bitsequence_value() 
    {
        if(_bseq_cache.is_valid ())
        {
            return _bseq_cache.as_string ();
        }
        String ret="";
        for (int i=0;i<_sequence.size ();i++)
        {
            ret+= ((DataNodeAbstract)_children.get ( (String)_sequence.get (i) )).get_bitsequence_value();
        }
        _bseq_cache.set (ret);
        return ret;
    }
    
    /** @return current size of struct in bits */
    public int get_bit_size ()
    {
        if(_bsize_cache.is_valid ())
        {
            return _bsize_cache.as_int ();
        }
        
        int ret=0;
        
        for (int i=0;i<_sequence.size ();i++)
        {
            ret+= ((DataNodeAbstract)_children.get ( _sequence.get (i))).get_bit_size ();
        }
        _bsize_cache.set (ret);
        return ret;
    }
    
    /** gets the next 4 bytes (as int) from the struct. increments 
     * ofset pointer. 
     * @return the next (upto) 4 bytes from the struct as int */
    
    public int get_int_value () throws Exception
    {
        if(_intvalue_cache.is_valid ())
        {
            return _intvalue_cache.as_int ();
        }
        
        int ret=0;
        String t=get_bitsequence_value ();
        if(t.length ()<_offset)
        {
            throw get_ex ("Underflow in get_int_val. offset too big ?");
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
    
    /** size of struct presumably in number of children*/
    public int get_size() 
    {
        return _children.size ();
    }
    
    /** returns child(element of the struct) given name of the child */
    public DataNodeAbstract get_child_by_name (String name) throws Exception
    {
        if(_children.containsKey (name))
        {
            return (DataNodeAbstract) _children.get (name);
            
        }
        else
        {
            throw get_ex ("no child found named "+name);
        }
    }
    
    /** initialize / set a child by name */
    public void set_child_by_name(String name,DataNodeAbstract  value) throws Exception
    { 
        if(_children.containsKey (name))
        {
            throw get_ex("child already declared : "+name);
        }
        else
        {
            _children.put (name,value);
            _sequence.add (name); //remember the sequence number of this child
        }
        evaluate_verify_then_else();
    }
    
    /** returns child(element of the struct) given number of the child */
    public DataNodeAbstract get_child_by_num(int num) throws Exception
    { 
        if(num>_children.size ())
            throw new Exception("index out of bounds in get_child_by_num");
        return (DataNodeAbstract)_children.get(_sequence.get (num));
    }
    
    /** @return when implemented, will return all children(elements of the struct) */
    public DataNodeAbstract get_all_children(int num) throws Exception 
    { 
        throw get_ex("get_all_children not implemented");
    }
    
    /** returns number of elements the struct has */
    public int get_num_children() 
    { 
        return _children.size ();
    }
    
    /** returns the type of a child of the struct 
     * string for now. the return value sould be a "type" class */
    public String get_child_type_by_name(String name) throws Exception
    {
        if(_children.containsKey (name))
        {
            return ((DataNodeAbstract) _children.get (name)).get_type_name ();

        }
        else
        {
            throw get_ex("no child found named "+name);
        }
    }
    
    /** current pointer within the struct */
    public int get_offset() {return _offset; }
    
    /** set current pointer within the struct */
    public void set_offset(int offset) {_offset=offset;}
    
    public String print()
    {
        String ret=get_type_name ()+"\n{\n";
        for (int i=0;i<_sequence.size ();i++)
        {
            String key= (String)_sequence.get (i);
            ret+= "\t"+  key+" => " + ((DataNodeAbstract)_children.get (key)).print().replaceAll ("\n","\n\t") +",\n";
        }
        return ret+"}\n";
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
        int ma=get_max_accept();
        evaluate_fieldsize ();
        if( _fieldsize>0 && ( _fieldsize < ma || ma==-1))
        {
            BdplMemFile mem_file=new BdplMemFile(rhs,_fieldsize);
            read_only_fieldsize=true;
            temprhs=mem_file;
            num_reading=_fieldsize;
        }
        
        int i=0;
        while( temprhs.num_readable_bits ()>0 && i<_sequence.size ())
        {
            DataNodeAbstract child= (DataNodeAbstract)_children.get (_sequence.get (i) );
            child.populate (temprhs);
            i++;
        }
        
        if(read_only_fieldsize)
        {
            rhs.read_n_bits (num_reading-temprhs.num_readable_bits ()); // consume bits from rhs
        }
        evaluate_verify_then_else();
    }
    
    private int _offset;
    
    private String _type_name;
    
    private HashMap _children;
    
    /** stores the sequence of children */
    private List _sequence;
    
    /** bit sequence  cache */
    private Cache _bseq_cache;
    
    /** bit size cache */
    private Cache _bsize_cache;
    
    /** cache calculated int value */
    private Cache _intvalue_cache;
    
    private VariableSymbolTable _scope_var_pointer;
    private TypeSymbolTable _scope_type_pointer;
    
}
