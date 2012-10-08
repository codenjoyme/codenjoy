package net.tetris.services;

import net.tetris.dom.Figure;
import net.tetris.dom.FigureQueue;
import net.tetris.services.randomizer.Randomizer;
import net.tetris.services.randomizer.RandomizerFetcher;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PlayerFigures implements FigureQueue {
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Figure.Type[] openFigures = null;
    private RandomizerFetcher randomizerFetcher;

    public void setRandomizerFetcher(RandomizerFetcher randomizerFetcher) {
        this.randomizerFetcher = randomizerFetcher;
    }

    @Override
    public Figure next() {
        lock.readLock().lock();
        try {
            return openFigures[getRandomizer().getNextNumber(openFigures.length)].createNewFigure();
        } finally {
            lock.readLock().unlock();
        }
    }

    private Randomizer getRandomizer() {
        return randomizerFetcher.get();
    }

    public void openFigures(Figure.Type... figureTypesToOpen) {
        lock.writeLock().lock();
        try {
            openFigures = figureTypesToOpen;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
