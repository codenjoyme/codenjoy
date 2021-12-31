package com.codenjoy.dojo.sample.services;

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
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.sample.services.GameSettings.Keys.*;

/**
 * Класс помогает подсчитывать очки за те или иные действия.
 * Обычно хочется, чтобы константы очков не были захардкоджены,
 * потому используй объект {@link Settings} для их хранения.
 */
public class Scores extends ScoresMap<Void> {

    public Scores(SettingsReader settings) {
        super(settings);

        put(Event.GET_GOLD,
                value -> settings.integer(GET_GOLD_SCORE));

        put(Event.WIN_ROUND,
                value -> settings.integer(WIN_ROUND_SCORE));

        put(Event.HERO_DIED,
                value -> heroDie(HERO_DIED_PENALTY));

        put(Event.KILL_OTHER_HERO,
                value -> heroDie(KILL_OTHER_HERO_SCORE));

        put(Event.KILL_ENEMY_HERO,
                value -> heroDie(KILL_ENEMY_HERO_SCORE));

    }
}