package com.codenjoy.dojo.bomberman.model;

/**
 * User: sanja
 * Date: 21.04.13
 * Time: 0:17
 */
public class EatSpaceWalls extends WallsDecorator implements Walls {

    private int timeout;
    private int size;
    private Dice dice;

    public EatSpaceWalls(Walls walls, int size, int timeout, Dice dice) {
        super(walls);
        this.timeout = timeout;
        this.size = size;
        this.dice = dice;
    }

    @Override
    public void tick() {       // TODO закончить
        if (timeout > 0) {
            timeout--;
        } else {
            int x = dice.next(size);
            int y = dice.next(size);
            walls.destroy(x, y);
            walls.add(new Wall(x, y));
        }
        super.tick();
    }
}
