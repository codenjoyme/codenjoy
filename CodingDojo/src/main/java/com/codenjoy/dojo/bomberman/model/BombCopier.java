package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.Tickable;

import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 1:05 PM
 */
public class BombCopier extends Bomb {

    private List<Tickable> copies;

    public BombCopier(int x, int y, int power) {
        super(x, y, power);
        copies = new LinkedList<Tickable>();
    }

    public BombCopier(Bomb bomb) {
        this(bomb.x, bomb.y, bomb.power);
        this.affect = bomb.affect;
        this.timer = bomb.timer;

        if (bomb instanceof BombCopier) {
            BombCopier copier = (BombCopier)bomb;
            copier.copies.add(this);
            this.affect = null; // бомба - муляж
        }
    }

    public void tick() {
        for (Tickable bomb : copies) {
            bomb.tick();
        }
        super.tick();
    }
}
