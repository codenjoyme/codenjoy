package com.codenjoy.dojo.tetris.services.scores;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
import com.codenjoy.dojo.tetris.services.Events;
import com.codenjoy.dojo.tetris.services.GameSettings;

public class MaxScores extends CumulativeScores implements PlayerScores {

    private volatile int max;

    public MaxScores(int score, GameSettings settings) {
        super(0, settings);
        max = score;
    }

    @Override
    public int clear() {
        super.clear();

        int result = max;
        max = 0;
        return result;
    }

    @Override
    public Integer getScore() {
        return max;
    }

    public int getCurrent() {
        return score;
    }

    @Override
    public void event(Object object) {
        // считаем очки по классике
        super.event(object);

        Events event = (Events)object;

        // посчитали текущий максимум в любом случае
        max = Math.max(max, score);

        // но когда у нас переполнение
        if (event.isGlassOverflown()) {
            // обнулились
            score = 0;
        }
    }

    @Override
    protected void glassOverflown(int level) {
        // ничего не надо делать, в этом режиме не отнимаются очки за переполнение;
    }

    @Override
    public void update(Object score) {
        score = 0;
        max = Integer.valueOf(score.toString());
    }

}
