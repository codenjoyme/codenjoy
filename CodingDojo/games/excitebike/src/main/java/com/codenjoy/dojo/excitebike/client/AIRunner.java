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
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;

/**
 * Это класс, подключающий твоего бота к игровому серверу.
 * Просто запусти main метод, предварительно убедившись, что url для подключения корректен.
 * Логика твоего бота должна быть расположена в классе AISolver, в нем необходимо реализовать один метод - String get(Board board)
 */
public class AIRunner implements Solver<Board> {

    private AISolver aiSolver;

    public AIRunner(Dice dice) {
        aiSolver = new AISolver(dice);
    }

    public static void main(String[] args) {
        // paste here board page url from browser after registration
        String url = "http://localhost:8080/codenjoy-contest/board/player/pmy8dshjv0o9o1rrjv04?code=7685646871932912113";
        WebSocketRunner.runClient(
                url,
                new AIRunner(new RandomDice()),
                new Board());
    }

    @Override
    public String get(Board board) {
        String aiCommand = aiSolver.get(board);
        return aiCommand != null ? aiCommand : "";
    }

}
