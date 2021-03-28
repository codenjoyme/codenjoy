package com.codenjoy.dojo.loderunner.model;

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


import com.codenjoy.dojo.services.printer.CharElements;

import java.util.Arrays;
import java.util.List;

public enum Elements implements CharElements {

    NONE(' '),                    // Пустое место – по которому может двигаться герой

    BRICK('#'),                   // Cтена в которой можно просверлить дырочку слева или справа от героя
                                  // (в зависимости от того, куда он сейчас смотрит)

    PIT_FILL_1('1'),              // Стена со временем зарастает. Когда процес начинается - мы видим таймер
    PIT_FILL_2('2'),
    PIT_FILL_3('3'),
    PIT_FILL_4('4'),

    UNDESTROYABLE_WALL('☼'),      // Неразрушаемая стена - в ней ничего просверлить не получится

    DRILL_PIT('*'),               // В момент сверления мы видим процесс так

    YELLOW_GOLD('$'),             // Горстка золота жёлтого цвета
    GREEN_GOLD('&'),              // Зелёная горстка золота
    RED_GOLD('@'),                // Золото красного цвета

    // Твой герой в зависимости от того, чем он сейчас занят отображается следующими символами
    HERO_DIE('Ѡ'),                // Герой переживает процесс умирания
    HERO_DRILL_LEFT('Я'),         // Герой сверлит слева от себя
    HERO_DRILL_RIGHT('R'),        // Герой сверлит справа от себя
    HERO_LADDER('Y'),             // Герой находится на лестнице
    HERO_LEFT('◄'),               // Герой бежит влево
    HERO_RIGHT('►'),              // Герой бежит вправо
    HERO_FALL_LEFT(']'),          // Герой падает, смотря влево
    HERO_FALL_RIGHT('['),         // Герой падает, смотря вправо
    HERO_PIPE_LEFT('{'),          // Герой ползёт по трубе влево
    HERO_PIPE_RIGHT('}'),         // Герой ползёт по трубе вправо

    // Тоже твой герой, но под таблеткой тени:
    HERO_SHADOW_DIE('x'),         // Герой-тень переживает процесс умирания // TODO test me
    HERO_SHADOW_DRILL_LEFT('⊰'),  // Герой-тень сверлит слева от себя
    HERO_SHADOW_DRILL_RIGHT('⊱'), // Герой-тень сверлит справа от себя
    HERO_SHADOW_LADDER('⍬'),      // Герой-тень находится на лестнице
    HERO_SHADOW_LEFT('⊲'),        // Герой-тень бежит влево
    HERO_SHADOW_RIGHT('⊳'),       // Герой-тень бежит вправо
    HERO_SHADOW_FALL_LEFT('⊅'),   // Герой-тень падает, смотря влево
    HERO_SHADOW_FALL_RIGHT('⊄'),  // Герой-тень падает, смотря вправо
    HERO_SHADOW_PIPE_LEFT('⋜'),   // Герой-тень ползёт по трубе влево
    HERO_SHADOW_PIPE_RIGHT('⋝'),  // Герой-тень ползёт по трубе вправо

    // Герои других игроков отображаются так
    OTHER_HERO_DIE('Z'),          // Герой переживает процесс умирания
    OTHER_HERO_DRILL_LEFT('⌋'),   // Герой сверлит слева от себя       // TODO test me
    OTHER_HERO_DRILL_RIGHT('⌊'),  // Герой сверлит справа от себя      // TODO test me
    OTHER_HERO_LADDER('U'),       // Герой находится на лестнице
    OTHER_HERO_LEFT(')'),         // Герой бежит влево
    OTHER_HERO_RIGHT('('),        // Герой бежит вправо
    OTHER_HERO_FALL_LEFT('⊐'),    // Герой падает, смотря влево        // TODO test me
    OTHER_HERO_FALL_RIGHT('⊏'),   // Герой падает, смотря вправо       // TODO test me
    OTHER_HERO_PIPE_LEFT('Э'),    // Герой ползёт по трубе влево
    OTHER_HERO_PIPE_RIGHT('Є'),   // Герой ползёт по трубе вправо

