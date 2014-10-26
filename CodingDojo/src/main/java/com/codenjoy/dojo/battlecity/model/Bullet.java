package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;

public class Bullet extends MovingObject implements State<Elements, Player> {

    private Field field;
    private Tank owner;
    private OnDestroy onDestroy;

    public Bullet(Field field, Direction tankDirection, Point from, Tank owner, OnDestroy onDestroy) {
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
        if (field.outOfField(newX, newY)) {
            onDestroy(); // TODO заимплементить взрыв
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

    @Override
    public Elements state(Player player) {
        if (destroyed()) {
            return Elements.BANG;
        } else {
            return Elements.BULLET;
        }
    }
}
