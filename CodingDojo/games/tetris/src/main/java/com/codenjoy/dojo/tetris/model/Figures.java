package com.codenjoy.dojo.tetris.model;

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


import com.codenjoy.dojo.tetris.model.levels.random.Randomizer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class Figures implements FigureQueue {
    public static final int DEFAULT_FUTURE_COUNT = 4;

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Type[] open = null;
    private Supplier<Randomizer> fetcher;
    private List<Type> figures = new LinkedList<>();
    private int futureCount;

    public Figures() {
        this(DEFAULT_FUTURE_COUNT);
    }

    public Figures(int futureCount) {
        this.futureCount = futureCount;
    }

    @Override
    public void setRandomizer(Supplier<Randomizer> fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public Figure next() {
        lock.readLock().lock();
        try {
            figures.add(generate());
            return figures.remove(0).create();
        } finally {
            lock.readLock().unlock();
        }
    }

    private Type generate() {
        return open[getRandomizer().getNextNumber(open.length)];
    }

    @Override
    public List<Type> future() {
        return Collections.unmodifiableList(new LinkedList<>(figures));
    }

    @Override
    public void clear() {
        figures.clear();
    }

    private Randomizer getRandomizer() {
        return fetcher.get();
    }

    public void open(Type... figures) {
        lock.writeLock().lock();
        try {
            open = figures;
            if (this.figures.isEmpty()) {
                fillFuture();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void fillFuture() {
        for (int i = 0; i < futureCount; i++) {
            figures.add(generate());
        }
    }
}
