package com.codenjoy.dojo.services.round;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SettingsReader;

public interface RoundSettings {

    public enum Keys implements SettingsReader.Key {

        ROUNDS_ENABLED("[Game][Rounds] Enabled"),
        TIME_PER_ROUND("[Rounds] Time per Round"),
        TIME_FOR_WINNER("[Rounds] Time for Winner"),
        TIME_BEFORE_START("[Rounds] Time before start Round"),
        ROUNDS_PER_MATCH("[Rounds] Rounds per Match"),
        MIN_TICKS_FOR_WIN("[Rounds] Min ticks for win");

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    Parameter<?> getParameter(String name);

    default Parameter<Integer> timeBeforeStart() {
        return getParameter(Keys.TIME_BEFORE_START.key()).type(Integer.class);
    }

    default Parameter<Integer> roundsPerMatch() {
        return getParameter(Keys.ROUNDS_PER_MATCH.key()).type(Integer.class);
    }

    default Parameter<Integer> minTicksForWin() {
        return getParameter(Keys.MIN_TICKS_FOR_WIN.key()).type(Integer.class);
    }

    default Parameter<Integer> timePerRound() {
        return getParameter(Keys.TIME_PER_ROUND.key()).type(Integer.class);
    }

    default Parameter<Integer> timeForWinner() {
        return getParameter(Keys.TIME_FOR_WINNER.key()).type(Integer.class);
    }

    default Parameter<Boolean> roundsEnabled() {
        return getParameter(Keys.ROUNDS_ENABLED.key()).type(Boolean.class);
    }
}
