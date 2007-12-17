/*
 * TypeSymbolTable.java
 *
 */

/**
 *
 * @author Preethi Narayan, Akshay Pundle
 */

import java.util.*;
import antlr.collections.*;
public class TypeSymbolTable  
{
    /**
     * Creates a new instance of TypeSymbolTable
     */
    private Map _the_table;
    private TypeSymbolTable _parent; // pointer to parent
    public TypeSymbolTable() 
    {
        _the_table=new HashMap();
        _the_table.put("int",new Type("int")); // predefined types 
        _the_table.put("bit",new Type("bit"));
        _the_table.put("byte",new Type("byte"));
        _the_table.put("ARRAY:byte",new Type("ARRAY",new DataNodeByte()));
        _the_table.put("ARRAY:int",new Type("ARRAY",new DataNodeInt()));
        _the_table.put("ARRAY:bit",new Type("ARRAY",new DataNodeBit()));
        _parent=null;
       
    }
        
    public void set_parent(TypeSymbolTable parent)
    {
        _parent=parent;
    }
    public TypeSymbolTable get_parent()
    {
        return _parent;
    }        
    /**
     * check if entry exists in the table otherwise make an entry into this table and the variable symbol table
     */
    public void insert(String type_name,Type type_ob) throws Exception
    {  
        if(_the_table.containsKey (type_name) )
        {
            throw new Exception(type_name+" already exists in symbol table");
        }
        else
        {
             _the_table.put (type_name,type_ob);
        }
                 
    }
    /**
     *method to retieve a type node from the type symbol table
     */
    public Type get(String type) throws Exception
    {
        if(_the_table.containsKey(type))
        {
            return (Type) _the_table.get (type);
        }   
        else
        {
            if(_parent!=null)
            {
                return _parent.get (type);
                
            }
            else
            {
                throw new Exception(type+" : undefined type");  
            }
        }
    }
    
    /** check if the symbol table containg this type already */
    private boolean contains(String type)
    {
        if(_the_table.containsKey(type))
        {
            return true;
        }   
        else
        {
            if(_parent!=null)
            {
                return _parent.contains(type);
                
            }
            else
            {
                return false;
            }
        }
    }
}     
