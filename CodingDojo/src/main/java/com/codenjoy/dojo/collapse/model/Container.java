package com.codenjoy.dojo.collapse.model;

import com.codenjoy.dojo.services.Point;

import java.util.*;

/**
* Created by Sanja on 2014-12-05.
*/
public class Container<T extends Point, V extends Point> implements Iterable<V> {
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
        V result = data.get(key);
        data.remove(key);
        return result;
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
