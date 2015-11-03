package com.codenjoy.dojo.a2048.client.ai;

import com.codenjoy.dojo.a2048.model.Elements;
import com.codenjoy.dojo.a2048.model.Numbers;
import com.codenjoy.dojo.client.Direction;

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
        numbers.move(convert(direction));
    }

    private com.codenjoy.dojo.services.Direction convert(Direction direction) {
        return com.codenjoy.dojo.services.Direction.valueOf(direction.value());
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
