package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

public class Action extends PointImpl {

    public boolean act;

    public Action(Point pt, boolean act) {
        super(pt);
        this.act = act;
    }
}
