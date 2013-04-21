package com.codenjoy.dojo.bomberman.model;

/**
 * User: sanja
 * Date: 21.04.13
 * Time: 0:17
 */
public class EatSpaceWalls extends WallsDecorator implements Walls { // TODO протестить класс

    private int timeout;
    private Board board;
    private int count;
    private Dice dice;

    public EatSpaceWalls(Walls walls, Board board, int timeout, int count, Dice dice) {
        super(walls);
        this.timeout = timeout;
        this.board = board;
        this.count = count;
        this.dice = dice;
    }

    @Override
    public void tick() {       // TODO закончить
        super.tick();    // TODO протестить эту строчку
        if (timeout > 0) {
            timeout--;
        } else {
            try {
                if (walls.subList(DestroyWall.class).size() >= count) {
                    return;
                }

                int x = 0;
                int y = 0;
                do {
                    x = dice.next(board.size());
                    y = dice.next(board.size());
                } while (board.isBarrier(x, y));

                walls.add(new DestroyWall(x, y));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
