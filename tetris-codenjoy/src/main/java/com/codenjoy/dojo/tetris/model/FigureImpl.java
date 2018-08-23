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


import org.apache.commons.lang.StringUtils;

public class FigureImpl implements Figure, Cloneable {

    private int centerX;
    private int centerY;
    private Type type;
    public String[] rows = new String[]{"#"};
    private int[] codes;
    private int[] uncoloredCodes;

    @Deprecated
    public FigureImpl() {
        this(0, 0, "#");
    }

    @Deprecated
    public FigureImpl(int centerX, int centerY, String... rows) {
        this(centerX, centerY, Type.I, rows);
    }

    public FigureImpl(int centerX, int centerY, Type type, String... rows) {
        this.centerX = centerX;
        this.centerY = centerY;
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

    public int getLeft() {
        return centerX;
    }

    public int getRight() {
        return rows[0].length() - centerX - 1;
    }

    public int getTop() {
        return centerY;
    }

    public int getBottom() {
        return rows.length - centerY - 1;
    }

    @Override
    public int[] getRowCodes(boolean ignoreColors) {
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
        char newRows[][] = new char[getWidth()][rows.length];
        int newX = rows.length - centerY - 1;
        int newY = getLeft();
        for (int y = 0; y < rows.length; y++) {
            String row = rows[y];
            for (int x = 0; x < row.length(); x++) {
                char c = row.charAt(x);
                int shiftedX = x - centerX;
                int shiftedY = y - centerY;
                newRows[shiftedX + newY][- shiftedY + newX] = c;
            }
        }

        String[] rows = new String[newRows.length];
        for (int i = 0; i < newRows.length; i++) {
            rows[i] = String.copyValueOf(newRows[i]);
        }

        parseRows(rows);
        centerX = newX;
        centerY = newY;
    }

    @Override
    public int getWidth() {
        return getLeft() + getRight() + 1;
    }

    @Override
    public Figure getCopy() {
        try {
            return (Figure) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type getType() {
        return type;
    }
}
