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
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.List;

import static com.codenjoy.dojo.kata.services.GameSettings.Keys.*;

public final class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {


    public enum Keys implements Key {

        WIN_SCORE("Win score"),
        LOOSE_PENALTY("Loose penalty"),
        A_CONSTANT("A constant"),
        B_CONSTANT("B constant"),
        C_CONSTANT("C constant"),
        D_CONSTANT("D constant");

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
        addEditBox(WIN_SCORE.key()).type(Integer.class).def(30);
        addEditBox(LOOSE_PENALTY.key()).type(Integer.class).def(100);

        addEditBox(A_CONSTANT.key()).type(Integer.class).def(100);
        addEditBox(B_CONSTANT.key()).type(Integer.class).def(3);
        addEditBox(C_CONSTANT.key()).type(Integer.class).def(30);
        addEditBox(D_CONSTANT.key()).type(Integer.class).def(10);
    }

    public List<Level> levels() {
        return LevelsLoader.getAlgorithms();
    }

}
