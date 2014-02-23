package com.codenjoy.dojo.rubicscube.model;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 5:28
 */
public class Line {

    public static final int SIZE = 3;

    private Element[] line = new Element[SIZE];

    public Line(String line) {
        for (int index = 0; index < SIZE; index++) {
            this.line[index] = Element.valueOf(line.charAt(index));
        }
    }

    public Line(Element[] newLine) {
        this.line = newLine;
    }

    public Element get(int index) {
        return line[index];
    }

    public Line invert() {
        Element[] newLine = new Element[SIZE];
        for (int index = 0; index < SIZE; index++) {
            newLine[SIZE - 1 - index] = line[index];
        }

        return new Line(newLine);
    }

    @Override
    public String toString() {
        String result = "";
        for (int index = 0; index < SIZE; index++) {
            result += get(index).value();
        }
        return result;
    }
}
