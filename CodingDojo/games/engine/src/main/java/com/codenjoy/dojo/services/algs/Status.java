package com.codenjoy.dojo.services.algs;

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
