package com.codenjoy.dojo.snake.services;

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

import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.event.ScoresImpl;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.event.Mode.CUMULATIVELY;
import static com.codenjoy.dojo.snake.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements Key {

        BOARD_SIZE("[Level] Board size"),
        GAME_OVER_PENALTY("[Score] Game over penalty"),
        START_SNAKE_LENGTH("[Game] Start snake length"),
        EAT_STONE_PENALTY("[Score] Eat stone penalty"),
        EAT_STONE_DECREASE("[Game] Eat stone decrease"),
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
        initScore(CUMULATIVELY);

        integer(BOARD_SIZE, 15);
        integer(GAME_OVER_PENALTY, -0);
        integer(START_SNAKE_LENGTH, 2);
        integer(EAT_STONE_PENALTY, -0);
        integer(EAT_STONE_DECREASE, 10);
    }

    public Calculator<Integer> calculator() {
        return new Calculator<>(new Scores(this));
    }
}
