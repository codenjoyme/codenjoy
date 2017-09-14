package com.codenjoy.dojo.services.lock;

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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockedGameType implements GameType {

    private LockedGame game;
    private GameType gameType;

    public LockedGameType (GameType gameType) {
        this.gameType = gameType;
        ReadWriteLock lock = new ReentrantReadWriteLock(true);
        game = new LockedGame(lock);
    }
    
    @Override
    public PlayerScores getPlayerScores(Object score) {
        return gameType.getPlayerScores(score);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory, String save) {
        return game.wrap(gameType.newGame(listener, factory, save));
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
    public boolean newAI(String aiName) {
        return gameType.newAI(aiName);
    }

    @Override
    public String getVersion() {
        return gameType.getVersion();
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

    @Override
    public String toString() {
        return gameType.getClass().toString();
    }

    @Override
    public void tick() {
        gameType.tick();
    }
}
