/*
 * Cache.java
 *
 * Created on November 18, 2007, 3:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 * Simple class to cache bitsequences, array sizes etc
 * @author akshay
 */
public class Cache
{
    
    /** Creates a new instance of Cache */
    
    public Cache (Object ob)
    {
        _valid=true;
        _object_data=ob;
    }
    public Cache()
    {
        /* if there's nothing in the cache, whatever is in it is invalid */
        _valid=false;
    }
    
    void set(Object ob)
    {
        _valid=true;
        _object_data=ob;
    }
    int as_int()
    {
        return Integer.parseInt (_object_data.toString ());
    }
    
    String as_string()
    {
        return _object_data.toString ();
    }
    
    Object as_ob()
    {
        return _object_data;
    }
    
    void valid()
    {
        _valid=true;
    }
    
    void invalid()
    {
        _valid=false;
    }
    
    boolean is_valid()
    {
        return _valid;
    }
    
    private boolean _valid;
    private Object _object_data; 
}
