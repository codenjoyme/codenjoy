package com.codenjoy.dojo.rubicscube.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import java.util.Arrays;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 4:49
 */
public class FaceValue {
    private Elements[][] colors = new Elements[3][3];

    public FaceValue(Elements fill) {
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
            result += colors[line][index].toString();
        }
        return new Line(result);
    }

    public Line getRow(int row) {
        String result = "";
        for (int index = 0; index < 3; index ++) {
            result += colors[index][row].toString();
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

    public Elements get(Neighbor neighborFace) {
        switch (neighborFace) {
            case UP: return colors[0][1];
            case LEFT: return colors[1][0];
            case DOWN: return colors[2][1];
            case RIGHT: return colors[1][2];
        }
        throw new IllegalArgumentException("Ой, у нас более чем 3х мерное пространство!");
    }

    public boolean isSolved() { // TODO test me
        Elements prev = colors[0][0];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (prev != colors[x][y]) {
                    return false;
                }
                prev = colors[x][y];
            }
        }
        return true;
    }
}
