package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.Direction;

import java.util.Random;

/**
 * User: sanja
 * Date: 14.06.13
 * Time: 2:51
 */
public class AITank extends Tank {

    public AITank(int x, int y, Direction direction) {
        super(x, y, direction, new RandomDice());
    }

    @Override
    public void move() {
        if (new Random().nextBoolean()) {
            act();
        } else {
            direction = Direction.random();
            moving = true;
            super.move();
        }
    }

}
