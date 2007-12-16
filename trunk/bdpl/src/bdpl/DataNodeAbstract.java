/*
 * AbstractDataNode.java
 *
 */

/**
 *
 * @author Akshay Pundle
 */
import antlr.collections.*;
abstract public class DataNodeAbstract implements DataNode
{
    
    /** Creates a new instance of AbstractDataNode */
    private void init()
    {
        _fieldsize=0;
        _bitsize=0;
        _context=null;
        _fieldsize_ast=null;
        _name="";
        _verify_ast=null;
        _then_ast=null;
        _else_ast=null;
    }
    public DataNodeAbstract ()
    {
        init();
    }
    public DataNodeAbstract (DataNodeAbstract data)
    {
        init();
        _fieldsize=data._fieldsize;
        _bitsize=data._bitsize;
        _fieldsize_ast=data._fieldsize_ast;
        _then_ast=data._then_ast;
        _verify_ast=data._verify_ast;
        _name=data._name;
            
    }
    
    
    
    public Exception get_ex(String ex_string)
    {
        return new Exception(this.getClass().getName()  +": "+ex_string);
    }

    public void set_name(String name)
    {
        _name=name;
    }
    public String get_name() 
    {
        return _name;
    }
    abstract public void assign(DataNodeAbstract rhs);
    
   
    abstract public int get_max_accept();
    abstract public String get_type_name();
    abstract public String get_bitsequence_value();
    
    /** set the expression for number of bits this type takes*/
    public void set_fieldsize(AST fieldsize_ast)
    {
        _fieldsize_ast=fieldsize_ast;
    }
    
    /** sets the contaxt for evaluation of expressions for this data node*/
    public void set_context(VariableSymbolTable context)
    {
        _context=context;
    }
    
    protected int evaluate_fieldsize ()
    {
        BdplTreeParser par=new BdplTreeParser ();
        if(_context != null)
            par.varSymbTbl=_context;

        try
        {
            if(_fieldsize_ast != null)
            {
                
                int f=((DataNodeAbstract)par.expr (_fieldsize_ast)).get_int_value ();
                if( f>0 && (_bitsize<=0 || f<_bitsize  ) )
                {
                    _fieldsize=f;
                }
            }
                
            return _fieldsize;
            
        }
        catch (Exception e)
        {
            e.printStackTrace ();
        }
        return 0;
    }
    
    public void set_verify_then_else(AST verify_ast,AST then_ast , AST else_ast)
    {
        _verify_ast=verify_ast;
        _then_ast=then_ast;
        _else_ast=else_ast;
    }
    
    protected void evaluate_verify_then_else ()
    {
        BdplTreeParser par=new BdplTreeParser ();
        if(_context != null)
            par.varSymbTbl=_context;

        try
        {
            if (_verify_ast != null && ((DataNodeAbstract)par.expr (_verify_ast)).get_int_value ()!=0)
            {
                if(_then_ast!= null) par.stmts (_then_ast);
            }
            else
            {
                if(_else_ast!= null) par.stmts (_else_ast);
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace ();
        }

    }
    
    
    
    public int get_bit_size()  {return _bitsize;}
    
    abstract public int get_int_value() throws Exception ;
    
    /** @return the number of bits this type takes */
    public int get_fieldsize() { return _fieldsize;}
    
    /** set the number of bits this type takes
     * @return nonzero on error */
    public int set_fieldsize(int fieldsize) {_fieldsize=fieldsize;return 0;}
    
    abstract public void populate(BdplFile rhs) throws Exception ;
    
    /** print a human readable form of this node */
    abstract public String print();
    
    /** print a human readable form of this node */
    abstract public String print(int format);
    
    /** store the max size of the data in num of bits */ 
    protected int _fieldsize;
    /** stores the current size of the data in bits */
    protected int _bitsize;
    
    protected String _name;
    
    VariableSymbolTable _context;
    
    AST _fieldsize_ast;
    AST _verify_ast;
    AST _then_ast;
    AST _else_ast;
    
}
