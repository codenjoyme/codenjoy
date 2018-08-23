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
import com.codenjoy.dojo.tetris.model.levels.random.RandomizerFetcher;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PlayerFigures implements FigureQueue {
    // TODO вот тут вообще ничего не понятно :)
    public static final int DEFAULT_FUTURE_COUNT = 4;

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Type[] openFigures = null;
    private RandomizerFetcher randomizerFetcher;
    private List<Type> figures = new LinkedList<>();
    private int futureCount;

    public PlayerFigures() {
        this(DEFAULT_FUTURE_COUNT);
    }

    public PlayerFigures(int futureCount) {
        this.futureCount = futureCount;
    }

    public void setRandomizerFetcher(RandomizerFetcher randomizerFetcher) {
        this.randomizerFetcher = randomizerFetcher;
    }

    @Override
    public Figure next() {
        lock.readLock().lock();
        try {
            figures.add(generateNextFigure());
            return figures.remove(0).create();
        } finally {
            lock.readLock().unlock();
        }
    }

    private Type generateNextFigure() {
        return openFigures[getRandomizer().getNextNumber(openFigures.length)];
    }

    @Override
    public List<Type> getFutureFigures() {
        return Collections.unmodifiableList(new LinkedList<>(figures));
    }

    private Randomizer getRandomizer() {
        return randomizerFetcher.get();
    }

    public void openFigures(Type... figureTypesToOpen) {
        lock.writeLock().lock();
        try {
            openFigures = figureTypesToOpen;
            if (figures.isEmpty()) {
                fillFutureFigures();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void fillFutureFigures() {
        for (int i = 0; i < futureCount; i++) {
            figures.add(generateNextFigure());
        }
    }
}
