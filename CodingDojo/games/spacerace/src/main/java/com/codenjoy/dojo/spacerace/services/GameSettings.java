package com.codenjoy.dojo.spacerace.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
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

import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.settings.PropertiesKey;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.codenjoy.dojo.spacerace.model.Level;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.spacerace.services.GameRunner.GAME_NAME;
import static com.codenjoy.dojo.spacerace.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements PropertiesKey {

        TICKS_TO_RECHARGE,
        BULLETS_COUNT,
        DESTROY_BOMB_SCORE,
        DESTROY_STONE_SCORE,
        DESTROY_ENEMY_SCORE,
        LOSE_PENALTY,
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
        integer(TICKS_TO_RECHARGE, 30);
        integer(BULLETS_COUNT, 10);

        integer(DESTROY_BOMB_SCORE, 30);
        integer(DESTROY_STONE_SCORE, 10);
        integer(DESTROY_ENEMY_SCORE, 500);
        integer(LOSE_PENALTY, -100);

        multiline(LEVEL_MAP,
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼              ☺             ☼");
    }

    public Level level() {
        return new Level(string(LEVEL_MAP));
    }

    public Calculator<Void> calculator() {
        return new Calculator<>(new Scores(this));
    }
}