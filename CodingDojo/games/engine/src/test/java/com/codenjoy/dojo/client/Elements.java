package com.codenjoy.dojo.client;

import com.codenjoy.dojo.services.CharElements;

/**
 * Created by indigo on 2016-09-22.
 */
enum Elements implements CharElements {

    ONE('1'), TWO('2'), THREE('3');

    final char ch;

    Elements(char ch) {
        this.ch = ch;
    }

    @Override
    public char ch() {
        return ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
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
