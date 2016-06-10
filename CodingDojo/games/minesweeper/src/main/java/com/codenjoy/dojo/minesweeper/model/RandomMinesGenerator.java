package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: oleksii.morozov
 * Date: 10/18/12
 * Time: 6:54 PM
 */
public class RandomMinesGenerator implements MinesGenerator {
    public static int SAFE_AREA_X_0 = 1;
    public static int SAFE_AREA_X_1 = 3;
    public static int SAFE_AREA_Y_0 = 1;
    public static int SAFE_AREA_Y_1 = 3;
    private List<Point> freeCells;

    public List<Mine> get(int count, Field board) {
        freeCells = board.getFreeCells();
        removeSafeAreaFromFreeCells();
        List<Mine> result = new ArrayList<Mine>();
        for (int index = 0; index < count; index++) {
            Mine mine = new Mine(getRandomFreeCellOnBoard());
            mine.setBoard(board);
            result.add(mine);
            freeCells.remove(mine);
        }
        return result;
    }

    private void removeSafeAreaFromFreeCells() {
        for (int i = 0; i < freeCells.size(); i++) {
            Point point = freeCells.get(i);
            if (isInSafeArea(point)) {
                freeCells.remove(i--);
            }
        }
    }

    private boolean isInSafeArea(Point point) {
        return point.getX() >= SAFE_AREA_X_0 && point.getX() <= SAFE_AREA_X_1
                && point.getY() >= SAFE_AREA_Y_0 && point.getY() <= SAFE_AREA_Y_1;
    }


    private Point getRandomFreeCellOnBoard() {
        if (!freeCells.isEmpty()) {
            int place = new Random().nextInt(freeCells.size());
            return freeCells.get(place);
        }

        throw new IllegalStateException("This exception should not be present");
    }
}