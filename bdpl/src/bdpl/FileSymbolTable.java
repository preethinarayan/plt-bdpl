/*
 * FileSymbolTable.java
 *
 * Created on January 3, 2008, 7:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author akshay
 */
import java.util.*;
public class FileSymbolTable 
{
    private Map _the_table;
    private BdplFile _current_file;
    
    /** Creates a new instance of FileSymbolTable */
    public FileSymbolTable() 
    {
        _the_table=new HashMap();
        _current_file=null;
     
    }
    
        
    
    /**
     * insert the file into the table
     */
    public void insert(String id,BdplFile the_file) throws Exception
    {  
        if(_the_table.containsKey (id) )
        {
            System.err.println(id+" file already open. Reopening.");
        }
        if(the_file == null)
            throw new Exception("trying to open null file");
        
        _the_table.put (id,the_file);
                 
    }
    /**
     * method to retieve a file from the table.
     * when a file is retreived, it is set as
     * the current file. this will be used by
     * structures wishing to change the file
     * pointer.
     */
    public BdplFile get(String id) throws Exception
    {
        if(_the_table.containsKey(id))
        {
            _current_file=(BdplFile) _the_table.get (id);
            return _current_file;
        }   
        else
        {
            throw new Exception(id+" : undefined file identifier");   
        }
    }
    
    /** check if the symbol table containg this type already */
    public boolean contains(String id)
    {
        return _the_table.containsKey (id);
    }
    
    public void set_current_file_pointer(int offset) throws Exception
    {
        if(_current_file != null)
            _current_file.set_current_pointer (offset);
        else
            System.err.println ("Error : Cant set file pointer, no file read");
    }
    
    public int get_current_file_pointer()
    {
        if(_current_file != null)
        {
            return _current_file.get_current_pointer ();
        }
        else
        {
            System.err.println ("Error : Tried to access current file pointer before the first read");
            return 0;
        }
    }
    
}
