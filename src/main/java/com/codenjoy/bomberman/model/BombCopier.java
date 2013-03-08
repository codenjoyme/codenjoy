package com.codenjoy.bomberman.model;

import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 1:05 PM
 */
public class BombCopier extends Bomb {

    private List<Tickable> listeners;

    public BombCopier(int x, int y, int power) {
        super(x, y, power);
        listeners = new LinkedList<Tickable>();
    }

    public BombCopier(Bomb bomb) {
        this(bomb.x, bomb.y, bomb.power);
        this.affect = bomb.affect;
        this.timer = bomb.timer;

        if (bomb instanceof BombCopier) {
            BombCopier copier = (BombCopier)bomb;
            copier.addTickListener(this);
            this.affect = null; // бомба - муляж
        }
    }

    private void addTickListener(Tickable bomb) {
        listeners.add(bomb);
    }

    private void tickAll() {
        for (Tickable bomb : listeners) {
            bomb.tick();
        }
    }

    public void tick() {
        tickAll();
        super.tick();
    }
}
