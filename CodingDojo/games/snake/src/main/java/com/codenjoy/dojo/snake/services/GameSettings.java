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

import com.codenjoy.dojo.services.round.RoundSettings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.snake.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings>, RoundSettings {

    public enum Keys implements Key {

        BOARD_SIZE("Board size"),
        MAX_SCORE_MODE("Max score mode"),
        GAME_OVER_PENALTY("Game over penalty"),
        START_SNAKE_LENGTH("Start snake length"),
        EAT_STONE_PENALTY("Eat stone penalty"),
        EAT_STONE_DECREASE("Eat stone decrease");

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
        addEditBox(BOARD_SIZE.key()).type(Integer.class).def(15);
        addCheckBox(MAX_SCORE_MODE.key()).type(Boolean.class).def(false);
        addEditBox(GAME_OVER_PENALTY.key()).type(Integer.class).def(0);
        addEditBox(START_SNAKE_LENGTH.key()).type(Integer.class).def(2);
        addEditBox(EAT_STONE_PENALTY.key()).type(Integer.class).def(0);
        addEditBox(EAT_STONE_DECREASE.key()).type(Integer.class).def(10);
    }
}
