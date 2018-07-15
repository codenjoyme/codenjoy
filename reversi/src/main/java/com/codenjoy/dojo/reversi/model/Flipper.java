package com.codenjoy.dojo.reversi.model;

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
                chip.flip(chip, direction, c -> result.add(new Turn(chip, direction)));
            };
        };
        return result;
    }

    public boolean canFlip(boolean color) {
        return !turns(color).isEmpty();
    }

    public boolean flipFromChip(Chip current) {
        boolean result = false;
        for (QDirection direction : directions()){
            result |= current.flip(direction);
        }
        return result;
    }

    private List<QDirection> directions() {
        return Arrays.asList(LEFT, LEFT_UP, UP, RIGHT_UP,
                RIGHT, RIGHT_DOWN, DOWN, LEFT_DOWN);
    }
}
