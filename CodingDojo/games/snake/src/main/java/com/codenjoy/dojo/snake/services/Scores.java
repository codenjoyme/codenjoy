package com.codenjoy.dojo.snake.services;

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

import com.codenjoy.dojo.services.event.ScoresImpl;
import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.stream.IntStream;

import static com.codenjoy.dojo.services.event.ScoresImpl.Mode.CUMULATIVELY;
import static com.codenjoy.dojo.snake.services.GameSettings.Keys.EAT_STONE_PENALTY;
import static com.codenjoy.dojo.snake.services.GameSettings.Keys.GAME_OVER_PENALTY;

public class Scores extends ScoresMap<Integer> {

    public Scores(SettingsReader settings) {
        super(settings);

        put(Event.Type.KILL,
                value -> settings.integer(GAME_OVER_PENALTY));

        put(Event.Type.EAT_APPLE,
                value -> ScoresImpl.modeValue(settings) == CUMULATIVELY
                        ? value
                        : IntStream.rangeClosed(3, value).sum());

        put(Event.Type.EAT_STONE,
                value -> settings.integer(EAT_STONE_PENALTY));
    }
}