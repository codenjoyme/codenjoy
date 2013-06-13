package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.PointImpl;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 6:08 PM
 */
public class Wall extends PointImpl {
    public Wall(int x, int y) {
        super(x, y);
    }

    public Wall(Wall wall) {
        super(wall);
    }

    public Wall copy() {
        return new Wall(this);
    }
}
