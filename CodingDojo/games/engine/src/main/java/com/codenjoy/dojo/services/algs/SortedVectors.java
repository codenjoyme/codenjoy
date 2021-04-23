package com.codenjoy.dojo.services.algs;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Класс умеет добавлять элементы так, чтобы список оставался постортированным
 * по полю Vector.rating
 */
public class SortedVectors extends LinkedList<Vector> {

    @Override
    public boolean add(Vector vector) {
        if (isEmpty()) {
            super.add(vector);
            return true;
        }
        ListIterator<Vector> iterator = listIterator();
        boolean added = false;
        while (iterator.hasNext()) {
            if (iterator.next().rating() > vector.rating()) {
                super.add(iterator.previousIndex(), vector);
                added = true;
                break;
            }
        }
        if (!added) {
            super.add(vector);
        }
        return true;
    }

    @Override
    public void addFirst(Vector vector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addLast(Vector vector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends Vector> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends Vector> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, Vector element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void push(Vector vector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offer(Vector vector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offerFirst(Vector vector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offerLast(Vector vector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Vector set(int index, Vector element) {
        throw new UnsupportedOperationException();
    }
}
