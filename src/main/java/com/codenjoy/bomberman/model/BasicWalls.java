package com.codenjoy.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 10/10/12
 * Time: 9:00 AM
 */
public class BasicWalls extends Walls implements Iterable<Wall> {
    public BasicWalls(int boardSize) {
        super();

        for (int x = 0; x < boardSize; x++) {
            add(x, 0);
            add(x, boardSize - 1);
        }

        final int D = 1;
        for (int y = D; y < boardSize - D; y++) {
            add(0, y);
            add(boardSize - 1, y);
        }
    }

}
