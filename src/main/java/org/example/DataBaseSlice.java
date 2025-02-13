package org.example;

import java.util.HashMap;
import java.util.Map;

public class DataBaseSlice {
    private Map<String,String> map= new HashMap<>();

    public void add(String domain,String ip){
    map.put(domain,ip);
    }

    public String getByDomain(String domain){
    if(map.containsKey(domain)){
        return map.get(domain);
    }
    return null;
    }

    public String getByIp(String Ip){
    if(map.containsValue(Ip)){
        for (Map.Entry<String,String> value : map.entrySet()) {
            if(value.getValue().equals(Ip)){
            return value.getKey();
            }
        }
    }
    return null;
    }

    public void removeByDomain(String domain){
    map.remove(domain);
    }

    public void removeByIp(String Ip){
        if(map.containsValue(Ip)){
            for (Map.Entry<String,String> value : map.entrySet()) {
                if(value.getValue().equals(Ip)){
                    map.remove(value.getKey());
                }
            }
        }
    }

    public Map<String, String> getMap() {
        return map;
    }
}
