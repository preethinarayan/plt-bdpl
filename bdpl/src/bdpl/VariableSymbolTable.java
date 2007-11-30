/*
 * VariableSymbolTable.java
 *
 * Created on November 21, 2007, 11:44 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author preethi
 */
/**
 * class to create a symbol table to hold the identifiers and their and pointers to where they are stored
 */
import java.util.*;
public class VariableSymbolTable extends Hashtable<String,DataNodeAbstract>{
     private DataNodeAbstract _map_value;
     private DataNodeAbstract _retrieved;
     private String _map_key;
    /**
     * Creates a new instance of VariableSymbolTable
     */
    public VariableSymbolTable() {
    }
   
    
    /**
     * check for an existing key entry
     */
    public boolean checkEntry(String key)
    {
        if(containsKey(key))
        {
            return true;
        }
        else
        {
            return false;
        }         
    }
    /** 
     * method to add keys and values to the existing keys in the hashtable
     */
    public void addValue(String key,DataNodeAbstract value) throws Exception
    {
        _map_key=key;
        _map_value=value;
        if(checkEntry(key))
        {
            throw new Exception("Identifier"+key+" already exists");
        }
        else
        {
            put(_map_key,_map_value);
        }
    }
    
    /**
     * method to retrieve values corresponding to the identifiers from the table if they exist
     */
    public DataNodeAbstract getValue(String key) throws Exception
    {
        
       
        if(checkEntry(key))
        {
            _retrieved=get(key);
            return _retrieved;
        }
        else
        {
            throw new Exception("Identifier "+key+" not declared");
        }
            
        
    }
            
}
