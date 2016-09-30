package com.epam.dojo.icancode;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TreeSet;

/**
 * Created by indigo on 2016-09-30.
 */
public class TestStuff {
    // because http://stackoverflow.com/a/17229462
    public static LinkedHashMap<String, Object> sorting(JSONObject expected) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        Iterator<String> keys = expected.keys();
        TreeSet<String> set = new TreeSet<>();
        while (keys.hasNext()) {
            set.add(keys.next());
        }
        for (String key : set) {
            map.put(key, expected.get(key));
        }
        return map;
    }
}
