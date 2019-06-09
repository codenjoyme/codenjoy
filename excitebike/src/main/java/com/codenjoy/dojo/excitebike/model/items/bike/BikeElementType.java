package com.codenjoy.dojo.excitebike.model.items.bike;


import com.codenjoy.dojo.services.printer.CharElements;

public enum BikeElementType implements CharElements {
    BIKE('o'),
    BIKE_FALLEN('~'),
    BIKE_INCLINE_LEFT('('),
    BIKE_INCLINE_RIGHT(')');

    final char ch;

    BikeElementType(char ch) {
        this.ch = ch;
    }

    @Override
    public char ch() {
        return ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static BikeElementType valueOf(char ch) {
        for (BikeElementType el : BikeElementType.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }

}
