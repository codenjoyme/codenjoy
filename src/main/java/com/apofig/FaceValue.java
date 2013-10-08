package com.apofig;

import java.util.Arrays;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 4:49
 */
public class FaceValue {
    private Color[][] colors = new Color[3][3];

    public FaceValue(Color fill) {
        for (int line = 0; line < 3; line ++) {
            Arrays.fill(colors[line], fill);
        }
    }

    @Override
    public String toString() {
        String result = "";
        for (int line = 0; line < 3; line ++) {
            result += getLine(line);
        }
        return result;
    }

    public Line getLine(int line) {
        String result = "";
        for (int index = 0; index < 3; index ++) {
            result += colors[line][index].value();
        }
        return new Line(result);
    }

    public Line getRow(int row) {
        String result = "";
        for (int index = 0; index < 3; index ++) {
            result += colors[index][row].value();
        }
        return new Line(result);
    }

    public void updateLine(int line, Line data) {
        for (int index = 0; index < 3; index ++) {
            colors[line][index] = data.get(index);
        }
    }

    public void updateRow(int row, Line data) {
        for (int index = 0; index < 3; index ++) {
            colors[index][row] = data.get(index);
        }
    }
}
