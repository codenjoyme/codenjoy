package com.globallogic.sapperthehero;

import com.globallogic.sapperthehero.game.Board;
import com.globallogic.sapperthehero.game.Cell;
import com.globallogic.sapperthehero.game.Mine;
import com.globallogic.sapperthehero.game.MinesGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: oleksii.morozov
 * Date: 10/18/12
 * Time: 6:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class RandomMinesGenerator implements MinesGenerator {

    private Board board;

    @Override
    public List<Mine> get(int count, Board board) {
        this.board = board;
        List<Mine> result = new ArrayList<Mine>();
        for (int index = 0; index < count; index++) {
            result.add(new Mine(getRandomFreeCellOnBoard()));
        }
        return result;
    }


    private Cell getRandomFreeCellOnBoard() {
        List<Cell> freeCells = board.getFreeCells();
        if (!freeCells.isEmpty()) {
            int palce = new Random().nextInt(freeCells.size());
            Cell result = freeCells.get(palce);
            return result;
        }
//        return null;
        throw new IllegalStateException("TADA");
    }
}