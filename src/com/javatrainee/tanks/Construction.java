package com.javatrainee.tanks;

public class Construction {
    private String symbolOfConstruction;

    public Construction() {
        symbolOfConstruction = Symbols.CONSTRUCTION_SYMBOL;
    }

    public String drawConstruction() {
        return symbolOfConstruction;
    }
}
