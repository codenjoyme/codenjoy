package com.codenjoy.dojo.snakebattle.model.board.round;

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

import com.codenjoy.dojo.services.settings.Parameter;

public class Timer {

    private final Parameter<Integer> from;
    private int time;
    private int currentFrom;

    public Timer(Parameter<Integer> from) {
        this.from = from;
    }

    public Timer start() {
        time = from.getValue();
        currentFrom = from.getValue();
        return this;
    }

    public boolean justFinished() {
        return time == 0;
    }

    public boolean done() {
        return time < 0;
    }

    public int countdown() {
        return time;
    }

    public int time() {
        return currentFrom - time;
    }

    public void tick(Runnable onProgress) {
        if (!done()) {
            time--;
        }

        if (onProgress != null && time > 0) {
            onProgress.run();
        }
    }

    public boolean unlimited() {
        return from.getValue() == 0;
    }

    public Timer stop() {
        time = -1;
        return this;
    }
}