    // А если герои других игроков под таблеткой тени, то так
    OTHER_HERO_SHADOW_DIE('⋈'),         // Другой герой-тень переживает процесс умирания
    OTHER_HERO_SHADOW_DRILL_LEFT('⋰'),  // Другой герой-тень сверлит слева от себя       // TODO test me
    OTHER_HERO_SHADOW_DRILL_RIGHT('⋱'), // Другой герой-тень сверлит справа от себя      // TODO test me
    OTHER_HERO_SHADOW_LEFT('⋊'),        // Другой герой-тень находится на лестнице
    OTHER_HERO_SHADOW_RIGHT('⋉'),       // Другой герой-тень бежит влево
    OTHER_HERO_SHADOW_LADDER('⋕'),      // Другой герой-тень бежит вправо
    OTHER_HERO_SHADOW_FALL_LEFT('⋣'),   // Другой герой-тень падает, смотря влево        // TODO test me
    OTHER_HERO_SHADOW_FALL_RIGHT('⋢'),  // Другой герой-тень падает, смотря вправо       // TODO test me
    OTHER_HERO_SHADOW_PIPE_LEFT('⊣'),   // Другой герой-тень ползёт по трубе влево
    OTHER_HERO_SHADOW_PIPE_RIGHT('⊢'),  // Другой герой-тень ползёт по трубе вправо

    // Боты-охотники
    ENEMY_LADDER('Q'),
    ENEMY_LEFT('«'),
    ENEMY_RIGHT('»'),
    ENEMY_PIPE_LEFT('<'),
    ENEMY_PIPE_RIGHT('>'),
    ENEMY_PIT('X'),

    LADDER('H'),              // Лестница - по ней можно перемещаться по уровню
    PIPE('~'),                // Труба - по ней так же можно перемещаться по уровню, но только горизонтально

    PORTAL('⊛'),              // Портал - позволяет телепортироваться на карте в иное место на карте

    SHADOW_PILL('S');         // Таблетка тени - наделяют героя дополнительными способностями

    final char ch;

    public static List<Elements> gold() {
        return Arrays.asList(
                YELLOW_GOLD,
                GREEN_GOLD,
                RED_GOLD);
    }

    public static List<Elements> ladders() {
        return Arrays.asList(LADDER,
                HERO_LADDER,
                HERO_SHADOW_LADDER,
                OTHER_HERO_LADDER,
                OTHER_HERO_SHADOW_LADDER,
                ENEMY_LADDER);
    }

    public static List<Elements> walls() {
        return Arrays.asList(BRICK,
                UNDESTROYABLE_WALL);
    }

    public static List<Elements> heroes() {
        return Arrays.asList(HERO_DIE,
                HERO_DRILL_LEFT,
                HERO_DRILL_RIGHT,
                HERO_LADDER,
                HERO_LEFT,
                HERO_RIGHT,
                HERO_FALL_LEFT,
                HERO_FALL_RIGHT,
                HERO_PIPE_LEFT,
                HERO_PIPE_RIGHT,

                HERO_SHADOW_DIE,
                HERO_SHADOW_DRILL_LEFT,
                HERO_SHADOW_DRILL_RIGHT,
                HERO_SHADOW_LADDER,
                HERO_SHADOW_LEFT,
                HERO_SHADOW_RIGHT,
                HERO_SHADOW_FALL_LEFT,
                HERO_SHADOW_FALL_RIGHT,
                HERO_SHADOW_PIPE_LEFT,
                HERO_SHADOW_PIPE_RIGHT);
    }

    public static List<Elements> enemies() {
        return Arrays.asList(ENEMY_LADDER,
                ENEMY_LEFT,
                ENEMY_PIPE_LEFT,
                ENEMY_PIPE_RIGHT,
                ENEMY_RIGHT,
                ENEMY_PIT);
    }

    public static List<Elements> otherHeroes() {
        return Arrays.asList(OTHER_HERO_DIE,
                OTHER_HERO_DRILL_LEFT,
                OTHER_HERO_DRILL_RIGHT,
                OTHER_HERO_LADDER,
                OTHER_HERO_LEFT,
                OTHER_HERO_RIGHT,
                OTHER_HERO_FALL_LEFT,
                OTHER_HERO_FALL_RIGHT,
                OTHER_HERO_PIPE_LEFT,
                OTHER_HERO_PIPE_RIGHT,

                OTHER_HERO_SHADOW_DIE,
                OTHER_HERO_SHADOW_DRILL_LEFT,
                OTHER_HERO_SHADOW_DRILL_RIGHT,
                OTHER_HERO_SHADOW_LEFT,
                OTHER_HERO_SHADOW_RIGHT,
                OTHER_HERO_SHADOW_LADDER,
                OTHER_HERO_SHADOW_FALL_LEFT,
                OTHER_HERO_SHADOW_FALL_RIGHT,
                OTHER_HERO_SHADOW_PIPE_LEFT,
                OTHER_HERO_SHADOW_PIPE_RIGHT);
    }

