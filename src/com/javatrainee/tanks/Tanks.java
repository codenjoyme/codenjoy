package com.javatrainee.tanks;

public class Tanks {

    private Field field;

    public Field getField() {
        return field;
    }

    public Tanks(int fieldSize) {
        field = new Field(fieldSize);
    }

    public String drawField() {
        return concatenateLinesWithBorder();
    }

    public String drawFieldWithoutBorder() {
        return field.getFieldAsLine();
    }

    private String concatenateLinesWithBorder() {
        String horizontalBorder = createHorizontalBorders();
        String fieldWithVerticalBorders = addVerticalBorders();
        return horizontalBorder + fieldWithVerticalBorders + horizontalBorder;
    }

    private String createHorizontalBorders() {
        int fieldSizeWithBorder = field.getSize() + 2;
        return field.getFieldAsLine().substring(0, fieldSizeWithBorder).replaceAll("\\*",Symbols.WALL_SYMBOL);
    }

    private String addVerticalBorders() {
        String oneLineFromField = field.getFieldAsLine().substring(0, field.getSize());
        String modifiedField = "";
        for(int i = 0; i < field.getSize(); i++){
             modifiedField+=Symbols.WALL_SYMBOL + oneLineFromField
                                                                          + Symbols.WALL_SYMBOL;
        }
        return  modifiedField;
    }

}
