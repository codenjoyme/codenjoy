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

import com.codenjoy.dojo.loderunner.services.Scores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SettingsImpl;

public class TestSettings extends SettingsImpl {

    public TestSettings() {
        editBox(Scores.HERO_KILL_PENALTY).type(Integer.class).def(0);
        editBox(Scores.ENEMY_KILL_SCORE).type(Integer.class).def(10);
        editBox(Scores.GOLD_SERIES_INCREMENT_SCORE).type(Integer.class).def(1);
        editBox(Scores.SUICIDE_PENALTY).type(Integer.class).def(0);
        editBox(Scores.SHADOW_TICKS).type(Integer.class).def(15);
        editBox(Scores.SHADOW_PILLS).type(Integer.class).def(0);
        editBox(Scores.PORTAL_TICKS).type(Integer.class).def(10);
        editBox(Scores.PORTALS).type(Integer.class).def(0);
        editBox(Scores.MAP_PATH).type(String.class).def("");

        editBox(Scores.GOLD_COUNT_YELLOW).type(Integer.class).def(-1);
        editBox(Scores.GOLD_COUNT_GREEN).type(Integer.class).def(0);
        editBox(Scores.GOLD_COUNT_RED).type(Integer.class).def(0);
        editBox(Scores.GOLD_SCORE_YELLOW).type(Integer.class).def(1);
        editBox(Scores.GOLD_SCORE_GREEN).type(Integer.class).def(5);
        editBox(Scores.GOLD_SCORE_RED).type(Integer.class).def(10);

        editBox(Scores.ENEMIES_COUNT).type(Integer.class).def(0);
    }

    private Parameter<?> editBox(String name) {
        return super.addEditBox(name);
    }
}
