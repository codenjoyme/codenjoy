package com.codenjoy.dojo.loderunner.services;

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


import com.codenjoy.dojo.loderunner.model.Level;
import com.codenjoy.dojo.loderunner.model.LevelImpl;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.*;

public final class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements Key {

        KILL_HERO_PENALTY("Kill hero penalty"),
        KILL_ENEMY_SCORE("Kill enemy score"),
        GET_GOLD_SCORE("Get gold score"),
        GET_NEXT_GOLD_INCREMENT("Get next gold increment score"),
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

    public GameSettings() {
        integer(KILL_HERO_PENALTY, 0);
        integer(KILL_ENEMY_SCORE, 10);
        integer(GET_GOLD_SCORE, 1);
        integer(GET_NEXT_GOLD_INCREMENT, 1);
        multiline(LEVEL_MAP, Level1.get());
    }

    public Level level() {
        return new LevelImpl(string(LEVEL_MAP));
    }

}
