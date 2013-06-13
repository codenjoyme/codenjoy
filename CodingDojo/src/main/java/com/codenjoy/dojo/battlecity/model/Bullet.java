package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Direction;

public class Bullet extends MovingObject {

    private Field field;
    private OnDestroy onDestroy;
    private boolean destroyed;

    public Bullet(Field field, Direction tankDirection, Point from, OnDestroy onDestroy) {
        super(from.getX(), from.getY(), tankDirection);
        this.field = field;
        destroyed = false;
        this.onDestroy = onDestroy;
        this.speed = 2;
    }

    public void onDestroy() {
        destroyed = true;
        if (onDestroy != null) {
            onDestroy.destroy(this);
        }
    }

    public void move() {
        for (int i = 0; i < speed; i++) {
            if (destroyed) {
                return;
            }
            int newX = direction.changeX(x);
            int newY = direction.inverted().changeY(y); // TODO fixme
            if (field.isBorder(newX, newY)) {
                onDestroy();
            } else {
                x = newX;
                y = newY;
                field.affect(this);
            }
        }
    }
}
