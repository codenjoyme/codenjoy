package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.CharElements;

/**
 * User: sanja
 * Date: 06.06.13
 * Time: 17:38
 */
public enum Elements implements CharElements {

    NONE(' '),

    BRICK('#'),
    PIT_FILL_1('1'),
    PIT_FILL_2('2'),
    PIT_FILL_3('3'),
    PIT_FILL_4('4'),
    UNDESTROYABLE_WALL('☼'),

    DRILL_PIT('*'),

    ENEMY_LADDER('Q'),
    ENEMY_LEFT('«'),
    ENEMY_RIGHT('»'),
    ENEMY_PIPE_LEFT('<'),
    ENEMY_PIPE_RIGHT('>'),
    ENEMY_PIT('X'),

    GOLD('$'),

    HERO_DIE('Ѡ'),
    HERO_DRILL_LEFT('Я'),
    HERO_DRILL_RIGHT('R'),
    HERO_LADDER('Y'),
    HERO_LEFT('◄'),
    HERO_RIGHT('►'),
    HERO_FALL_LEFT(']'),
    HERO_FALL_RIGHT('['),
    HERO_PIPE_LEFT('{'),
    HERO_PIPE_RIGHT('}'),

    OTHER_HERO_DIE('Z'),
    OTHER_HERO_LEFT(')'),
    OTHER_HERO_RIGHT('('),
    OTHER_HERO_LADDER('U'),
    OTHER_HERO_PIPE_LEFT('Э'),
    OTHER_HERO_PIPE_RIGHT('Є'),

    LADDER('H'),
    PIPE('~');

    final char ch;

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

    public static Elements forOtherHero(Elements hero) {
        switch (hero) {
            case HERO_DIE : return OTHER_HERO_DIE;
            case HERO_DRILL_LEFT : return OTHER_HERO_LEFT;
            case HERO_DRILL_RIGHT : return OTHER_HERO_RIGHT;
            case HERO_LADDER : return OTHER_HERO_LADDER;
            case HERO_LEFT : return OTHER_HERO_LEFT;
            case HERO_RIGHT : return OTHER_HERO_RIGHT;
            case HERO_FALL_LEFT : return OTHER_HERO_LEFT;
            case HERO_FALL_RIGHT : return OTHER_HERO_RIGHT;
            case HERO_PIPE_LEFT : return OTHER_HERO_PIPE_LEFT;
            case HERO_PIPE_RIGHT : return OTHER_HERO_PIPE_RIGHT;
        }
        throw new IllegalArgumentException("Bad hero state: " + hero);
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

}
