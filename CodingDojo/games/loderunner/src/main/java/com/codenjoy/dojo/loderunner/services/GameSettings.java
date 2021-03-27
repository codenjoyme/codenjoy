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
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {
    
    public enum Keys implements Key {

        KILL_HERO_PENALTY("Kill hero penalty"),
        KILL_ENEMY_SCORE("Kill enemy score"),
        SUICIDE_PENALTY("Suicide penalty"),

        LEVEL_MAP("Level map"),
        
        SHADOW_TICKS("Shadow ticks"),
        SHADOW_PILLS_COUNT("Shadow pills count"),
        
        PORTAL_TICKS("Portal ticks"),
        PORTALS_COUNT("Portals count"),

        GOLD_COUNT_YELLOW("Yellow gold count"),
        GOLD_COUNT_GREEN("Green gold count"),
        GOLD_COUNT_RED("Red gold count"),

        GOLD_SCORE_YELLOW("Yellow gold score"),
        GOLD_SCORE_GREEN("Green gold score"),
        GOLD_SCORE_RED("Red gold score"),

        GOLD_SCORE_YELLOW_INCREMENT("Yellow gold score increment"),
        GOLD_SCORE_GREEN_INCREMENT("Green gold score increment"),
        GOLD_SCORE_RED_INCREMENT("Red gold score increment"),

        ENEMIES_COUNT("Enemies count"),

        MAP_PATH("Custom map path");

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
        multiline(LEVEL_MAP, Level1.get());

        integer(KILL_HERO_PENALTY, 1);
        integer(KILL_ENEMY_SCORE, 10);

        integer(SHADOW_PILLS_COUNT, 0);
        integer(SHADOW_TICKS, 15);
        integer(SUICIDE_PENALTY, 10);
        integer(PORTALS_COUNT, 0);
        integer(PORTAL_TICKS, 10);

        integer(GOLD_COUNT_GREEN, 0);
        integer(GOLD_COUNT_YELLOW, 20);
        integer(GOLD_COUNT_RED, 0);

        integer(GOLD_SCORE_GREEN, 1);
        integer(GOLD_SCORE_YELLOW, 2);
        integer(GOLD_SCORE_RED, 5);

        integer(GOLD_SCORE_GREEN_INCREMENT, 5);
        integer(GOLD_SCORE_YELLOW_INCREMENT, 2);
        integer(GOLD_SCORE_RED_INCREMENT, 1);

        integer(ENEMIES_COUNT, 5);

        string(MAP_PATH, "");
    }

    public Level level(Dice dice) {
        return new LevelImpl(getMap(), dice);
    }

    public String getMap() {
        String path = string(MAP_PATH);
        if (StringUtils.isNotEmpty(path)) {
            return MapLoader.loadMapFromFile(path);
        } else {
            return string(LEVEL_MAP);
        }
    }
}
