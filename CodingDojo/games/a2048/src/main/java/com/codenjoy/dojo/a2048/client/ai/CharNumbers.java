package com.codenjoy.dojo.a2048.client.ai;

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


import com.codenjoy.dojo.a2048.model.Elements;
import com.codenjoy.dojo.a2048.model.Numbers;
import com.codenjoy.dojo.services.Direction;

public class CharNumbers {

    private Numbers numbers;

    public CharNumbers(char[][] charField) {
        int length = charField.length;
        int[][] intField = new int[length][length];

        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                intField[x][y] = value(charField[x][y]);
            }
        }

        numbers = new Numbers(intField);
    }

    private int value(char number) {
        if (number == ' ') {
            return 0;
        }
        Elements elements = Elements.valueOf(number);
        return elements.number();
    }

    private char valueChar(int number) {
        if (number == 0) {
            return ' ';
        }
        if (number == -1) {
            return 'x';
        }
        Elements elements = Elements.valueOf(number);
        return elements.ch();
    }

    public int freeSpace() {
        return numbers.freeSpace();
    }

    public int getSum() {
        return numbers.getSum();
    }

    public void move(Direction direction) {
        numbers.move(direction);
    }

    public void addNew() {
        int last = numbers.size() - 1;
        checkAndSet(0, 0);
        checkAndSet(0, last);
        checkAndSet(last, 0);
        checkAndSet(last, last);
    }

    private void checkAndSet(int x, int y) {
        if (!numbers.isBusy(x, y)) {
            numbers.add(x, y, 2);
        }
    }

    public char[][] get() {
        int length = numbers.size();
        char[][] charField = new char[length][length];

        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                charField[x][y] = valueChar(numbers.get(x, y).get());
            }
        }

        return charField;
    }
}
