package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

public class Action extends PointImpl {

    private boolean mark;

    public Action(Point pt, boolean mark) {
        super(pt);
        this.mark = mark;
    }

    public boolean willMark() {
        return mark;
    }
}
