package com.javatrainee.tanks;

public class Tanks {
    public Field getField() {
        Field field = new Field(3);
        return field;
    }

    public String drawField() {
        return "*********";
    }
}
