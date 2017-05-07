/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ding3
 */
class MyLinkedMap<K, V> extends LinkedHashMap<K, V>
{

    public V getValue(int i)
    {

       Map.Entry<K, V>entry = this.getEntry(i);
       if(entry == null) return null;

       return entry.getValue();
    }
    
    public K getKey(int i)
    {
        Map.Entry<K, V>entry = this.getEntry(i);
        if(entry == null)return null;
        
        return entry.getKey();
    }

    public Map.Entry<K, V> getEntry(int i)
    {
        // check if negetive index provided
        Set<Map.Entry<K,V>>entries = entrySet();
        int j = 0;

        for(Map.Entry<K, V>entry : entries)
            if(j++ == i)return entry;

        return null;

    }

}