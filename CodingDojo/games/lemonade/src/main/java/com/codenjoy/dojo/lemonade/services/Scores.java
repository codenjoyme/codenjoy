package com.codenjoy.dojo.lemonade.services;

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


import com.codenjoy.dojo.services.event.Answer;
import com.codenjoy.dojo.services.event.Manual;
import com.codenjoy.dojo.services.event.ScoresMap;

import static com.codenjoy.dojo.lemonade.services.GameSettings.Keys.BANKRUPT_PENALTY;

public class Scores extends ScoresMap {

    public Scores(GameSettings settings) {
        super(settings);

        put(new Manual(Event.Type.WIN),
                answer -> win(settings, (Answer)answer));

        put(Event.Type.LOSE,
                event -> (settings.scoreMode() == ScoreMode.SUM_OF_PROFITS)
                        ? settings.integer(BANKRUPT_PENALTY)
                        : 0);
    }

    private int toScore(double value) {
        return (int) (100 * value);
    }

    private int win(GameSettings settings, Answer answer) {
        if (settings.scoreMode() == ScoreMode.SUM_OF_PROFITS) {
            return toScore(answer.value(Event.class).profit());
        }

        if (settings.scoreMode() == ScoreMode.LAST_DAY_ASSETS) {
            int amount = toScore(answer.value(Event.class).assetsAfter());
            answer.score(Math.max(answer.score(), amount));
            return 0;
        }

        return 0;
    }
}