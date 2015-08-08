package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.CharElements;

/**
 * Тут указана легенда всех возможных объектов на поле и их состояний.
 * Важно помнить, что для каждой енумной константы надо создать спрайт в папке \src\main\webapp\resources\sprite.
 */
public enum Elements implements CharElements {

    NONE(' '),       // например это пустое место, куда можно перейти герою
    EXPLOSION('x'),     // взрыв
    WALL('☼'),       // а это стенка, через которую я хочу чтобы проходить нельзя было
    HERO('☺'),       // а это мой герой
    OTHER_HERO('☻'), // это герои других игроков
    DEAD_HERO('+'),  // а это временное явление - трупик моего героя, которое пропадет в следующем такте
    GOLD('$'),       // это то, за чем будет охота
    BOMB('♣'),       // а это бомба, на которой можно подорваться
    STONE('0'),      // а это камень
    BULLET('*');     // а это пуля

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
