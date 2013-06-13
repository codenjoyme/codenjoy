package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.Tickable;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 1:37 PM
 */
public class Bomb extends PointImpl implements Tickable {
    protected int timer = 5;
    protected Boom affect;
    protected int power;
    private Bomberman owner;

    public Bomb(Bomberman owner, int x, int y, int power) {
        super(x, y);
        this.power = power;
        this.owner = owner;
    }

    public void tick() {
        timer--;
        if (timer == 0) {
            boom();
        }
    }

    private void boom() {
        if (affect != null) {
            affect.boom(this);
        }
    }

    public void setAffect(Boom affect) {
        this.affect = affect;
    }

    public int getTimer() {
        return timer;
    }

    public int getPower() {
        return power;
    }

    public boolean isExploded() {
        return timer == 0;
    }

    public boolean itsMine(Bomberman bomberman) {
        return this.owner == bomberman;
    }

    public Bomberman getOwner() {
        return owner;
    }
}
