package com.codenjoy.dojo.tetris.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import org.apache.commons.lang3.StringUtils;

public class FigureImpl implements Figure, Cloneable {

    private int x;
    private int y;
    private Type type;
    public String[] rows = new String[]{"#"};
    private int[] codes;
    private int[] uncoloredCodes;

    public FigureImpl() {
        this(0, 0, "#");
    }

    public FigureImpl(int x, int y, String... rows) {
        this(x, y, Type.I, rows);
    }

    public FigureImpl(int x, int y, Type type, String... rows) {
        this.x = x;
        this.y = y;
        this.type = type;
        parseRows(rows);
    }

    private void parseRows(String... rows) {
        this.rows = rows;
        codes = new int[rows.length];
        uncoloredCodes = new int[rows.length];
        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];
            String colorCode = Integer.toBinaryString(type.getColor().ordinal() + 1);
            String paddedCode = StringUtils.leftPad(colorCode, 3, '0');
            codes[i] = Integer.parseInt(row.replace("#", paddedCode).replace(" " , "000"), 2);
            uncoloredCodes[i] = Integer.parseInt(row.replace("#", "111").replace(" " , "000"), 2);
        }
    }

    public int left() {
        return x;
    }

    public int right() {
        return rows[0].length() - x - 1;
    }

    public int top() {
        return y;
    }

    public int bottom() {
        return rows.length - y - 1;
    }

    @Override
    public int[] rowCodes(boolean ignoreColors) {
        if (ignoreColors) {
            return uncoloredCodes;
        }
        return codes;
    }

    public Figure rotate(int times) {
        int realRotates = times % 4;
        for (int i = 0; i < realRotates; i++) {
            performRotate();
        }
        return this;
    }

    private void performRotate() {
        char[][] newRows = new char[width()][rows.length];
        int newX = rows.length - y - 1;
        int newY = left();
        for (int y = 0; y < rows.length; y++) {
            String row = rows[y];
            for (int x = 0; x < row.length(); x++) {
                char c = row.charAt(x);
                int shiftedX = x - this.x;
                int shiftedY = y - this.y;
                newRows[shiftedX + newY][- shiftedY + newX] = c;
            }
        }

        String[] rows = new String[newRows.length];
        for (int i = 0; i < newRows.length; i++) {
            rows[i] = String.copyValueOf(newRows[i]);
        }

        parseRows(rows);
        x = newX;
        y = newY;
    }

    @Override
    public int width() {
        return left() + right() + 1;
    }

    @Override
    public int height() {
        return top() + bottom() + 1;
    }

    @Override
    public Figure copy() {
        try {
            return (Figure) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("'%s' WH(%s:%s) LR(%s,%s) TB(%s,%s)",
                type.getColor().ch,
                width(), height(),
                left(), right(),
                top(), bottom());
    }
}
