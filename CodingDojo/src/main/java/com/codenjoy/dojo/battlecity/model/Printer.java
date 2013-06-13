package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Direction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Printer {

    private Field field;
    private Elements[][] battleField;
    private final int size;

    private Map<Direction, Elements> directionCharacterMap =
            new HashMap<Direction, Elements>() {{
                put(Direction.UP, Elements.TANK_UP);
                put(Direction.RIGHT, Elements.TANK_RIGHT);
                put(Direction.DOWN, Elements.TANK_DOWN);
                put(Direction.LEFT, Elements.TANK_LEFT);
            }};

    public Printer(Field field) {
        this.field = field;
        size = field.getSize();
    }

    private void addHorizontalBorders() {
        for (int colNumber = 0; colNumber < size; colNumber++) {
            battleField[0][colNumber] = Elements.WALL;
            battleField[size - 1][colNumber] = Elements.WALL;
        }
    }

    private void addVerticalBorders() {
        for (int rowNumber = 0; rowNumber < size; rowNumber++) {
            battleField[rowNumber][0] = Elements.WALL;
            battleField[rowNumber][size - 1] = Elements.WALL;
        }
    }

    @Override
    public String toString() {
        fillField();
        
        String string = "";
        for (Elements[] currentRow : battleField) {
            for (Elements element : currentRow) {
                string += element.ch;
            }
            string += "\n";
        }
        return string;
    }

    private void fillField() {
        battleField = new Elements[size][size];

        for (int rowNumber = 0; rowNumber < size; rowNumber++) {
            for (int colNumber = 0; colNumber < size; colNumber++) {
                set(new Point(rowNumber, colNumber), Elements.GROUND);
            }
        }

        List<Construction> constructions = field.getConstructions();
        for (Construction construction : constructions) {
            set(construction, construction.getChar());
        }

        Tank tank = field.getTank();
        if (tank != null) {
            set(tank, directionCharacterMap.get(tank.getDirection()));

            for (Bullet bullet : tank.getBullets()) {
                if (!bullet.equals(tank)) {
                    set(bullet, Elements.BULLET);
                }
            }
        }

        addHorizontalBorders();
        addVerticalBorders();
    }

    private void set(Point pt, Elements element) {
        if (pt.getY() == -1 || pt.getX() == -1) {
            return;
        }

        battleField[size - 1 - pt.getY()][pt.getX()] = element;
    }

}
