package com.epam.dojo.expansion.services;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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


import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class Scores implements PlayerScores {

    private volatile int score;

    public Scores(int startScore) {
        this.score = startScore;
    }

    @Override
    public int clear() {
        score = 0;
        return 0;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void event(Object input) {
        Events events = (Events)input;

        if (events.getType() == Events.Type.WIN) {
            score += events.getScore();
        } else if (events.getType() == Events.Type.LOOSE) {
            score -= events.getScore();
        }
        score = Math.max(0, score);
    }
}
