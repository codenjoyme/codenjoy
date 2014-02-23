package com.apofig;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 5:28
 */
public class Line {

    public static final int SIZE = 3;

    private Color[] line = new Color[SIZE];

    public Line(String line) {
        for (int index = 0; index < SIZE; index++) {
            this.line[index] = Color.valueOf(line.charAt(index));
        }
    }

    public Line(Color[] newLine) {
        this.line = newLine;
    }

    public Color get(int index) {
        return line[index];
    }

    public Line invert() {
        Color[] newLine = new Color[SIZE];
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
