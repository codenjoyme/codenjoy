package net.tetris.services;

import net.tetris.dom.Figure;
import net.tetris.dom.FigureQueue;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PlayerFigures implements FigureQueue {
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Figure.Type[] openFigures = null;
    private Random random = new Random();

    @Override
    public Figure next() {
        lock.readLock().lock();
        try {
            return openFigures[random.nextInt(openFigures.length)].createNewFigure();
        } finally {
            lock.readLock().unlock();
        }
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
