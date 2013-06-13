package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.PointImpl;

/**
 * User: sanja
 * Date: 20.04.13
 * Time: 15:46
 */
public class Blast extends PointImpl {

    private Bomberman bomberman;

    public Blast(int x, int y, Bomberman bomberman) {
        super(x, y);
        this.bomberman = bomberman;
    }

    public boolean itsMine(Bomberman bomberman) {
        return this.bomberman == bomberman;
    }
}
