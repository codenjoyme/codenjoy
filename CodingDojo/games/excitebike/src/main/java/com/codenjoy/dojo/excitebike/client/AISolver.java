package com.codenjoy.dojo.excitebike.client;

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


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.excitebike.client.Board;
import com.codenjoy.dojo.services.Dice;

/**
 * Это алгоритм твоего бота.
 * Метод get должен возвращать строковое представление объекта класса Direction (либо ничего).
 * ExciteBike поддерживает ровно две команды - Direction.UP и Direction.DOWN
 * Реализуй логику своего бота как хочешь, хоть на Random (только используй для этого
 * {@see Dice} что приходит через конструктор).
 * Для запуска воспользуйся методом {@see AIRunner#main}
 */
public class AISolver implements Solver<Board> {

    private Dice dice;

    public AISolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(final Board board) {
        // return Direction.UP.toString();
        // return Direction.DOWN.toString();
        return null;
    }

}
