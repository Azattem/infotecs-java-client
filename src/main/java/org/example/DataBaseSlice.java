package org.example;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class DataBaseSlice {
    private final Map<String,String> map= new HashMap<>();

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

    public void removeByIp(String ip){
        if(map.containsValue(ip)){
            for (Map.Entry<String,String> value : map.entrySet()) {
                if(value.getValue().equals(ip)){
                    map.remove(value.getKey());
                }
            }
        }
    }

    public boolean containsDomain(String domain){
    return map.containsKey(domain);
    }

    public boolean containsIp(String ip){
    return map.containsValue(ip);
    }

    public List<String[]> getAsSortedArray() {
        List<String[]> list = new ArrayList<>();
        for (Map.Entry<String,String> s : map.entrySet()) {
        list.add(new String[]{s.getKey(),s.getValue()});
        }
        list.sort(new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                char[] e1 = o1[0].toCharArray();
                char[] e2 = o2[0].toCharArray();
                for (int i = 0; i < (Math.min(e1.length, e2.length)); i++) {
                    if (e1[i] < e2[i]) {
                        return -1;
                    }
                    if (e1[i] > e2[i]) {
                        return 1;
                    }
                }
                return 0;
            }
        });
        return  list;
    }
}