    public static List<Elements> pipes() {
        return Arrays.asList(PIPE,
                HERO_PIPE_LEFT,
                HERO_PIPE_RIGHT,
                HERO_SHADOW_PIPE_LEFT,
                HERO_SHADOW_PIPE_RIGHT,
                OTHER_HERO_PIPE_LEFT,
                OTHER_HERO_PIPE_RIGHT,
                OTHER_HERO_SHADOW_PIPE_LEFT,
                OTHER_HERO_SHADOW_PIPE_RIGHT);
    }

    @Override
    public char ch() {
        return ch;
    }

    public char getChar() {
        return ch;
    }

    Elements(char ch) {
        this.ch = ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public Elements shadow() {
        switch (this) {
            case HERO_DIE: return HERO_SHADOW_DIE;
            case HERO_DRILL_LEFT: return HERO_SHADOW_DRILL_LEFT;
            case HERO_DRILL_RIGHT: return HERO_SHADOW_DRILL_RIGHT;
            case HERO_LADDER: return HERO_SHADOW_LADDER;
            case HERO_LEFT: return HERO_SHADOW_LEFT;
            case HERO_RIGHT: return HERO_SHADOW_RIGHT;
            case HERO_FALL_LEFT: return HERO_SHADOW_FALL_LEFT;
            case HERO_FALL_RIGHT: return HERO_SHADOW_FALL_RIGHT;
            case HERO_PIPE_LEFT: return HERO_SHADOW_PIPE_LEFT;
            case HERO_PIPE_RIGHT: return HERO_SHADOW_PIPE_RIGHT;

            case OTHER_HERO_DIE: return OTHER_HERO_SHADOW_DIE;
            case OTHER_HERO_DRILL_LEFT: return OTHER_HERO_SHADOW_DRILL_LEFT;
            case OTHER_HERO_DRILL_RIGHT: return OTHER_HERO_SHADOW_DRILL_RIGHT;
            case OTHER_HERO_LADDER: return OTHER_HERO_SHADOW_LADDER;
            case OTHER_HERO_LEFT: return OTHER_HERO_SHADOW_LEFT;
            case OTHER_HERO_RIGHT: return OTHER_HERO_SHADOW_RIGHT;
            case OTHER_HERO_FALL_LEFT: return OTHER_HERO_SHADOW_FALL_LEFT;
            case OTHER_HERO_FALL_RIGHT: return OTHER_HERO_SHADOW_FALL_RIGHT;
            case OTHER_HERO_PIPE_LEFT: return OTHER_HERO_SHADOW_PIPE_LEFT;
            case OTHER_HERO_PIPE_RIGHT: return OTHER_HERO_SHADOW_PIPE_RIGHT;
        }
        throw new IllegalArgumentException("Bad hero state: " + this);
    }

    public Elements otherHero() {
        switch (this) {
            case HERO_DIE: return OTHER_HERO_DIE;
            case HERO_DRILL_LEFT: return OTHER_HERO_DRILL_LEFT;
            case HERO_DRILL_RIGHT: return OTHER_HERO_DRILL_RIGHT;
            case HERO_LADDER: return OTHER_HERO_LADDER;
            case HERO_LEFT: return OTHER_HERO_LEFT;
            case HERO_RIGHT: return OTHER_HERO_RIGHT;
            case HERO_FALL_LEFT: return OTHER_HERO_FALL_LEFT;
            case HERO_FALL_RIGHT: return OTHER_HERO_FALL_RIGHT;
            case HERO_PIPE_LEFT: return OTHER_HERO_PIPE_LEFT;
            case HERO_PIPE_RIGHT: return OTHER_HERO_PIPE_RIGHT;

            case HERO_SHADOW_DIE: return OTHER_HERO_SHADOW_DIE;
            case HERO_SHADOW_DRILL_LEFT: return OTHER_HERO_SHADOW_DRILL_LEFT;
            case HERO_SHADOW_DRILL_RIGHT: return OTHER_HERO_SHADOW_DRILL_RIGHT;
            case HERO_SHADOW_LADDER: return OTHER_HERO_SHADOW_LADDER;
            case HERO_SHADOW_LEFT: return OTHER_HERO_SHADOW_LEFT;
            case HERO_SHADOW_RIGHT: return OTHER_HERO_SHADOW_RIGHT;
            case HERO_SHADOW_FALL_LEFT: return OTHER_HERO_SHADOW_FALL_LEFT;
            case HERO_SHADOW_FALL_RIGHT: return OTHER_HERO_SHADOW_FALL_RIGHT;
            case HERO_SHADOW_PIPE_LEFT: return OTHER_HERO_SHADOW_PIPE_LEFT;
            case HERO_SHADOW_PIPE_RIGHT: return OTHER_HERO_SHADOW_PIPE_RIGHT;
        }
        throw new IllegalArgumentException("Bad hero state: " + this);
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
