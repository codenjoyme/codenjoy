package com.codenjoy.dojo.services.algs;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.services.Direction;

import java.util.ArrayList;
import java.util.List;

import static com.codenjoy.dojo.services.Direction.*;

/**
 * Оптимизированная версия List<Direction>.
 * Как часть Points помогает понять куда в этой клеточке
 * мы можем еще попробовать двигаться.
 */
public class Status {

    private boolean[] goes = new boolean[4];

    public void add(Direction direction) {
        goes[direction.value()] = true;
    }

    public boolean done(Direction direction) {
        boolean result = goes[direction.value()];
        goes[direction.value()] = false;
        return result;
    }

    public boolean empty() {
        return goes[LEFT.value()]
                && goes[RIGHT.value()]
                && goes[UP.value()]
                && goes[DOWN.value()];
    }

    public boolean[] goes() {
        return goes;
    }

    // not optimized method, used for testing
    public List<Direction> directions() {
        List<Direction> result = new ArrayList<>(4);
        for (int index = 0; index < goes.length; index++) {
            if (!goes[index]) continue;

            Direction direction = Direction.valueOf(index);
            result.add(direction);
        }
        return result;
    }
}
