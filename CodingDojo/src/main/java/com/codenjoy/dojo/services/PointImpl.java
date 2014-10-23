package com.codenjoy.dojo.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Каждый объект на поле имеет свои координаты. Этот класс обычно используется дял указания координат или как родитель.
 * Может использоваться в коллекциях.
 */
public class PointImpl implements Point, Comparable<Point> {
    protected int x;
    protected int y;

    public PointImpl(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PointImpl(Point point) {
        this(point.getX(), point.getY());
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public boolean itsMe(Point pt) {
        return itsMe(pt.getX(), pt.getY());
    }

    public boolean itsMe(int x, int y) {
        return this.x == x && this.y == y;
    }

    @Override
    public boolean isOutOf(int size) {
        return x < 0 || y < 0 || y > size - 1 || x > size - 1;
    }

    @Override
    public int hashCode() {
        return x*1000 + y;
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]", x, y);
    }

    public static long counter = 0;
//    public static Map<String, Integer> count = new HashMap<String, Integer>();

    @Override
    public boolean equals(Object o) {
        counter++; // TODO переделать нафиг, этот метод за один тик вызыввется ~30 миллионов раз на 26 игроках
        if (o == null) {
            return false;
        }

//        if (counter % 10000 == 0) System.out.print("+");
//        Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
//        for (StackTraceElement[] list : traces.values()) {
//            if (list.length == 0) continue;
//            boolean mine = list[0].getMethodName().equals("dumpThreads");
//            if (!mine) continue;
//
//            StringBuilder builder = new StringBuilder();
//            for (StackTraceElement el : list) {
//                if (el.getFileName().equals("Thread.java")) continue;
//                builder.append(el.getClassName()).append(".")
//                        .append(el.getMethodName()).append("():")
//                        .append(el.getLineNumber()).append("\n");
//            }
//            String id = builder.toString() ;
//
//            int n = 0;
//            if (count.containsKey(id)) {
//                n = count.get(id) + 1;
//            }
//            count.put(id, n);
//        }
//        if (count.size() % 10000 == 0) System.out.println(count.toString());


        if (!(o instanceof PointImpl)) {
            return false;
        }

        PointImpl p = (PointImpl)o;

        return (p.x == x && p.y == y);
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(Point pt) {
        this.x = pt.getX();
        this.y = pt.getY();
    }

    public PointImpl copy() {
        return new PointImpl(this);
    }

    public void change(Point delta) {
        x += delta.getX();
        y += delta.getY();
    }

    public static Point pt(int x, int y) {
        return new PointImpl(x, y);
    }

    @Override
    public int compareTo(Point o) {
        if (o == null) {
            return -1;
        }
        return Integer.valueOf(this.hashCode()).compareTo(Integer.valueOf(o.hashCode()));
    }
}
