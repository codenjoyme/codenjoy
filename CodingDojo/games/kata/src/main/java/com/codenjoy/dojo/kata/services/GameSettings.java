package com.codenjoy.dojo.kata.services;

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


import com.codenjoy.dojo.kata.model.levels.Level;
import com.codenjoy.dojo.kata.model.levels.LevelsLoader;
import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.kata.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements Key {

        WIN_SCORE("[Score] Win score"),
        LOSE_PENALTY("[Score] Lose penalty"),
        A_CONSTANT("[Score] A constant"),
        B_CONSTANT("[Score] B constant"),
        C_CONSTANT("[Score] C constant"),
        D_CONSTANT("[Score] D constant");

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
        integer(LOSE_PENALTY, -100);

        integer(A_CONSTANT, 100);
        integer(B_CONSTANT, 3);
        integer(C_CONSTANT, 30);
        integer(D_CONSTANT, 10);
    }

    public List<Level> levels() {
        return LevelsLoader.getAlgorithms();
    }

    public Calculator<Object> calculator() {
        return new Calculator<>(new Scores(this));
    }
}
