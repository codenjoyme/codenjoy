package com.apofig;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 4:50
 */
public enum Color {
    RED('R'), GREEN('G'), YELLOW('Y'), WHITE('W'), ORANGE('O'), BLUE('B');

    private char color;

    Color(char color) {
        this.color = color;
    }

    public String value() {
        return String.valueOf(color);
    }
}
