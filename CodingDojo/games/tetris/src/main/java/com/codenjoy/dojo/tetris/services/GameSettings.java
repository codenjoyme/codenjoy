package com.codenjoy.dojo.tetris.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.services.settings.SelectBox;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.codenjoy.dojo.tetris.model.levels.LevelsFactory;
import com.codenjoy.dojo.tetris.model.levels.level.ProbabilityWithoutOverflownLevels;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.tetris.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements Key {

        GAME_LEVELS("[Game] Game Levels"),
        GLASS_SIZE("[Game] Glass Size"),

        SCORE_MODE("[Score] Score mode (cumulative or maximum between overflows)"),
        FIGURE_DROPPED_SCORE("[Score] Figure dropped score score"),
        ONE_LINE_REMOVED_SCORE("[Score] One line removed score"),
        TWO_LINES_REMOVED_SCORE("[Score] Two lines removed score"),
        THREE_LINES_REMOVED_SCORE("[Score] Three lines removed score"),
        FOUR_LINES_REMOVED_SCORE("[Score] Four lines removed score"),
        GLASS_OVERFLOWN_PENALTY("[Score] Glass overflown penalty");

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
        options(GAME_LEVELS, levels(),
                ProbabilityWithoutOverflownLevels.class.getSimpleName());
        integer(GLASS_SIZE, 18);

        bool(SCORE_MODE, true);
        integer(FIGURE_DROPPED_SCORE, 1);
        integer(ONE_LINE_REMOVED_SCORE, 10);
        integer(TWO_LINES_REMOVED_SCORE, 30);
        integer(THREE_LINES_REMOVED_SCORE, 50);
        integer(FOUR_LINES_REMOVED_SCORE, 100);
        integer(GLASS_OVERFLOWN_PENALTY, 10);
    }

    private List<String> levels() {
        LevelsFactory factory = new LevelsFactory();
        return factory.allLevels();
    }

}
