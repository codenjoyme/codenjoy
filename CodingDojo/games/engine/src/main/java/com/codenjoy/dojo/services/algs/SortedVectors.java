package com.codenjoy.dojo.services.algs;

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
