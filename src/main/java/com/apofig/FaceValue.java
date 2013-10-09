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

    private FaceValue() { }

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

    public void rotateClockwise() {
        FaceValue newFace = new FaceValue();

        newFace.updateRow(2, getLine(0));
        newFace.updateRow(1, getLine(1));
        newFace.updateRow(0, getLine(2));

        colors = newFace.colors;
    }

    public Color get(Neighbor neighborFace) {
        switch (neighborFace) {
            case UP: return colors[0][1];
            case LEFT: return colors[1][0];
            case DOWN: return colors[2][1];
            case RIGHT: return colors[1][2];
        }
        throw new IllegalArgumentException("Ой, у нас более чем 3х мерное пространство!");
    }
}
