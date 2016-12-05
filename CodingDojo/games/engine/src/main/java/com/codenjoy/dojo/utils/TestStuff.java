package com.codenjoy.dojo.utils;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


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
