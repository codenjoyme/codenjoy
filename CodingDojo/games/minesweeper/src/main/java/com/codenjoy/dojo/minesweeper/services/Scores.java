package com.codenjoy.dojo.minesweeper.services;

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


import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.minesweeper.services.GameSettings.Keys.*;

public class Scores extends ScoresMap<Void> {

    private volatile int destroyed;

    public Scores(SettingsReader settings) {
        super(settings);

        put(Event.DESTROY_MINE,
                event -> onDestroyMine());

        put(Event.FORGET_CHARGE,
                event -> onForgotCharge());

        put(Event.KILL_ON_MINE,
                event -> onKillOnMine());

        put(Event.NO_MORE_CHARGE,
                event -> onNoMoreCharge());

        put(Event.WIN,
                event -> onWin());

        put(Event.CLEAN_BOARD,
                event -> onClearBoard());
    }

    private int onClearBoard() {
        return settings.integer(CLEAR_BOARD_SCORE);
    }

    private int onWin() {
        return settings.integer(WIN_SCORE);
    }

    private int onNoMoreCharge() {
        return onKillOnMine();
    }

    private int onDestroyMine() {
        destroyed++;
        return destroyed;
    }

    private int onForgotCharge() {
        destroyed -= settings.integer(DESTROYED_PENALTY);
        destroyed = Math.max(0, destroyed);
        return settings.integer(DESTROYED_FORGOT_PENALTY);
    }

    private int onKillOnMine() {
        destroyed = 0;
        return settings.integer(GAME_OVER_PENALTY);
    }
}