/*
 * TypeConverter.java
 *
 * Created on December 11, 2007, 5:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Bharadwaj Vellore
 */

import java.util.*;

public class TypeConverter {

    private Map _map;
    private Map _intMap;
    private Map _byteMap;
    private Map _bitMap;

    /** Creates a new instance of TypeConverter */
    public TypeConverter() {
        _intMap = new HashMap();
        _intMap.put("byte","int");
        _intMap.put("bit","int");
        _intMap.put("int","int");

        _bitMap = new HashMap();
        _bitMap.put("byte","byte");
        _bitMap.put("bit","bit");
        _bitMap.put("int","int");

        _byteMap = new HashMap();
        _byteMap.put("byte","byte");
        _byteMap.put("bit","byte");
        _byteMap.put("int","int");
        
        _map = new HashMap();
        _map.put("int",_intMap);
        _map.put("byte",_byteMap);
        _map.put("bit",_bitMap);
    }
    
    public String get(String ltype, String rtype){
        HashMap map = (HashMap)_map.get(ltype);
        return (String)map.get(rtype);
    }
}
