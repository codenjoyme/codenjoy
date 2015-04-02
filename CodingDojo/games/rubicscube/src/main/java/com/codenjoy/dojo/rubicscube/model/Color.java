package com.codenjoy.dojo.rubicscube.model;

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

    public char value() {
        return color;
    }

    public static Color valueOf(char ch) {
        for (Color color : Color.values()) {
            if (color.value() == ch) {
                return color;
            }
        }
        throw new IllegalArgumentException("Не найден цвет для " + ch);
    }
}
