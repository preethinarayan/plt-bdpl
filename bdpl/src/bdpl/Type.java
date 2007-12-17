/*
 * Type.java
 *
 */

/**
 *
 * @author Preethi Narayan, Akshay Pundle
 */
import antlr.collections.*;
public class Type 
{
    private String _type;
    private DataNodeAbstract _array_dummy;
    private DataNodeAbstract _struct_node;
    private AST _struct_ast;
    
    /** Creates a new instance of Type */
    public Type(String type) 
    {
        if( type=="bit" ||
            type=="byte" ||
            type=="int"
            )
        {
            _type=type;
        }
        else
        {
            _type="";
        }
    }
            
     /**
     *  constructor which takes the subtree and the type name
     *  type must be struct here
     */
    public Type(String type,DataNodeAbstract node)
    {
        if(type.startsWith ("ARRAY"))
        {
            _type=type;
            _array_dummy=node;
            _struct_node=null;
        }
        else if(type.startsWith ("struct"))
        {
            
            //_type=type;
            //_struct_node=node;
            //_array_dummy=null;
            _struct_node=null;
            _array_dummy=null;
            _type=null;            
        }
        else
        {
            _struct_node=null;
            _array_dummy=null;
            _type=null;
        }
        
        
    }
    
    
    public Type(String type,AST struct_ast)
    {
        if(type.startsWith ("struct"))
        {
            _struct_node=null;
            _array_dummy=null;
            _type=type;            
            _struct_ast=struct_ast;
        }
        else
        {
            _struct_node=null;
            _array_dummy=null;
            _type=null;
        }
        
        
    }
        
    /**
     *method to get the type of the type object
     */
    public DataNodeAbstract getDataNode() throws Exception
    {
        /*if(!st.contains (_type))
            throw new Exception(_type+" type not defined");*/
                
        if(_type.equals("int"))
        {
            return new DataNodeInt();
        }
        else if(_type.equals("bit"))
        {
            return new DataNodeBit();
        }
        else if(_type.equals("byte"))
        {
            return new DataNodeByte();
        }
        else if(_type.startsWith("ARRAY"))
        {
            throw new Exception("getDataNode() not implemented for struct : use getDataNode(int)");
        }
        else if(_type.startsWith("struct"))
        {
            throw new Exception("get data node not implemented for struct : use get_ast");
        }
        throw new Exception("bad type!");
    }

    
    
    public DataNodeAbstract getDataNode(int arr_size) throws Exception
    {
        if(_type.startsWith("ARRAY"))
        {
            return new DataNodeArray(_array_dummy,arr_size);
        }
        else 
        {
            throw new Exception("getDataNode(int) is only for making arrays");
        }
    }
        
    
    public AST get_ast()
    {
        return _struct_ast;
    }
    /**
     *method to return the type as a string
     */
    public String getTypeName()
    {
        return _type;
    }
    
    
}
