package com.codenjoy.dojo.snake.services;

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

// Запоминается только максимальное количество очков во всех раундах,
// рассчитываемое из текущей длинны змеи. Поедание камней и суицид не уменьшает количество очков.
// Полезно в онлайн турнире, когда сложно собрать всех воедино для финала.
// Включается опция на админке путем установки настройки игры "Max score mode" в true
public class MaxScores extends Scores {

    public MaxScores(int startScore, SnakeSettings setup) {
        super(startScore, setup);
    }

    @Override
    protected void snakeIsDead() {
        initLength();
    }

    @Override
    protected void snakeEatApple() {
        length++;

        int current = 0;
        for (int i = setup.startSnakeLength().getValue() + 1; i <= length; i++) {
            current += i;
        }

        score = Math.max(score, current);
    }

    @Override
    protected void snakeEatStone() {
        length -= setup.eatStoneDecrease().getValue();
    }

}
