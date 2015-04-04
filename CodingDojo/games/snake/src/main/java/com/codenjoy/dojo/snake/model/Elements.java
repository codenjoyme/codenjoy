package com.codenjoy.dojo.snake.model;

import com.codenjoy.dojo.services.CharElements;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:57 AM
 */
public enum Elements implements CharElements {
    BAD_APPLE('☻'),
    GOOD_APPLE('☺'),

    BREAK('☼'),

    HEAD_DOWN('▼'),
    HEAD_LEFT('◄'),
    HEAD_RIGHT('►'),
    HEAD_UP('▲'),

    TAIL_END_DOWN('╙'),
    TAIL_END_LEFT('╘'),
    TAIL_END_UP('╓'),
    TAIL_END_RIGHT('╕'),
    TAIL_HORIZONTAL('═'),
    TAIL_VERTICAL('║'),
    TAIL_LEFT_DOWN('╗'),
    TAIL_LEFT_UP('╝'),
    TAIL_RIGHT_DOWN('╔'),
    TAIL_RIGHT_UP('╚'),

    NONE(' ');

    final char ch;

    Elements(char ch) {
        this.ch = ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    @Override
    public char ch() {
        return ch;
    }

    public static Elements valueOf(char ch) {
        for (Elements el : Elements.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }
}
