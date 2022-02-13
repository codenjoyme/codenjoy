package com.codenjoy.dojo.tetris.services;

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
import com.codenjoy.dojo.tetris.model.levels.LevelsFactory;
import com.codenjoy.dojo.tetris.model.levels.level.ProbabilityWithoutOverflownLevels;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.event.Mode.CUMULATIVELY;
import static com.codenjoy.dojo.tetris.services.GameRunner.GAME_NAME;
import static com.codenjoy.dojo.tetris.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements PropertiesKey {

        GAME_LEVELS,
        GLASS_SIZE,
        SCORE_MODE,
        FIGURE_DROPPED_SCORE,
        ONE_LINE_REMOVED_SCORE,
        TWO_LINES_REMOVED_SCORE,
        THREE_LINES_REMOVED_SCORE,
        FOUR_LINES_REMOVED_SCORE,
        GLASS_OVERFLOWN_PENALTY,
        SCORE_COUNTING_TYPE;

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
        initScore(CUMULATIVELY);

        options(GAME_LEVELS, levels(),
                ProbabilityWithoutOverflownLevels.class.getSimpleName());
        integer(GLASS_SIZE, 18);

        bool(SCORE_MODE, true);
        integer(FIGURE_DROPPED_SCORE, 1);
        integer(ONE_LINE_REMOVED_SCORE, 10);
        integer(TWO_LINES_REMOVED_SCORE, 30);
        integer(THREE_LINES_REMOVED_SCORE, 50);
        integer(FOUR_LINES_REMOVED_SCORE, 100);
        integer(GLASS_OVERFLOWN_PENALTY, -10);
    }

    private List<String> levels() {
        LevelsFactory factory = new LevelsFactory();
        return factory.allLevels();
    }

    public Calculator<Event> calculator() {
        return new Calculator<>(new Scores(this));
    }
}