package com.codenjoy.dojo.football.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.football.model.Level;
import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.settings.PropertiesKey;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.football.services.GameRunner.GAME_NAME;
import static com.codenjoy.dojo.football.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements PropertiesKey {

        NUMBER_OF_PLAYERS,
        IS_NEED_AI,
        WIN_SCORE,
        LEVEL_MAP;

        private String key;

        Keys() {
            this.key = key(GAME_NAME);
        }

        @Override
        public String key() {
            return key;
        }
    }

    @Override
    public List<Key> allKeys() {
        return Arrays.asList(Keys.values());
    }

    public GameSettings() {
        integer(NUMBER_OF_PLAYERS, 2);
        integer(IS_NEED_AI, 1);

        integer(WIN_SCORE, 1);

        multiline(LEVEL_MAP,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼┴┴┴┴┴┴┴☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼               ∙              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼                              ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼┬┬┬┬┬┬┬☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    public Level level() {
        return new Level(string(LEVEL_MAP));
    }

    public Calculator<Void> calculator() {
        return new Calculator<>(new Scores(this));
    }
}