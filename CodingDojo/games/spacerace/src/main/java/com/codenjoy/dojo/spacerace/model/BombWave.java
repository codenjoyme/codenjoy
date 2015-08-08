package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

/**
 * Created by indigo on 08.08.2015.
 */
public class BombWave extends PointImpl {
    private Bomb bomb;

    public BombWave(int x, int y, Bomb bomb) {
        super(x, y);
        this.bomb = bomb;
    }

    public Bomb getBomb() {
        return bomb;
    }
}
