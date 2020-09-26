package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Direction;

public class Sliding {

    public static final int TAKE_CONTROL_EVERY_TICKS = 2;
    private Field field;
    
    private int tick;
    private Direction before;

    public Sliding(Field field) {
        this.field = field;
    }

    public Direction act(Tank tank) {
        if (!field.isIce(tank)) {
            tick = 0;
            return before = tank.getDirection();
        }

        tick++;

        if (tick % TAKE_CONTROL_EVERY_TICKS == 0) {
            before = tank.getDirection();
        } else {
            // ignore current direction because sliding
        }

        return before;
    }
}
