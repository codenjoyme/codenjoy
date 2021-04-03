package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import static com.codenjoy.dojo.services.Direction.getValues;

public class Action extends PointImpl {

    private boolean mark;

    public Action(Point pt, boolean mark) {
        super(pt);
        this.mark = mark;
    }

    public boolean willMark() {
        return mark;
    }

    public Direction direction(Point from) {
        return getValues().stream()
                .filter(direction -> direction.change(from).itsMe(this))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException());
    }
}
