/*
 * TypeSymbolTable.java
 *
 * Created on November 26, 2007, 4:40 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author preethi
 */

import java.util.*;
import antlr.collections.*;
public class TypeSymbolTable extends Hashtable<String,Type> {
    private DataNodeAbstract _data_node;
    private String _type_name;
    private Type _value;
    public VariableSymbolTable vst=new VariableSymbolTable();
    /**
     * Creates a new instance of TypeSymbolTable
     */
    public TypeSymbolTable() {
        put("int",new Type("int",null)); // predefined types 
        put("bit",new Type("bit",null));
        put("byte",new Type("byte",null));
       
    }
        
    public String setTypeName(String type,String tag)
    {
        type=type+"_"+tag;
        _type_name=type;
        return _type_name;
    }
    
    /**
     * check if entry exists in the table otherwise make an entry into this table and the variable symbol table
     */
    public void makeTypeObject(String type,AST subtree) 
    {  
        if(containsKey(type)) ;
        else
         {
             _value=new Type(_type_name,subtree);
             put(type,_value);     
         }
                 
    }
    /**
     *method to retieve a type node from the type symbol table
     */
    public Type getTypeObject(String type) throws Exception
    {
        if(containsKey(type))
        {
            _value=get(type);
            return _value;
        }   
        else
            throw new Exception("Invalid "+type+" type");  
    }
   
    
}     
