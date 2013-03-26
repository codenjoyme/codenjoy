package com.javatrainee.tanks;

import java.util.Arrays;

public class Printer {
    private static final char GROUND_SYMBOL = '*';
    private static final char WALL_SYMBOL = 'X';
    private static final char CONSTRUCTION_SYMBOL = '■';
    private static final char TANK_SYMBOL = 'T';
    private Field field;

    public Printer(Field field) {
        this.field = field;
    }

    public String drawField() {
        char[][] battleField = {{'*', '*'},{ '*', '*'}};
        return toString(battleField);
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
}
