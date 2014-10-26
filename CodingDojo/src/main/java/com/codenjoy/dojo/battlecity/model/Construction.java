package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.*;

public class Construction extends PointImpl implements Tickable, State<Elements, Player> {

    public static final int REGENERATE_TIME = 30;

    private Elements ch;
    private int timer;

    public Construction(int x, int y) {
        super(x, y);
        ch = Elements.CONSTRUCTION;
    }

    public Construction(Point xy) {
        this(xy.getX(), xy.getY());
    }

    public void destroyFrom(Direction bulletDirection) {
        if (ch.power == 1) {
            ch = Elements.CONSTRUCTION_DESTROYED;
            return;
        }
        if (bulletDirection.equals(Direction.UP)) {
            switch (ch) {
                case CONSTRUCTION : ch = Elements.CONSTRUCTION_DESTROYED_DOWN; break;
                case CONSTRUCTION_DESTROYED_DOWN : ch = Elements.CONSTRUCTION_DESTROYED_DOWN_TWICE; break;
                case CONSTRUCTION_DESTROYED_UP : ch = Elements.CONSTRUCTION_DESTROYED_UP_DOWN; break;
                case CONSTRUCTION_DESTROYED_LEFT : ch = Elements.CONSTRUCTION_DESTROYED_DOWN_LEFT; break;
                case CONSTRUCTION_DESTROYED_RIGHT : ch = Elements.CONSTRUCTION_DESTROYED_DOWN_RIGHT; break;
            }
        } else if (bulletDirection.equals(Direction.RIGHT)) {
            switch (ch) {
                case CONSTRUCTION : ch = Elements.CONSTRUCTION_DESTROYED_LEFT; break;
                case CONSTRUCTION_DESTROYED_LEFT : ch = Elements.CONSTRUCTION_DESTROYED_LEFT_TWICE; break;
                case CONSTRUCTION_DESTROYED_RIGHT : ch = Elements.CONSTRUCTION_DESTROYED_LEFT_RIGHT; break;
                case CONSTRUCTION_DESTROYED_UP : ch = Elements.CONSTRUCTION_DESTROYED_UP_LEFT; break;
                case CONSTRUCTION_DESTROYED_DOWN : ch = Elements.CONSTRUCTION_DESTROYED_DOWN_LEFT; break;
            }
        } else if (bulletDirection.equals(Direction.LEFT)) {
            switch (ch) {
                case CONSTRUCTION : ch = Elements.CONSTRUCTION_DESTROYED_RIGHT; break;
                case CONSTRUCTION_DESTROYED_RIGHT : ch = Elements.CONSTRUCTION_DESTROYED_RIGHT_TWICE; break;
                case CONSTRUCTION_DESTROYED_UP : ch = Elements.CONSTRUCTION_DESTROYED_RIGHT_UP; break;
                case CONSTRUCTION_DESTROYED_DOWN : ch = Elements.CONSTRUCTION_DESTROYED_DOWN_RIGHT; break;
                case CONSTRUCTION_DESTROYED_LEFT : ch = Elements.CONSTRUCTION_DESTROYED_LEFT_RIGHT; break;
            }
        } else if (bulletDirection.equals(Direction.DOWN)) {
            switch (ch) {
                case CONSTRUCTION : ch = Elements.CONSTRUCTION_DESTROYED_UP; break;
                case CONSTRUCTION_DESTROYED_UP : ch = Elements.CONSTRUCTION_DESTROYED_UP_TWICE; break;
                case CONSTRUCTION_DESTROYED_RIGHT : ch = Elements.CONSTRUCTION_DESTROYED_RIGHT_UP; break;
                case CONSTRUCTION_DESTROYED_DOWN : ch = Elements.CONSTRUCTION_DESTROYED_UP_DOWN; break;
                case CONSTRUCTION_DESTROYED_LEFT : ch = Elements.CONSTRUCTION_DESTROYED_UP_LEFT; break;
            }
        }
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (!destroyed()) {
            return ch;
        } else  {
            return Elements.BATTLE_GROUND;
        }
    }

    @Override
    public void tick() {
        if (timer == REGENERATE_TIME) {
            timer = 0;
            ch = Elements.CONSTRUCTION;
        }
        if (destroyed()) {
            timer++;
        }
    }

    public boolean destroyed() {
        return ch.power == 0;
    }
}
