package com.codenjoy.dojo.collapse.model;

import com.codenjoy.dojo.services.Dice;

import java.util.ArrayList;

/**
 * Created by Sanja
 */
public class LevelBuilder {

    private int[][] field;
    private int size;
    private Dice dice;

    public LevelBuilder(Dice dice, int size) {
        this.dice = dice;
        this.size = size;
        field = new int[size][size];

        for (int x = 1; x < size - 1; x++) {
            for (int y = 1; y < size - 1; y++) {
                Container<Integer, Integer> numbers = new Container<Integer, Integer>();
                for (int i = 1; i < 9; i++) {
                    numbers.add(i);
                }

                for (int dx = -1; dx <= 1; dx ++) {
                    for (int dy = -1; dy <= 1; dy ++) {
                        if (dx == 0 && dy == 0) continue;

                        numbers.remove(field[x + dx][y + dy]);
                    }
                }

                int index = dice.next(numbers.size());
                field[x][y] = new ArrayList<Integer>(numbers.values()).get(index);
            }
        }
    }

    public String getBoard() {
        StringBuffer result = new StringBuffer();

        char borderChar = Elements.BORDER.ch();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (x == 0 || y == 0 || x == size - 1 || y == size - 1) {
                    result.append(borderChar);
                } else {
                    result.append(field[x][y]);
                }
            }
        }
        return result.toString();
    }

}
