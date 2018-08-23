package com.codenjoy.dojo.reversi.model;

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


import com.codenjoy.dojo.reversi.model.items.Chip;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.QDirection.*;
import static com.codenjoy.dojo.services.QDirection.LEFT_DOWN;

public class Flipper {

    private GetChip field;

    public Flipper(GetChip field) {
        this.field = field;
    }

    public static class Turn {

        public Chip chip;
        public QDirection direction;

        public Turn(Chip chip, QDirection direction) {
            this.direction = direction;
            this.chip = chip;
        }
    }

    public List<Turn> turns(boolean color) {
        List<Turn> result = new LinkedList<>();
        for (Point pt : field.freeSpaces()) {
            for (QDirection direction : directions()) {
                Chip chip = new Chip(color, pt, field);
                chip.flip(chip, direction,
                        c -> result.add(new Turn(chip, direction)));
            };
        };
        return result;
    }

    public boolean cantFlip(boolean color) {
        return turns(color).isEmpty();
    }

    public int flip(Chip current) {
        int count = 0;
        for (QDirection direction : directions()){
            count += current.flip(direction);
        }
        return count;
    }

    private List<QDirection> directions() {
        return Arrays.asList(LEFT, LEFT_UP, UP, RIGHT_UP,
                RIGHT, RIGHT_DOWN, DOWN, LEFT_DOWN);
    }
}
