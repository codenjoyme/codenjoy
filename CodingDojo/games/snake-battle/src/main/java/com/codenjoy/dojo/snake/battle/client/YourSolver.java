package com.codenjoy.dojo.snake.battle.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.client.WebSocketRunner.Host;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.snake.battle.services.GameRunner;

import static com.codenjoy.dojo.client.Direction.RIGHT;

/**
 * User: your name
 * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
 * Обрати внимание на {@see YourSolverTest} - там приготовлен тестовый
 * фреймворк для тебя.
 */
public class YourSolver implements Solver<Board> {

    private static final String USER_NAME = "user@mail.ru"; // TODO вписать свой ник (с которым регистрировался)

    private Dice dice;
    private Board board;

    YourSolver(Dice dice) {
        this.dice = dice;
    }

    // Необходимо изменить данный метод. Он должен возвращать осмысленное направление для дальнейшего движения змейки.
    @Override
    public String get(Board board) {
        this.board = board;
        if (board.isGameOver()) return "";

        return Direction.RIGHT.toString();
//      return Direction.random().toString();
    }

    public static void main(String[] args) {
        start(USER_NAME, getKorsHost());
    }

    private static Host getKorsHost() {
        Host h = Host.REMOTE;
        h.host = "5.19.187.136:8080";
        h.uri = "ws://" + h.host + "/codenjoy-contest/ws";
        return h;
    }

    static void start(String name, Host server) {
        try {
            WebSocketRunner.run(server, name,
                    new YourSolver(new RandomDice()),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
