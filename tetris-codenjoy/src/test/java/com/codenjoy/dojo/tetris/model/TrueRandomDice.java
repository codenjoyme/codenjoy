package com.codenjoy.dojo.tetris.model;

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
