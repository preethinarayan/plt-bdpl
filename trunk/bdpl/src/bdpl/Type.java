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
public class Type {
    private AST _subtree;
    private String _type;
    private DataNodeAbstract _data_node;
    /** Creates a new instance of Type */
    public Type() {
    }
    /**
     *constructor which takes the subtree and the type name
     */
    public Type(String type,AST subtree)
    {
        _type=type;
        _subtree=subtree;
        
    }
    /**
     *method to get the type of the type object
     */
    public DataNodeAbstract getTypeNode()
    {
        if(_type.equals("int"))
        {
            _data_node=new DataNodeInt();
        }
        else if(_type.equals("bit"))
        {
            _data_node=new DataNodeBit();
        }
        else if(_type.equals("byte"))
        {
            _data_node=new DataNodeByte();
        }
        else if(_type.startsWith("array"))
        {
            _data_node=new DataNodeArray(new DataNodeInt());
        }
        else if(_type.startsWith("struct"))
        {
            _data_node=new DataNodeStruct();
        }
        return _data_node;
    }
    /**
     *method to return the type as a string
     */
    public String getTypeName()
    {
        return _type;
    }
    
}
