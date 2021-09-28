package com.codenjoy.dojo.services.mocks;

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

import com.codenjoy.dojo.services.round.RoundSettings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.mocks.ThirdGameSettings.Keys.PARAMETER5;
import static com.codenjoy.dojo.services.mocks.ThirdGameSettings.Keys.PARAMETER6;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.*;

public class ThirdGameSettings extends SettingsImpl
        implements SettingsReader<ThirdGameSettings>,
                   RoundSettings<ThirdGameSettings> {

    public enum Keys implements Key {

        PARAMETER5("Parameter 5"), // false - will be MULTIPLE, true - SINGLE_LEVELS
        PARAMETER6("Parameter 6"); // count levels

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

    public ThirdGameSettings() {
        init();
        initRound();

        playersAndTeamsPerRoom(4, 2);
    }

    public SettingsReader playersAndTeamsPerRoom(int players, int teams) {
        return bool(ROUNDS_ENABLED, true)
                .integer(ROUNDS_PLAYERS_PER_ROOM, players)
                .integer(ROUNDS_TEAMS_PER_ROOM, teams);
    }

    public void init() {
        integer(PARAMETER5, 67);
        bool(PARAMETER6, true);
        boolValue(PARAMETER6).update(false);
    }

    @Override
    public String toString() {
        return "Third" + super.toStringShort();
    }
}
