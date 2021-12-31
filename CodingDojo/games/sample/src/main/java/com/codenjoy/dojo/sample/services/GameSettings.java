package com.codenjoy.dojo.sample.services;

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


import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.event.ScoresImpl;
import com.codenjoy.dojo.services.settings.AllSettings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.sample.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements AllSettings<GameSettings> {

    public enum Keys implements Key {

        GET_GOLD_SCORE("[Score] Pick gold score"),
        WIN_ROUND_SCORE("[Score] Win round score"),
        HERO_DIED_PENALTY("[Score] Hero died penalty"),
        KILL_OTHER_HERO_SCORE("[Score] Kill other hero score"),
        KILL_ENEMY_HERO_SCORE("[Score] Kill enemy hero score"),
        SCORE_COUNTING_TYPE(ScoresImpl.SCORE_COUNTING_TYPE.key());

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
        initAll();

        integer(GET_GOLD_SCORE, 30);
        integer(WIN_ROUND_SCORE, 100);
        integer(HERO_DIED_PENALTY, -20);
        integer(KILL_OTHER_HERO_SCORE, 5);
        integer(KILL_ENEMY_HERO_SCORE, 10);

        Levels.setup(this);
    }

    public Calculator<Void> calculator() {
        return new Calculator<>(new Scores(this));
    }
}