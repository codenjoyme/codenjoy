package com.codenjoy.dojo.collapse.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.sudoku.model.Cell;
import com.codenjoy.dojo.sudoku.model.Level;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by Sanja
 */
public class LevelBuilder {

    private final int size;
    private Dice dice;

    public LevelBuilder(Dice dice, int size) {
        this.dice = dice;
        this.size = size;
    }

    public String getBoard() {
        StringBuffer result = new StringBuffer();

        char borderChar = Elements.BORDER.ch();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (x == 0 || y == 0 || x == size - 1 || y == size - 1) {
                    result.append(borderChar);
                } else {
                    result.append(dice.next(8) + 1);
                }
            }
        }
        return result.toString();
    }

}
