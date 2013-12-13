package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

public class Bullet extends MovingObject {

    private Tanks field;
    private Tank owner;
    private OnDestroy onDestroy;

    public Bullet(Tanks field, Direction tankDirection, Point from, Tank owner, OnDestroy onDestroy) {
        super(from.getX(), from.getY(), tankDirection);
        this.field = field;
        this.owner = owner;
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
            onDestroy(); // TODO потестить
        } else {
            x = newX;
            y = newY;
            field.affect(this);
        }
    }

    public Tank getOwner() {
        return owner;
    }

    public void boom() {
        moving = false;
        owner = null;
    }

    public boolean destroyed() {
        return owner == null;
    }
}
