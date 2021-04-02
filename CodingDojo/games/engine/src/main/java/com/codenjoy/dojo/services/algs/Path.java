package com.codenjoy.dojo.services.algs;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Оптимизированная версия Map<Point, List<Direction>>.
 * Хранит список направлений движений, которым надо следовать
 * чтобы прийти из from в заданную точку.
 */
public class Path {

    private List<Direction>[][] all;

    public Path(int size) {
        all = new ArrayList[size][size];
    }

    private List<Direction> setList(Point pt, List<Direction> list) {
        return all[pt.getX()][pt.getY()] = list;
    }

    private List<Direction> getList(Point pt) {
        return all[pt.getX()][pt.getY()];
    }

    public List<Direction> get(Point pt) {
        List<Direction> list = getList(pt);
        if (list != null) {
            return list;
        }
        list = new ArrayList(100);
        setList(pt, list);
        return list;
    }
}
