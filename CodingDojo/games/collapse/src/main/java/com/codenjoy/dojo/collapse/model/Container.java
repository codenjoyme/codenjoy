package com.codenjoy.dojo.collapse.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import java.util.*;

public class Container<T, V> implements Iterable<V> {
    private Map<T, V> data;

    public Container() {
        data = new HashMap<T, V>();
    }

    public Container(List<V> list) {
        this();
        for (V v : list) {
            data.put((T)v, v);
        }
    }

    public V get(T key) {
        return data.get(key);
    }

    public void add(V value) {
        data.put((T)value, value);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public V removeLast() {
        Iterator<T> iterator = data.keySet().iterator();
        if (!iterator.hasNext()) return null;
        T key = iterator.next();
        return data.remove(key);
    }

    @Override
    public Iterator<V> iterator() {
        return data.values().iterator();
    }

    public void remove(V value) {
        data.remove(value);
    }

    public Collection<V> values() {
        return data.values();
    }

    public int size() {
        return data.size();
    }

    public boolean contains(T value) {
        return data.containsKey(value);
    }
}
