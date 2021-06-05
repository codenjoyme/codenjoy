package com.codenjoy.dojo.snakebattle.client;

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

/**
 * Тут указана легенда всех возможных объектов на поле и их состояний.
 * Важно помнить, что для каждой енумной константы надо создать спрайт в папке \src\main\webapp\resources\sprite.
 */
public enum Element implements CharElements {

    NONE(' '),         // пустое место
    WALL('☼'),         // а это стенка
    START_FLOOR('#'),  // место старта змей
    OTHER('?'),        // этого ты никогда не увидишь :)

    APPLE('○'),        // яблоки надо кушать от них становишься длинее
    STONE('●'),        // а это кушать не стоит - от этого укорачиваешься
    FLYING_PILL('©'),  // таблетка полета - дает суперсилы
    FURY_PILL('®'),    // таблетка ярости - дает суперсилы
    GOLD('$'),         // золото - просто очки

    // голова твоей змеи в разных состояниях и напрвлениях
    HEAD_DOWN('▼'),
    HEAD_LEFT('◄'),
    HEAD_RIGHT('►'),
    HEAD_UP('▲'),
    HEAD_DEAD('☻'),    // этот раунд ты проиграл
    HEAD_EVIL('♥'),    // ты скушал таблетку ярости
    HEAD_FLY('♠'),     // ты скушал таблетку полета
    HEAD_SLEEP('&'),   // твоя змейка ожидает начала раунда

    // хвост твоей змейки
    TAIL_END_DOWN('╙'),
    TAIL_END_LEFT('╘'),
    TAIL_END_UP('╓'),
    TAIL_END_RIGHT('╕'),
    TAIL_INACTIVE('~'),

    // туловище твоей змейки
    BODY_HORIZONTAL('═'),
    BODY_VERTICAL('║'),
    BODY_LEFT_DOWN('╗'),
    BODY_LEFT_UP('╝'),
    BODY_RIGHT_DOWN('╔'),
    BODY_RIGHT_UP('╚'),

    // змейки противников
    ENEMY_HEAD_DOWN('˅'),
    ENEMY_HEAD_LEFT('<'),
    ENEMY_HEAD_RIGHT('>'),
    ENEMY_HEAD_UP('˄'),
    ENEMY_HEAD_DEAD('☺'),   // этот раунд противник проиграл
    ENEMY_HEAD_EVIL('♣'),   // противник скушал таблетку ярости
    ENEMY_HEAD_FLY('♦'),    // противник скушал таблетку полета
    ENEMY_HEAD_SLEEP('ø'),  // змейка противника ожидает начала раунда

    // хвосты змеек противников
    ENEMY_TAIL_END_DOWN('¤'),
    ENEMY_TAIL_END_LEFT('×'),
    ENEMY_TAIL_END_UP('æ'),
    ENEMY_TAIL_END_RIGHT('ö'),
    ENEMY_TAIL_INACTIVE('*'),

    // туловище змеек противников
    ENEMY_BODY_HORIZONTAL('─'),
    ENEMY_BODY_VERTICAL('│'),
    ENEMY_BODY_LEFT_DOWN('┐'),
    ENEMY_BODY_LEFT_UP('┘'),
    ENEMY_BODY_RIGHT_DOWN('┌'),
    ENEMY_BODY_RIGHT_UP('└');

    final char ch;

    Element(char ch) {
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

    public static Element valueOf(char ch) {
        for (Element el : Element.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }

}
