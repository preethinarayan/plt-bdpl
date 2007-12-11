/*
 * Type.java
 *
 * Created on November 29, 2007, 9:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author preethi
 */
import antlr.collections.*;
public class Type 
{
    private AST _subtree;
    private String _type;
    private DataNodeAbstract _array_dummy;
    
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
    public Type(String type,AST subtree)
    {
        if(type.startsWith ("struct"))
        {
            _type=type;
            _subtree=subtree;
        }
        else
        {
            _type="";
            _subtree=null;
        }
        
    }
     /**
     *  constructor which takes the subtree and the type name
     *  type must be struct here
     */
    public Type(String type,DataNodeAbstract array_dummy)
    {
        if(type.startsWith ("ARRAY"))
        {
            _type=type;
            _array_dummy=array_dummy;
        }
        else
        {
            _type="";
            _subtree=null;
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
            // TODO array size.
            return new DataNodeArray(_array_dummy);
        }
        else if(_type.startsWith("struct"))
        {
            DataNodeStruct retval=new DataNodeStruct();
            assert(_subtree!=null);
            AST body=_subtree.getFirstChild ();//.getNextSibling ();
            System.out.println (body.getText ());
            return retval;
        }
        throw new Exception("bad type!");
    }
    /**
     *method to return the type as a string
     */
    public String getTypeName()
    {
        return _type;
    }
    
    
}
