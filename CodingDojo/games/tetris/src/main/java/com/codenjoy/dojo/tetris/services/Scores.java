package com.codenjoy.dojo.tetris.services;

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

import com.codenjoy.dojo.services.event.ScoresImpl;
import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.services.event.Mode.SERIES_MAX_VALUE;
import static com.codenjoy.dojo.tetris.services.GameSettings.Keys.*;

public class Scores extends ScoresMap<Event> {

    public Scores(SettingsReader settings) {
        super(settings);

        put(Event.Type.LINES_REMOVED,
                event -> linesRemoved(event.getLevel(), event.getRemovedLines()));

        put(Event.Type.FIGURES_DROPPED,
                event -> figureDropped(event.getFigureIndex()));

        put(Event.Type.GLASS_OVERFLOWN,
                event -> glassOverflown(event.getLevel()));
    }

    protected int figureDropped(int figureIndex) {
        return settings.integer(FIGURE_DROPPED_SCORE) * figureIndex;
    }

    protected Integer glassOverflown(int level) {
        if (ScoresImpl.modeValue(settings) == SERIES_MAX_VALUE) {
            return null; // что значит, что мы собрались обнулить серию
        } else {
            return settings.integer(GLASS_OVERFLOWN_PENALTY) * level;
        }
    }

    protected int linesRemoved(int level, int count) {
        switch (count) {
            case 1: return settings.integer(ONE_LINE_REMOVED_SCORE) * level;
            case 2: return settings.integer(TWO_LINES_REMOVED_SCORE) * level;
            case 3: return settings.integer(THREE_LINES_REMOVED_SCORE) * level;
            case 4: return settings.integer(FOUR_LINES_REMOVED_SCORE) * level;
            default: return 0;
        }
    }
}