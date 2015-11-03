package com.codenjoy.dojo.quake2d.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.Tickable;

/**
 * Артефакт Стена на поле
 */
public class Robot extends PointImpl implements Tickable, State<Elements, Player> {

    private Field field;

    public Robot(Field field, int x, int y) {
        super(x, y);
        this.field = field;
    }

    public Robot(Point point) {
        super(point);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.ROBOT;
    }

    @Override
    public void tick() {
//        move(x - 1, y - 2);
//        field.newRobot(x + 1, y);
    }
}
