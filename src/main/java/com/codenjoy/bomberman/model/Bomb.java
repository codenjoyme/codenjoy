package com.codenjoy.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 1:37 PM
 */
public class Bomb extends Point {
    private int timer = 5;
    private Boom affect;
    private int blastWaveLength = 1;

    public Bomb(int x, int y) {
        super(x, y);
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

    public int getBlastWaveLength() {
        return blastWaveLength;
    }

    public boolean isExploded() {
        return timer == 0;
    }
}
