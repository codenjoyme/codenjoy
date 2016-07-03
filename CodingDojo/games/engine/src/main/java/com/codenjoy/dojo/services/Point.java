package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
