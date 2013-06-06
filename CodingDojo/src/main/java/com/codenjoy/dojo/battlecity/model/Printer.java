package com.codenjoy.dojo.battlecity.model;

import java.util.HashMap;
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
            battleField[0][colNumber] = Elements.WALL_SYMBOL;
            battleField[size - 1][colNumber] = Elements.WALL_SYMBOL;
        }
    }

    private void addVerticalBorders() {
        for (int rowNumber = 0; rowNumber < size; rowNumber++) {
            battleField[rowNumber][0] = Elements.WALL_SYMBOL;
            battleField[rowNumber][size - 1] = Elements.WALL_SYMBOL;
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
        return deleteStatements(string);
    }

    private String deleteStatements(String string) {
        final String WHITE_SPACE = " ";
        final String COMMA = "\\,";
        final String OPENING_BRACKET = "\\[";
        final String CLOSING_BRACKET = "\\]";
        return string.replaceAll(WHITE_SPACE, "").
                replaceAll(COMMA, "").
                replaceAll(OPENING_BRACKET, "").
                replaceAll(CLOSING_BRACKET, "");
    }

    private void fillField() {
        battleField = new Elements[size][size];

        for (int rowNumber = 0; rowNumber < size; rowNumber++) {
            for (int colNumber = 0; colNumber < size; colNumber++) {
                set(new Point(rowNumber, colNumber), Elements.GROUND_SYMBOL);
            }
        }

        Construction construction = field.getConstruction();
        if (construction != null) {
            set(construction, Elements.CONSTRUCTION_SYMBOL);
        }

        Tank tank = field.getTank();
        if (tank != null) {
            set(tank, directionCharacterMap.get(tank.getDirection()));

            Bullet bullet = tank.getBullet();
            if (bullet != null) {
                set(bullet, Elements.BULLET_SYMBOL);
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
