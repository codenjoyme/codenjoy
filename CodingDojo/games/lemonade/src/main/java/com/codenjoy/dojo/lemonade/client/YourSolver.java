package com.codenjoy.dojo.lemonade.client;

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


import com.codenjoy.dojo.client.AbstractJsonSolver;
import com.codenjoy.dojo.client.AbstractTextSolver;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;
import org.json.JSONObject;

/**
 * User: your name
 * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
 * Обрати внимание на {@see YourSolverTest} - там приготовлен тестовый
 * фреймворк для тебя.
 */
public class YourSolver implements Solver<Board> {

    private Dice dice;
    private YourSolver board;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                "http://localhost:8080/codenjoy-contest/board/player/nobm5352w8ho65xnx968?code=1279539930846793759",
                new YourSolver(new RandomDice()),
                new Board());
    }

    @Override
    public String get(Board board) {
        if (board.isGameOver())
            return resetCommand();
        System.out.println(board.getMessages());
        String a = toAnswerString(1, 1, 1);
        return a;
    }

    private String toAnswerString(int lemonadeToMake, int signsToMake, int lemonadePriceCents) {
        return String.format("message('go %d,%d,%d')", lemonadeToMake, signsToMake, lemonadePriceCents);
    }

    private String resetCommand() {
        return "message('go reset')";
    }
}
