package com.codenjoy.dojo.icancode.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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

import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.*;

public class Scores extends ScoresMap<Event> {

    public Scores(SettingsReader settings) {
        super(settings);

        put(Event.Type.WIN,
                event -> (event.isMultiple() ? 0 : settings.integer(WIN_SCORE))
                        + event.getGoldCount() * settings.integer(GOLD_SCORE));

        put(Event.Type.LOSE,
                event -> settings.integer(LOSE_PENALTY));

        put(Event.Type.KILL_ZOMBIE,
                event -> event.isMultiple() && settings.bool(ENABLE_KILL_SCORE)
                        ? event.getKillCount() * settings.integer(KILL_ZOMBIE_SCORE)
                        : 0);

        put(Event.Type.KILL_HERO,
                event -> event.isMultiple() && settings.bool(ENABLE_KILL_SCORE)
                        ? event.getKillCount() * settings.integer(KILL_HERO_SCORE)
                        : 0);
    }
}