package com.codenjoy.dojo.services.lock;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import org.json.JSONObject;

import java.util.concurrent.locks.ReadWriteLock;

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
    public boolean isGameOver() {
        lock.writeLock().lock();
        try {
            return game.isGameOver();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean isWin() {
        lock.writeLock().lock();
        try {
            return game.isWin();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean shouldLeave() {
        lock.writeLock().lock();
        try {
            return game.shouldLeave();
        } finally {
            lock.writeLock().unlock();
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
    public void loadSave(JSONObject save) {
        lock.writeLock().lock();
        try {
            game.loadSave(save);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Object getBoardAsString() {
        lock.writeLock().lock();
        try {
            return game.getBoardAsString();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void close() {
        lock.writeLock().lock();
        try {
            game.close();
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
    public HeroData getHero() {
        lock.writeLock().lock();
        try {
            return game.getHero();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public JSONObject getSave() {
        lock.writeLock().lock();
        try {
            return game.getSave();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public GamePlayer getPlayer() {
        lock.writeLock().lock();
        try {
            return game.getPlayer();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public GameField getField() {
        lock.writeLock().lock();
        try {
            return game.getField();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void on(GameField field) {
        lock.writeLock().lock();
        try {
            game.on(field);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void setProgress(LevelProgress progress) {
        lock.writeLock().lock();
        try {
            game.setProgress(progress);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public LevelProgress getProgress() {
        lock.writeLock().lock();
        try {
            return game.getProgress();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String toString() {
        lock.writeLock().lock();
        try {
            return game.toString();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Game getWrapped() {
        return game;
    }

    public static boolean equals(Game game1, Game game2) {
        if (game1 instanceof LockedGame) {
            game1 = ((LockedGame)game1).getWrapped();
        }
        if (game2 instanceof LockedGame) {
            game2 = ((LockedGame)game2).getWrapped();
        }
        return game1 == game2;
    }
}
