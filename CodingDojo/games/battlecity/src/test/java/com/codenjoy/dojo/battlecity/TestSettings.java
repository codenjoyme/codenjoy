package com.codenjoy.dojo.battlecity;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.services.settings.EditBox;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SettingsImpl;

public class TestSettings extends SettingsImpl {

    public TestSettings() {
        editBox("Kill your tank penalty").type(Integer.class).def(0);
        editBox("Kill other hero tank score").type(Integer.class).def(10);
        editBox("Kill other AI tank score").type(Integer.class).def(25);
        editBox("Custom map path").type(String.class).def("");
    }

    private Parameter<?> editBox(String name) {
        return super.addEditBox(name);
    }

    @Override
    public Parameter<?> addEditBox(String name) {
        return new EditBox<>(name);
    }
}

