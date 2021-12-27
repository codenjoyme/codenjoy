package com.codenjoy.dojo.sudoku.services;

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

import static com.codenjoy.dojo.sudoku.services.GameSettings.Keys.*;

public class Scores extends ScoresMap<Void> {

    public Scores(SettingsReader settings) {
        super(settings);

        put(Event.WIN,
                value -> settings.integer(WIN_SCORE));

        put(Event.FAIL,
                value -> settings.integer(FAIL_PENALTY));

        put(Event.SUCCESS,
                value -> settings.integer(SUCCESS_SCORE));

        put(Event.LOSE,
                value -> settings.integer(LOSE_PENALTY));
    }
}