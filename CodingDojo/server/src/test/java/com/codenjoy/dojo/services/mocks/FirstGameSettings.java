package com.codenjoy.dojo.services.mocks;

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

import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.mocks.FirstGameSettings.Keys.PARAMETER1;
import static com.codenjoy.dojo.services.mocks.FirstGameSettings.Keys.PARAMETER2;

public class FirstGameSettings extends SettingsImpl
        implements SettingsReader<FirstGameSettings> {

    public enum Keys implements Key {

        PARAMETER1("Parameter 1"),
        PARAMETER2("Parameter 2");

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

    public FirstGameSettings() {
        init();
    }

    public void init() {
        integer(PARAMETER1, 12);
        integerValue(PARAMETER1).update(15);
        bool(PARAMETER2, true);
    }

    @Override
    public String toString() {
        return "First" + super.toStringShort();
    }
}
