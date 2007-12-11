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
        _intMap.put("TYPE_BYTE","int");
        _intMap.put("TYPE_BIT","int");
        _intMap.put("TYPE_INT","int");

        _bitMap = new HashMap();
        _bitMap.put("TYPE_BYTE","byte");
        _bitMap.put("TYPE_BIT","bit");
        _bitMap.put("TYPE_INT","int");

        _byteMap = new HashMap();
        _byteMap.put("TYPE_BYTE","byte");
        _byteMap.put("TYPE_BIT","byte");
        _byteMap.put("TYPE_INT","int");
        
        _map = new HashMap();
        _map.put("TYPE_INT",_intMap);
        _map.put("TYPE_BYTE",_byteMap);
        _map.put("TYPE_BIT",_bitMap);
    }
    
    public String get(String ltype, String rtype){
        HashMap map = (HashMap)_map.get(ltype);
        return (String)map.get(rtype);
    }
}
