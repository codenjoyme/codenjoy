package com.codenjoy.dojo.lemonade.services;

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


import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.lemonade.services.GameSettings.Keys.*;

public final class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements Key {

        WIN_SCORE("Win score"),
        LOOSE_PENALTY("Loose penalty"),
        BANKRUPT_PENALTY("Bankrupt penalty"),
        LIMIT_DAYS("Limit days");

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

        addEditBox(BANKRUPT_PENALTY.key()).type(Integer.class).def(100);
        addEditBox(LIMIT_DAYS.key()).type(Integer.class).def(30);
    }

    public ScoreMode scoreMode(){
        return integer(LIMIT_DAYS) > 0
                ? ScoreMode.LAST_DAY_ASSETS
                : ScoreMode.SUM_OF_PROFITS;
    }


}
