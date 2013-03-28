package com.javatrainee.tanks;

public class Tanks {

    private Field field;

    public Field getField() {
        return field;
    }

    public Tanks(int fieldSize) {
        field = new Field(fieldSize);
     }

}
