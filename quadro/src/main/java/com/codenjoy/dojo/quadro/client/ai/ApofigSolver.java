package com.codenjoy.dojo.quadro.client.ai;

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


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.quadro.client.Board;
import com.codenjoy.dojo.quadro.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;

import java.util.ArrayList;

/**
 * Это алгоритм твоего бота. Он будет запускаться в игру с первым
 * зарегистрировавшимся игроком, чтобы ему не было скучно играть самому.
 * Реализуй его как хочешь, хоть на Random (только используй для этого
 * {@see Dice} что приходит через конструктор).
 * Для его запуска воспользуйся методом {@see ApofigSolver#main}
 */
public class ApofigSolver implements Solver<Board> {

    private Dice dice;

    public ApofigSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(final Board board) {
        return String.format("ACT(%s)", dice.next(board.size()));
    }

    public static void main(String[] args) {
        LocalGameRunner.run(new GameRunner(),
                new ArrayList<Solver>() {{
                    add(new ApofigSolver(new RandomDice()));
                    add(new ApofigSolver(new RandomDice()));
                }},
                new ArrayList<ClientBoard>() {{
                    add(new Board());
                    add(new Board());
                }});
        // TODO
//        start(WebSocketRunner.DEFAULT_USER, WebSocketRunner.Host.LOCAL, new RandomDice());
    }

    public static void start(String name, WebSocketRunner.Host host, Dice dice) {
        WebSocketRunner.run(host,
                name,
                null,
                new ApofigSolver(dice),
                new Board());
    }

}
