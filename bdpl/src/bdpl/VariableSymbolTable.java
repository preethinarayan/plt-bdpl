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
public class VariableSymbolTable  
{
    /**
     * Creates a new instance of TypeSymbolTable
     */
    private Map _the_table;
    private VariableSymbolTable _parent;
    
    public VariableSymbolTable() 
    {
        _the_table=new HashMap();
        _parent=null;
    }
    
    public void set_parent(VariableSymbolTable parent)
    {
        _parent=parent;
    }
    public VariableSymbolTable get_parent()
    {
        return _parent;
    }
        
    
    /**
     * check if entry exists in the table otherwise make an entry into this table and the variable symbol table
     */
    public void insert(String id,DataNodeAbstract datanode) throws Exception
    {  
        if(_the_table.containsKey (id) )
        {
            throw new Exception(id+" already exists in symbol table");
        }
        else
        {
             _the_table.put (id,datanode);
        }
                 
    }
    /**
     *method to retieve a type node from the type symbol table
     */
    public DataNodeAbstract get(String id) throws Exception
    {
        if(_the_table.containsKey(id))
        {
            return (DataNodeAbstract) _the_table.get (id);
        }   
        else
        {
            if(_parent!=null)
            {
                return _parent.get (id);
                
            }
            else
            {
                throw new Exception(id+" : undefined identifier");  
            }
        }
    }
    
    /** check if the symbol table containg this type already */
    public boolean contains(String id)
    {
        return _the_table.containsKey (id);
    }
}     

