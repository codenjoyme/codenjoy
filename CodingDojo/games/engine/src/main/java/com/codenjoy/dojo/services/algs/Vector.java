package com.codenjoy.dojo.services.algs;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

/**
 * Клас инкапсулирующий намерение отправиться из точки
 * from в точку to по направлению direction.
 * Рейтинг позволяет понимать насколько оптимален этот путь - расчитывается как
 * сумма 2х длинн - double расстояния по диагонали до точки назначения и длинны
 * уже построенного до этой точки маршрута. То есть выбирая направления куда
 * можно попробовать пойти в поиске кратчайшго пути мы выбираем те направления
 * которые одновременно и ближе к финальной точке (наивно полагая, что путь к ней чист)
 * и при этом длинна нашего маршрута так же должна оставаться минимальной по
 * сравнению с другими вариантами (работает в случае если есть препятствия
 * их нужно ободить). Если мы поставим raining - 0 то получим классический
 * алгоритм Дейкстры, иначе это алгоритм A* и он быстрее находит цель.
 */
public class Vector implements Comparable<Vector> {

    private Point to;
    private Point from;
    private Direction where;
    private int rating;

    public Vector(Point from, Direction where, Point goal, int pathLength) {
        this.from = from;
        this.where = where;
        this.to = where.change(from);
        this.rating = (int)(1000*(to.distance(goal) + pathLength));
    }

    @Override
    public String toString() {
        return String.format("Vector{%s->%s, %s, %s}",
                from,
                to,
                where.name().charAt(0),
                rating);
    }

    @Override
    public int compareTo(Vector o) {
        return Integer.compare(rating, o.rating);
    }

    public int rating() {
        return rating;
    }

    public Point to() {
        return to;
    }

    public Point from() {
        return from;
    }

    public Direction where() {
        return where;
    }
}
