/*
 * Cache.java
 *
 * Simple class to cache bitsequences, array sizes etc
 */

/**
 *
 * @author Akshay Pundle
 */
public class Cache
{
    
    /** Creates a new instance of Cache with some data (that is the cached data) */
    public Cache (Object ob)
    {
        _valid=true;
        _object_data=ob;
    }
    /** Creates a new instance of Cache with NO cached data
     * if there's nothing in the cache, whatever is in it is invalid 
     * so set valid=false here
     */
    public Cache()
    {
        _valid=false;
    }
    
    /** "set" the cache , i.e cache a new object */
    public void set(Object ob)
    {
        _valid=true;
        _object_data=ob;
    }
    
    /** retreive cached object as an integer */
    public int as_int()
    {
        return Integer.parseInt (_object_data.toString ());
    }
    
    /** retreive cached object as a string */
    public String as_string()
    {
        return _object_data.toString ();
    }
    
    /** retreive cached object as an object */
    public Object as_ob()
    {
        return _object_data;
    }
    
    /** set the cache as "valid" 
     * have made this method private because
     * there shouldnt be any way of saying this from outside
     */
    private void valid()
    {
        _valid=true;
    }
    
    /** make the cache invalid */
    public void invalid()
    {
        _valid=false;
    }
    
    /** check if the cache is valid */
    public boolean is_valid()
    {
        return _valid;
    }
    
    /** class private data */
    /** is the object valid ? */
    private boolean _valid;
    /** the object data */
    private Object _object_data; 
}
