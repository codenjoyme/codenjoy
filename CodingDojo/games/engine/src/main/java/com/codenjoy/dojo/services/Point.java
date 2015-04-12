package com.codenjoy.dojo.services;

/**
 * Инкапсулирует координату элемента на поле. Все элементы поля должны наследоваться от PointImpl,
 * единственной реализации этого интерфейса.
 */
public interface Point extends Comparable<Point> {
    int getX();

    int getY();

    void move(int x, int y);

    void move(Point pt);

    PointImpl copy();

    boolean itsMe(Point point);

    boolean itsMe(int x, int y);

    boolean isOutOf(int size);

    boolean isOutOf(int dw, int dh, int size);

    double distance(Point point2);

    void change(Point delta);
}
