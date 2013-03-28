package com.javatrainee.tanks;

import java.util.*;

public class Printer {
    private final char GROUND_SYMBOL = '*';
    private final char WALL_SYMBOL = 'X';
    private final char CONSTRUCTION_SYMBOL = '■';
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
       // char[][] battleFieldWithBorder = addBorders(battleField);
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
            battleFieldAsString += Arrays.toString(currentRow);
        }
        return deleteStatements(battleFieldAsString);
    }

    private String deleteStatements(String stringToProcess) {
        final String WHITE_SPACE = "\\s";
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

        if(field.getConstruction()!=null) {
            int coordinateX = field.getConstruction().getCoordinates()[0] + 1;
            int coordinateY = field.getConstruction().getCoordinates()[1] + 1;
            battleField[coordinateY][coordinateX] = CONSTRUCTION_SYMBOL;
        }

        if(field.getTank()!=null) {
            int coordinateX = field.getTank().getCoordinates()[0] + 1;
            int coordinateY = field.getTank().getCoordinates()[1] + 1;
            battleField[coordinateY][coordinateX] = directionCharacterMap.get(field.getTank().getDirection());
        }

        addHorizontalBorders(fieldSize, battleField);
        addVerticalBorders(fieldSize, battleField);
        return battleField;
    }

}
