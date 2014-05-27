package com.codenjoy.dojo.chess.model.figures;

import com.codenjoy.dojo.chess.model.Field;
import com.codenjoy.dojo.services.*;

public class Figure extends PointImpl implements Tickable {

    private Field field;
    private boolean alive;

    public Figure(Point xy) {
        super(xy);
        alive = true;
    }

    public void init(Field field) {
        this.field = field;
    }


    @Override
    public void tick() {
        if (!alive) return;

        // TODO
    }

    public boolean isAlive() {
        return alive;
    }
}
