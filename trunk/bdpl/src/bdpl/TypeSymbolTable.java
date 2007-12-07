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
public class TypeSymbolTable  
{
    /**
     * Creates a new instance of TypeSymbolTable
     */
    private Map _the_table;
    public TypeSymbolTable() 
    {
        _the_table=new HashMap();
        _the_table.put("int",new Type("int")); // predefined types 
        _the_table.put("bit",new Type("bit"));
        _the_table.put("byte",new Type("byte"));
        _the_table.put("ARRAY:byte",new Type("ARRAY",new DataNodeByte()));
        _the_table.put("ARRAY:int",new Type("ARRAY",new DataNodeInt()));
        _the_table.put("ARRAY:bit",new Type("ARRAY",new DataNodeBit()));
       
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
            throw new Exception(type+" : undefined type");  
        }
    }
    
    /** check if the symbol table containg this type already */
    public boolean contains(String type)
    {
        return _the_table.containsKey (type);
    }
}     
