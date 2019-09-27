package com.codenjoy.dojo.loderunner;

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
        editBox("Kill hero penalty").type(Integer.class).def(0);
        editBox("Kill enemy score").type(Integer.class).def(10);
        editBox("Get next gold increment score").type(Integer.class).def(1);
        editBox("SuicidePenalty").type(Integer.class).def(0);
        editBox("Number of ticks that the shadow pill will be active").type(Integer.class).def(15);
        editBox("The shadow pills count").type(Integer.class).def(0);
        editBox("Number of ticks that the portals will be active").type(Integer.class).def(10);
        editBox("The portals count").type(Integer.class).def(0);
        editBox("Custom map path").type(String.class).def("");

        editBox("yellow type gold count").type(Integer.class).def(-1);
        editBox("green type gold count").type(Integer.class).def(0);
        editBox("red type gold count").type(Integer.class).def(0);
        editBox("yellow type gold weight").type(Integer.class).def(1);
        editBox("green type gold weight").type(Integer.class).def(5);
        editBox("red type gold weight").type(Integer.class).def(10);

        addEditBox("Number of enemies").type(Integer.class).def(0);
    }

    private Parameter<?> editBox(String name) {
        return super.addEditBox(name);
    }

    @Override
    public Parameter<?> addEditBox(String name) {
        return new EditBox<>(name);
    }
}
