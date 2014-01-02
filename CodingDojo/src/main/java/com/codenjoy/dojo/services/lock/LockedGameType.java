package com.codenjoy.dojo.services.lock;

import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;
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
    public Game newGame(EventListener listener) {
        return game.wrap(gameType.newGame(listener));
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return gameType.getBoardSize();
    }

    @Override
    public String gameName() {
        return gameType.gameName();
    }

    @Override
    public Enum[] getPlots() {
        return gameType.getPlots();
    }

    @Override
    public Settings getGameSettings() {
        return gameType.getGameSettings();
    }

    @Override
    public boolean isSingleBoardGame() {
        return gameType.isSingleBoardGame();
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
