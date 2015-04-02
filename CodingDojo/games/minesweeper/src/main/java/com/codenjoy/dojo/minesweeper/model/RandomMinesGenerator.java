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

    private List<Point> freeCells;

    public List<Mine> get(int count, Field board) {
        freeCells = board.getFreeCells();
        List<Mine> result = new ArrayList<Mine>();
        for (int index = 0; index < count; index++) {
            Mine mine = new Mine(getRandomFreeCellOnBoard());
            mine.setBoard(board);
            result.add(mine);
            freeCells.remove(mine);
        }
        return result;
    }


    private Point getRandomFreeCellOnBoard() {
        if (!freeCells.isEmpty()) {
            int place = new Random().nextInt(freeCells.size());
            return freeCells.get(place);
        }

        throw new IllegalStateException("This exception should not be present");
    }
}