package com.codenjoy.dojo.a2048.client.ai;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.a2048.client.Board;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;

import java.util.Arrays;

public class AISolver implements Solver<Board> {

    private static Step[] path = new Step[102400];
    private static int length = 0;
    private static int deepIndex = 0;

    public AISolver(Dice dice) {
        // do nothing
    }

    class Step {
        String directions;
        char[][] field;
        int free;
        int sum;
    }

    static char[][] fieldPrevPrev;
    static char[][] fieldPrev;

    private boolean fixLiveLock(char[][] field) {
        boolean lock = Arrays.deepEquals(fieldPrevPrev, field);

        fieldPrevPrev = fieldPrev;
        fieldPrev = field;

        return lock;
    }

    @Override
    public String get(Board board) {
        char[][] field = board.getField();

        calculateFor(field, "");

        String best = findBest();
        length = 0;

        if (fixLiveLock(field)) {
            return Direction.valueOf(best).clockwise().toString();
        }

        return best;
    }

    private String findBest() {
        int maxFree = 0;
        for (int index = 0; index < length; index++) {
            Step step = path[index];
            if (step.free > maxFree) {
                maxFree = step.free;
            }
        }

//        int maxSum = 0;
//        for (int index = 0; index < length; index++) {
//            Step step = path[index];
//            if (step.free != maxFree) continue;
//
//            if (step.sum > maxSum) {
//                maxFree = step.sum;
//            }
//        }

        int fast = 100;
        int fastIndex = 0;
        for (int index = 0; index < length; index++) {
            Step step = path[index];
            if (step.free < maxFree /*|| step.sum < maxSum*/) continue;

            int count = step.directions.split(",").length;
            if (count < fast) {
                fastIndex = index;
                fast = count;
            }
        }

        String best = path[fastIndex].directions;

        return best.split(",")[0];
    }

    private void calculateFor(char[][] field, String directions) {
        deepIndex++;
        if (deepIndex <= 7) {
            for (Direction direction : new Direction[] {Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP}) {
                Step step = new Step();
                step.directions = addComma(directions) + direction.name();

                CharNumbers numbers = new CharNumbers(field);
                numbers.move(direction);
                numbers.addNew();

                step.field = numbers.get();
                if (changed(field, step.field)) {
                    step.free = numbers.freeSpace();
                    step.sum = numbers.getSum();
                    path[length] = step;
                    length++;

                    calculateFor(step.field, step.directions);
                }
            }
        }
        deepIndex--;
    }

    private boolean changed(char[][] from, char[][] to) {
        for (int x = 0; x < from.length; x++) {
            for (int y = 0; y < from.length; y++) {
                if (from[x][y] != to[x][y]) return true;
            }
        }
        return false;
    }

    private String addComma(String directions) {
        return (directions.length() > 0)?(directions + ","):"";
    }

}
