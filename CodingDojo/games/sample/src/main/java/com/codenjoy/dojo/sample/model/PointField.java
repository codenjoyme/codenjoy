package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.services.Point;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PointField {

    private PointList[][] field;

    static class PointList {

        private Map<Class, Point> list = new HashMap<>();

        public void add(Point point) {
            list.put(point.getClass(), point);
        }

        public boolean contains(Class<?> filter) {
            return list.containsKey(filter);
        }

        public void remove(Class<?> filter) {
            list.remove(filter);
        }

        public <T> T get(Class<T> filter) {
            return (T) list.get(filter);
        }
    }

    public PointField(int size) {
        field = new PointList[size][];
        for (int x = 0; x < size; x++) {
            field[x] = new PointList[size];
            for (int y = 0; y < size; y++) {
                field[x][y] = new PointList();
            }
        }
    }

    public int size() {
        return field.length;
    }

    public void addAll(List<? extends Point> elements) {
        elements.forEach(this::add);
    }

    public void add(Point point) {
        get(point).add(point);
    }

    private PointList get(Point point) {
        return field[point.getX()][point.getY()];
    }

    public <T extends Point> boolean contains(Class<?> filter, T element) {
        return get(element).contains(filter);
    }

    public <T extends Point> void remove(Class<?> filter, T element) {
        get(element).remove(filter);
    }

    public <T> List<T> getAll(Class<T> filter) {
        List<T> result = new LinkedList<>();
        for (int x = 0; x < size(); x++) {
            for (int y = 0; y < size(); y++) {
                T element = field[x][y].get(filter);
                if (element != null) {
                    result.add(element);
                }
            }
        }
        return result;
    }

}