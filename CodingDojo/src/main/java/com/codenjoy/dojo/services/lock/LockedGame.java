package com.codenjoy.dojo.services.lock;

import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Point;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * User: sanja
 * Date: 19.12.13
 * Time: 2:14
 */
public class LockedGame implements Game {
    private final LockedJoystick joystick;
    private ReadWriteLock lock;

    private Game game;

    public LockedGame(ReadWriteLock lock) {
        this.lock = lock;
        this.joystick = new LockedJoystick(lock);
    }

    public Game wrap(Game game) {
        this.game = game;
        return this;
    }

    @Override
    public Joystick getJoystick() {
        return joystick.wrap(game.getJoystick());
    }

    @Override
    public int getMaxScore() {
        lock.readLock().lock();
        try {
            return game.getMaxScore();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int getCurrentScore() {
        lock.readLock().lock();
        try {
            return game.getCurrentScore();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isGameOver() {
        lock.readLock().lock();
        try {
            return game.isGameOver();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void newGame() {
        lock.writeLock().lock();
        try {
            game.newGame();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String getBoardAsString() {
        lock.readLock().lock();
        try {
            return game.getBoardAsString();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void destroy() {
        lock.writeLock().lock();
        try {
            game.destroy();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clearScore() {
        lock.writeLock().lock();
        try {
            game.clearScore();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Point getHero() {
        lock.readLock().lock();
        try {
            return game.getHero();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void tick() {
        lock.writeLock().lock();
        try {
            game.tick();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
