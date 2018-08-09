package com.codenjoy.dojo.services;

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


import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import com.codenjoy.dojo.services.printer.BoardReader;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedList;

public class NullGame implements Game {

    public static final Game INSTANCE = new NullGame();

    private NullGame() {
        // do nothing
    }

    @Override
    public Joystick getJoystick() {
        return NullJoystick.INSTANCE;
    }

    @Override
    public int getMaxScore() {
        return 0; 
    }

    @Override
    public int getCurrentScore() {
        return 0; 
    }

    @Override
    public boolean isGameOver() {
        return false; 
    }

    @Override
    public void newGame() {
        // do nothing
    }

    @Override
    public String getBoardAsString() {
        return StringUtils.EMPTY;
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public void clearScore() {
        // do nothing
    }

    @Override
    public HeroData getHero() {
        return HeroData.NULL;
    }

    @Override
    public String getSave() {
        return StringUtils.EMPTY;
    }

    @Override
    public GamePlayer getPlayer() { // TODO to use NullGamePlayer
        return new GamePlayer(event -> {}) {
            @Override
            public PlayerHero getHero() { // TODO to use PlayerHero
                return new PlayerHero() {
                    @Override
                    public void down() {
                        // do nothing
                    }

                    @Override
                    public void up() {
                        // do nothing
                    }

                    @Override
                    public void left() {
                        // do nothing
                    }

                    @Override
                    public void right() {
                        // do nothing
                    }

                    @Override
                    public void act(int... p) {
                        // do nothing
                    }

                    @Override
                    public void tick() {
                        // do nothing
                    }
                };
            }

            @Override
            public void newHero(GameField field) {
                // do nothing
            }

            @Override
            public boolean isAlive() {
                return false;
            }
        };
    }

    @Override
    public GameField getField() { // TODO to use GameField
        return new GameField() {
            @Override
            public BoardReader reader() { // TODO to use BoardReader
                return new BoardReader() {
                    @Override
                    public int size() {
                        return 0;
                    }

                    @Override
                    public Iterable<? extends Point> elements() {
                        return new LinkedList<>();
                    }
                };
            }

            @Override
            public void newGame(GamePlayer player) {
                // do nothing
            }

            @Override
            public void remove(GamePlayer player) {
                // do nothing
            }

            @Override
            public void tick() {
                // do nothing
            }
        };
    }

    @Override
    public void tick() {
        // do nothing
    }
}
