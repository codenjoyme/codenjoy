package com.codenjoy.dojo.tetris.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PlayerFigures implements FigureQueue {
    public static final int DEFAULT_FUTURE_COUNT = 4;

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Figure.Type[] openFigures = null;
    private RandomizerFetcher randomizerFetcher;
    private List<Figure.Type> figures = new LinkedList<>();
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
            return figures.remove(0).createNewFigure();
        } finally {
            lock.readLock().unlock();
        }
    }

    private Figure.Type generateNextFigure() {
        return openFigures[getRandomizer().getNextNumber(openFigures.length)];
    }

    @Override
    public List<Figure.Type> getFutureFigures() {
        return Collections.unmodifiableList(new LinkedList<>(figures));
    }

    private Randomizer getRandomizer() {
        return randomizerFetcher.get();
    }

    public void openFigures(Figure.Type... figureTypesToOpen) {
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
