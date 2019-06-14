package com.codenjoy.dojo.excitebike.model.items.bike;


import com.codenjoy.dojo.services.printer.CharElements;

public enum BikeType implements CharElements {
    BIKE('o'),
    BIKE_FALLEN('~'),
    BIKE_INCLINE_LEFT('('),
    BIKE_INCLINE_RIGHT(')'),
    OTHER_BIKE('e'),
    OTHER_BIKE_FALLEN('_'),
    OTHER_BIKE_INCLINE_LEFT('z'),
    OTHER_BIKE_INCLINE_RIGHT('x');

    final char ch;

    BikeType(char ch) {
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

    public static BikeType valueOf(char ch) {
        for (BikeType el : BikeType.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }

}
