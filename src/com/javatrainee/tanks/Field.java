package com.javatrainee.tanks;

import java.util.Arrays;

public class Field {
    private int size;

    private String body = "";

    public Field(int size) {
        this.size = size;
        initializeLine();
    }

    public String  getFieldAsLine() {
         return body;
    }

    public int getSize() {
        return size;
    }

    public String getFieldLine() {
        String line = "";
        for(int i = 0;  i < size; i++) {
            line+=Symbols.GROUND_SYMBOL;
        }
        return line;
    }
    private void initializeLine() {
        for(int i = 0;  i < size*size; i++) {
            body+=Symbols.GROUND_SYMBOL;
        }
    }

    public void setConstructionAt(int coordinateX, int coordinateY) {
        int indexOfConstruction = calculateIndexByCoordinates(coordinateX,coordinateY);
        String previousField = body.substring(0, indexOfConstruction - 1);
        String overField = body.substring(indexOfConstruction);
        body = previousField +  Symbols.CONSTRUCTION_SYMBOL + overField;
    }

    private int calculateIndexByCoordinates(int X, int Y) {
          return X*size + Y +1;
    }

}
