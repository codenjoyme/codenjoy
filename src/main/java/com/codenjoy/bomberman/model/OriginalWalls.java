package com.codenjoy.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 10/10/12
 * Time: 8:29 PM
 */
public class OriginalWalls extends BasicWalls implements Iterable<Wall> {
    public OriginalWalls(int boardSize) {
        super(boardSize);

        for (int x = 2; x <= boardSize - 2; x++) {
            for (int y = 2; y <= boardSize - 2; y++) {
                if (y % 2 != 0 || x % 2 != 0) {
                    continue;
                }
                add(x, y);
            }
        }
    }

}
