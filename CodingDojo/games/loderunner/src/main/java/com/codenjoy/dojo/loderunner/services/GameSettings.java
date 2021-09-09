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


import com.codenjoy.dojo.loderunner.model.levels.Level;
import com.codenjoy.dojo.loderunner.services.levels.Big;
import com.codenjoy.dojo.services.round.RoundSettings;
import com.codenjoy.dojo.services.semifinal.SemifinalSettings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl
        implements SettingsReader<GameSettings>,
                    RoundSettings<GameSettings>,
                    SemifinalSettings<GameSettings> {

    public static final String MAP_PATH_NONE = "none";

    public enum Keys implements Key {

        SHADOW_PILLS_COUNT("[Game] Shadow pills count"),
        SHADOW_TICKS("[Game] Shadow ticks"),

        PORTALS_COUNT("[Game] Portals count"),
        PORTAL_TICKS("[Game] Portal ticks"),

        ENEMIES_COUNT("[Game] Enemies count"),

        GOLD_COUNT_GREEN("[Game] Green gold count"),
        GOLD_SCORE_GREEN("[Score] Green gold score"),
        GOLD_SCORE_GREEN_INCREMENT("[Score] Green gold score increment"),

        GOLD_COUNT_YELLOW("[Game] Yellow gold count"),
        GOLD_SCORE_YELLOW("[Score] Yellow gold score"),
        GOLD_SCORE_YELLOW_INCREMENT("[Score] Yellow gold score increment"),

        GOLD_COUNT_RED("[Game] Red gold count"),
        GOLD_SCORE_RED("[Score] Red gold score"),
        GOLD_SCORE_RED_INCREMENT("[Score] Red gold score increment"),

        KILL_HERO_PENALTY("[Score] Kill hero penalty"),
        KILL_ENEMY_SCORE("[Score] Kill enemy score"),
        SUICIDE_PENALTY("[Score] Suicide penalty"),

        LEVEL_MAP("[Level] Map"),
        MAP_PATH("[Level] Custom map path (or 'none')");

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
        initRound();
        initSemifinal();

        integer(SHADOW_PILLS_COUNT, 0);
        integer(SHADOW_TICKS, 15);

        integer(PORTALS_COUNT, 0);
        integer(PORTAL_TICKS, 10);

        integer(ENEMIES_COUNT, 5);

        integer(GOLD_COUNT_GREEN, 40);
        integer(GOLD_SCORE_GREEN, 1);
        integer(GOLD_SCORE_GREEN_INCREMENT, 1);

        integer(GOLD_COUNT_YELLOW, 20);
        integer(GOLD_SCORE_YELLOW, 2);
        integer(GOLD_SCORE_YELLOW_INCREMENT, 1);

        integer(GOLD_COUNT_RED, 10);
        integer(GOLD_SCORE_RED, 5);
        integer(GOLD_SCORE_RED_INCREMENT, 1);

        integer(KILL_HERO_PENALTY, 1);
        integer(KILL_ENEMY_SCORE, 10);
        integer(SUICIDE_PENALTY, 10);

        multiline(LEVEL_MAP, Big.all().get(0));
        string(MAP_PATH, MAP_PATH_NONE);
    }

    public Level level() {
        return new Level(map());
    }

    public String map() {
        String path = string(MAP_PATH);
        if (MAP_PATH_NONE.equals(path)) {
            return string(LEVEL_MAP);
        } else {
            return MapLoader.loadMapFromFile(path);
        }
    }
}
