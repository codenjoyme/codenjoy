package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.RandomDice;

public class AITank extends Tank {

    private final static Dice dice = new RandomDice();
    private int act;

    public AITank(int x, int y, Direction direction) {
        super(x, y, direction, dice, 1);
    }

    @Override
    public void move() {
        if (act++ % 10 == 0) {
            act();
        }

        int c = 0;
        int x = 0;
        int y = 0;
        do {
            x = direction.changeX(getX());
            y = direction.changeY(getY());

            if (field.isBarrier(x, y)) {
                direction = Direction.random();
            }
        } while (field.isBarrier(x, y) && c++ < 10);

        moving = true;

        super.move();
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (isAlive()) {
            switch (direction) {
                case LEFT:  return Elements.AI_TANK_LEFT;
                case RIGHT: return Elements.AI_TANK_RIGHT;
                case UP:    return Elements.AI_TANK_UP;
                case DOWN:  return Elements.AI_TANK_DOWN;
                default: throw new RuntimeException("Неправильное состояние танка!");
            }
        } else {
            return Elements.BANG;
        }
    }
}
