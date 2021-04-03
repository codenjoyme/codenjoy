package com.codenjoy.dojo.minesweeper.client.ai.logic;

public enum Value {

    BANG(-5),
    FLAG(-4),
    DETECTOR(-3),
    HIDDEN(-2),
    BORDER(-1),

    NONE(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8);

    private int value;

    Value(int value) {
        this.value = value;
    }

    public static Value valueOf(int i) {
        for (Value value : values()) {
            if (value.value == i) {
                return value;
            }
        }
        throw new IllegalArgumentException("Value not found for: " + i);
    }

    public int get() {
        return value;
    }
}
