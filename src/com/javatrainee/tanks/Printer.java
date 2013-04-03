package com.javatrainee.tanks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Printer {
    private final char GROUND_SYMBOL = '*';
    private final char WALL_SYMBOL = 'X';
    private final char CONSTRUCTION_SYMBOL = '■';
    private final char BULLET_SYMBOL = '•';
    private Field field;
    private Map<Direction, Character> directionCharacterMap =
                new HashMap<Direction, Character>(){{put(Direction.UP, '▲');
                                                                              put(Direction.RIGHT, '►');
                                                                              put(Direction.DOWN, '▼');
                                                                              put(Direction.LEFT, '◄'); }};

    public Printer(Field field) {
        this.field = field;
    }

    public String drawField() {
        char[][] battleField = fillField();
        return toString(battleField);
    }

    private void addHorizontalBorders(int borderedFieldSize, char[][] battleFieldWithBorder) {
        for (int colNumber = 0; colNumber < borderedFieldSize; colNumber++) {
            battleFieldWithBorder[0][colNumber] = WALL_SYMBOL;
            battleFieldWithBorder[borderedFieldSize - 1][colNumber] = WALL_SYMBOL;
        }
    }

    private void addVerticalBorders(int borderedFieldSize, char[][] battleFieldWithBorder) {
        for (int rowNumber = 0; rowNumber < borderedFieldSize; rowNumber++) {
            battleFieldWithBorder[rowNumber][0] = WALL_SYMBOL;
            battleFieldWithBorder[rowNumber][borderedFieldSize - 1] = WALL_SYMBOL;
        }
    }

    private String toString(char[][] battleField) {
        String battleFieldAsString = "";
        for(char[] currentRow:battleField) {
            battleFieldAsString += Arrays.toString(currentRow) + "\n";
        }
        return deleteStatements(battleFieldAsString);
    }

    private String deleteStatements(String stringToProcess) {
        final String WHITE_SPACE = " ";
        final String COMMA = "\\,";
        final String OPENING_BRACKET = "\\[";
        final String CLOSING_BRACKET = "\\]";
        return stringToProcess.replaceAll(WHITE_SPACE, "").
                replaceAll(COMMA, "").
                replaceAll(OPENING_BRACKET, "").
                replaceAll(CLOSING_BRACKET, "");
    }

    private char[][] fillField() {
        final int fieldSize = field.getSize() + 2;
        char[][] battleField = new char[fieldSize][fieldSize];
        for (int rowNumber = 0; rowNumber < fieldSize; rowNumber++) {
            for(int colNumber = 0; colNumber < fieldSize; colNumber++) {
                battleField[rowNumber][colNumber] = GROUND_SYMBOL;
            }
        }

        Construction construction = field.getConstruction();
        if(construction != null) {
            int coordinateX = construction.getCoordinates()[0] + 1;
            int coordinateY = construction.getCoordinates()[1] + 1;
            battleField[coordinateY][coordinateX] = CONSTRUCTION_SYMBOL;
        }

        Tank tank = field.getTank();
        if(tank != null) {
            int coordinateX = tank.getCoordinates()[0] + 1;
            int coordinateY = tank.getCoordinates()[1] + 1;
            battleField[coordinateY][coordinateX] =
                                   directionCharacterMap.get(tank.getDirection());

            Bullet bullet = tank.getBullet();
            if(bullet != null) {
                int bulletCoordinateX = bullet.getCoordinates()[0] + 1;
                int bulletCoordinateY = bullet.getCoordinates()[1] + 1;
                battleField[bulletCoordinateY][bulletCoordinateX] = BULLET_SYMBOL;
            }
        }

        addHorizontalBorders(fieldSize, battleField);
        addVerticalBorders(fieldSize, battleField);
        return battleField;
    }

}
