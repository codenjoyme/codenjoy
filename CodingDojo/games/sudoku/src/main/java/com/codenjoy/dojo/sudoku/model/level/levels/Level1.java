package com.codenjoy.dojo.sudoku.model.level.levels;

import com.codenjoy.dojo.sudoku.model.level.Level;
import com.codenjoy.dojo.sudoku.model.level.LevelImpl;

public class Level1 extends LevelImpl implements Level {

    public Level1() {
        super(
                "8 3*6 2*1 5*7 9*4*\n" +
                "2 1 9*7 4*6 8*5 3 \n" +
                "4 7*5 3 9*8 6 2*1*\n" +
                "6 9 7*5*2*4 1 3 8*\n" +
                "1*8*4*9 7 3 5*6 2 \n" +
                "3 5*2 6 8 1*9 4 7*\n" +
                "9*4 1 8*5 2 3 7*6*\n" +
                "5*6*8 4*3 7 2*1 9 \n" +
                "7 2*3 1 6 9*4 8 5*\n"
        );
    }
}
