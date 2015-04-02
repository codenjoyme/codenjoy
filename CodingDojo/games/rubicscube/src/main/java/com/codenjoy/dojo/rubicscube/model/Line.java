package com.codenjoy.dojo.rubicscube.model;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 5:28
 */
public class Line {

    public static final int SIZE = 3;

    private Elements[] line = new Elements[SIZE];

    public Line(String line) {
        for (int index = 0; index < SIZE; index++) {
            this.line[index] = Elements.valueOf(line.charAt(index));
        }
    }

    public Line(Elements[] newLine) {
        this.line = newLine;
    }

    public Elements get(int index) {
        return line[index];
    }

    public Line invert() {
        Elements[] newLine = new Elements[SIZE];
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
