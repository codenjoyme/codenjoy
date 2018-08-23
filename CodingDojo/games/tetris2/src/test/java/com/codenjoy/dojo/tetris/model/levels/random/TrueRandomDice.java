package com.codenjoy.dojo.tetris.model.levels.random;

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

import com.codenjoy.dojo.services.Dice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by indigo on 2018-08-22.
 */
public class TrueRandomDice implements Dice {

    Map<Integer, N> map = new HashMap<>();

    class N {
        int[] values;
        int index;

        public N(int[] values, int index) {
            this.values = values;
            this.index = index;
        }
    }

    @Override
    public int next(int n) {
        if (!map.containsKey(n)) {
            generate(n);
        }

        int[] values = map.get(n).values;
        int index = map.get(n).index;

        int value = values[index];

        ++index;
        index %= values.length;
        map.get(n).index = index;

        return value;
    }

    private void generate(int n) {
        int[] values = new int[n];
        for (int i = 0; i < values.length; i++) {
            values[i] = i;
        }
        int index = 0;

        map.put(n, new N(values, index));
    }
}
