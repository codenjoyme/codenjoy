package com.codenjoy.dojo.football.model;

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

public enum Elements implements CharElements {

    NONE(' '),        // это пустое место, куда можно перейти герою
    WALL('☼'),        // а это внешняя разметка поля, через которую я хочу чтобы проходить нельзя было
    HERO('☺'),        // а это мой герой
    HERO_W_BALL('☻'), // герой с мячом
    BALL('*'),        // а это мяч
    STOPPED_BALL('∙'),// а это маленький мяч (остановленный)
    TOP_GOAL('┴'),    // верхние ворота
    BOTTOM_GOAL('┬'), // нижние ворота
    MY_GOAL('='),     // мои ворота
    ENEMY_GOAL('⌂'),  // чужие ворота
    HITED_GOAL('x'),  // гол в ворота
    HITED_MY_GOAL('#'), // гол в мои ворота
    TEAM_MEMBER('♦'),         // член моей команды
    TEAM_MEMBER_W_BALL('♥'), // член моей команды с мячем
    ENEMY('♣'),              // член второй команды
    ENEMY_W_BALL('♠');       // член второй команды с мячем
    
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
