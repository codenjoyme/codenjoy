package com.globallogic.training.oleksii.morozov.sapperthehero.game.minegenerator.impl;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.minegenerator.MinesGenerator;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Cell;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Mine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: oleksii.morozov
 * Date: 10/18/12
 * Time: 6:54 PM
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
            int place = new Random().nextInt(freeCells.size());
            return freeCells.get(place);
        }

        throw new IllegalStateException("This exception should not be present");
    }
}