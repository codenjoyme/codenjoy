package com.codenjoy.dojo.hex.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.hex.model.levels.Level;
import com.codenjoy.dojo.hex.model.levels.LevelImpl;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.hex.services.GameSettings.Keys.*;

public final class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements Key {

        WIN_SCORE("Win score"),
        LOSE_PENALTY("Lose penalty"),
        LEVEL_MAP("Level map");

        private String key;

        Keys(String key) {
            this.key = key;
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
        integer(WIN_SCORE, 30);
        integer(LOSE_PENALTY, 100);

        multiline(LEVEL_MAP,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼             ☼\n" +
                "☼             ☼\n" +
                "☼             ☼\n" +
                "☼             ☼\n" +
                "☼             ☼\n" +
                "☼             ☼\n" +
                "☼             ☼\n" +
                "☼             ☼\n" +
                "☼             ☼\n" +
                "☼             ☼\n" +
                "☼             ☼\n" +
                "☼             ☼\n" +
                "☼             ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
}

    public Level level() {
        return new LevelImpl(string(LEVEL_MAP));
    }

}
