package com.javatrainee.tanks;

public class Tanks {

    private Field field;

    public Field getField() {
        return field;
    }

    public Tanks(int fieldSize) {
        field = new Field(fieldSize);
    }
    /*
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
        return Symbols.WALL_SYMBOL + field.getFieldLine().replaceAll("\\*",Symbols.WALL_SYMBOL) + Symbols.WALL_SYMBOL;
    }

    private String addVerticalBorders() {
        String modifiedField = "";
        int sizeOfLine = field.getSize();
        for(int lineNumber = 0; lineNumber < sizeOfLine; lineNumber++) {
            String oneLineFromField =  field.getFieldAsLine().substring(lineNumber*sizeOfLine, (lineNumber+1)*sizeOfLine);
             modifiedField+=Symbols.WALL_SYMBOL + oneLineFromField
                                                                          + Symbols.WALL_SYMBOL;
        }
        return  modifiedField;
    }

    public String drawTank() {
        return Symbols.TANK_SYMBOL;
    }
    */
}
