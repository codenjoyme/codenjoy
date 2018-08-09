package com.codenjoy.dojo.minesweeper.model;

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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.List;

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
    private Dice dice;

    public RandomMinesGenerator(Dice dice) {
        this.dice = dice;
    }

    public List<Mine> get(int count, Field board) {
        freeCells = board.getFreeCells();
        removeSafeAreaFromFreeCells();
        List<Mine> result = new ArrayList<Mine>();
        for (int index = 0; index < count; index++) {
            Mine mine = new Mine(getRandomFreeCellOnBoard());
            mine.init(board);
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
            int place = dice.next(freeCells.size());
            return freeCells.get(place);
        }

        throw new IllegalStateException("This exception should not be present");
    }
}
