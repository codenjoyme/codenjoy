package com.codenjoy.dojo.client;

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


import com.codenjoy.dojo.services.CharElements;
import com.codenjoy.dojo.services.Point;

import java.util.*;

public abstract class AbstractBoard<E extends CharElements> extends AbstractLayeredBoard<E> {

    public abstract E valueOf(char ch);

    public AbstractBoard forString(String boardString) {
        return (AbstractBoard)super.forString(boardString);
    }

    @Override
    public AbstractBoard forString(String... layers) {
        return (AbstractBoard) super.forString(layers);
    }

    public List<Point> get(E... elements) {
        return get(0, elements);
    }

    public boolean isAt(int x, int y, E element) {
        return isAt(0, x, y, element);
    }

    public E getAt(int x, int y) {
        return getAt(0, x, y);
    }

    public String boardAsString() {
        return boardAsString(0);
    }

    public boolean isAt(int x, int y, E... elements) {
        return isAt(0, x, y, elements);
    }

    public boolean isNear(int x, int y, E element) {
        return isNear(0, x, y, element);
    }

    public int countNear(int x, int y, E element) {
        return countNear(0, x, y, element);
    }

    public List<E> getNear(int x, int y) {
        return getNear(0, x, y);
    }

    public void set(int x, int y, char ch) {
        set(0, x, y, ch);
    }

    public char[][] getField() {
        return getField(0);
    }

}
