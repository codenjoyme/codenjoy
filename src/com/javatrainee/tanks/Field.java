package com.javatrainee.tanks;

public class Field {
    private int size;
    private final String GROUND_SYMBOL = "*";
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

    private void initializeLine() {
        for(int i = 0;  i < size*size; i++) {
            body+=GROUND_SYMBOL;
        }
    }
}
