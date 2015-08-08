package com.codenjoy.dojo.startandjump.model;

import com.codenjoy.dojo.services.CharElements;

/**
 * Тут указана легенда всех возможных объектов на поле и их состояний.
 * Важно помнить, что для каждой енумной константы надо создать спрайт в папке \src\main\webapp\resources\sprite.
 */
public enum Elements implements CharElements {

    NONE(' '),       // пустое поле
    WALL('#'),       // а это стенка, через которую я хочу чтобы проходить нельзя было
    PLATFORM('='),   // а это МОЯ ПЛАТФОРМА
    HERO('☺'),       // а это мой герой
    BLACK_HERO('☻');       // а это очень мертвый труп


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
