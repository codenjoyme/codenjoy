package com.codenjoy.dojo.bomberman.model;

import java.util.Iterator;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 8:17 PM
 */
public class DestroyWalls extends WallsDecorator implements Walls {

    private int size;
    private int count;
    private Dice dice;

    public DestroyWalls(Walls walls, Dice dice) {
        super(walls);
        this.dice = dice;
        for (Wall wall : walls) {
            this.walls.add(new DestroyWall(wall.x, wall.y));
        }
    }

    public DestroyWalls(Walls walls, int size, Dice dice) {
        super(walls);
        this.dice = dice;
        this.size = size;
        this.count = size*size/10;
        randomFill();
    }

    private void randomFill() {
        int index = 0;
        int counter = 0;
        do {
            int x = dice.next(size);
            int y = dice.next(size);

            if (walls.itsMe(x, y)) {
                continue;
            }

            if (x <= 3 && y == 1) {  // чтобы бомбермену было откуда начинать
                continue;
            }

            walls.add(new DestroyWall(x, y));

            index++;
            counter++;
        } while (index != count && counter < 10000);
        if (counter == 10000) {
            throw new  RuntimeException("Dead loop at DestroyWalls.randomFill!");
        }
    }
}
