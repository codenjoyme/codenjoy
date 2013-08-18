package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

public class Bullet extends MovingObject {

    private Tanks field;
    private OnDestroy onDestroy;

    public Bullet(Tanks field, Direction tankDirection, Point from, OnDestroy onDestroy) {
        super(from.getX(), from.getY(), tankDirection);
        this.field = field;
        moving = true;
        this.onDestroy = onDestroy;
        this.speed = 2;
    }

    public void onDestroy() {
        moving = false;
        if (onDestroy != null) {
            onDestroy.destroy(this);
        }
    }

    @Override
    public void moving(int newX, int newY) {
        if (field.isBorder(newX, newY)) {
            onDestroy();
        } else {
            x = newX;
            y = newY;
            field.affect(this);
        }
    }
}
