package com.codenjoy.dojo.services.lock;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * User: sanja
 * Date: 19.12.13
 * Time: 2:23
 */
public class LockedGameType implements GameType {

    private LockedGame game;
    private GameType gameType;

    public LockedGameType (GameType gameType) {
        this.gameType = gameType;
        ReadWriteLock lock = new ReentrantReadWriteLock(true);
        game = new LockedGame(lock);
    }
    
    @Override
    public PlayerScores getPlayerScores(int score) {
        return gameType.getPlayerScores(score);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory) {
        return game.wrap(gameType.newGame(listener, factory));
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return gameType.getBoardSize();
    }

    @Override
    public String name() {
        return gameType.name();
    }

    @Override
    public Enum[] getPlots() {
        return gameType.getPlots();
    }

    @Override
    public Settings getSettings() {
        return gameType.getSettings();
    }

    @Override
    public boolean isSingleBoard() {
        return gameType.isSingleBoard();
    }

    @Override
    public void newAI(String aiName) {
        gameType.newAI(aiName);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof LockedGameType) {
            LockedGameType t = (LockedGameType)o;
            GameType gt = t.gameType;
            o = gt;
        }

        return gameType.equals(o);
    }

    @Override
    public int hashCode() {
        return gameType.hashCode();
    }
}
