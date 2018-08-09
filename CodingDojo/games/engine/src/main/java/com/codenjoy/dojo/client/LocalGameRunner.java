package com.codenjoy.dojo.client;

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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerCommand;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public class LocalGameRunner { // TODO test me

    public static int timeout = 1000;
    public static Consumer<String> out = System.out::println;
    public static Integer countIterations = null;

    public static void run(GameType gameType, Solver solver, ClientBoard board) {
        run(gameType, Arrays.asList(solver), Arrays.asList(board));
    }

    public static void run(GameType gameType,
                           List<Solver> solver,
                           List<ClientBoard> board)
    {
        GameField game = gameType.createGame();

        List<Single> singles = solver.stream()
                .map(slv -> createGame(gameType, game))
                .collect(toList());

        Integer count = countIterations;
        while (count == null || (count != null && count-- > 0)) {
            for (int index = 0; index < singles.size(); index++) {
                processNextTick(index, solver.get(index),
                        board.get(index),
                        singles.get(index));
            }

            game.tick();
            for (int index = 0; index < singles.size(); index++) {
                Game single = singles.get(index);
                if (single.isGameOver()) {
                    out.accept(player(index, "PLAYER_GAME_OVER -> START_NEW_GAME"));
                    single.newGame();
                }
            };

            out.accept("------------------------------------------");
        }
    }

    private static void processNextTick(int index, Solver solver, ClientBoard board, Single single) {
        Object data = single.getBoardAsString();
        board.forString(data.toString());

        out.accept(player(index, board.toString()));

        String answer = solver.get(board);

        out.accept(player(index, "Answer: " + answer));

        new PlayerCommand(single.getJoystick(), answer).execute();

        if (timeout > 0) {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Dice getDice(int... numbers) {
        int[] index = {0};
        return (n) -> {
            int next = numbers[index[0]];
            out.accept("DICE:" + next);
            if (next >= n) {
                next = next % n;
                out.accept("DICE_CORRECTED < " + n + " :" + next);
            }
            if (++index[0] == numbers.length) {
                index[0]--; // повторять последнее число если мы в конце массива
            }
            return next;
        };
    }

    private static String player(int index, String message) {
        String preffix = (index + 1) + ":";
        return preffix + message.replaceAll("\\n", "\n" + preffix);
    }

    private static Single createGame(GameType gameType, GameField field) {
        GamePlayer gamePlayer = gameType.createPlayer(
                event -> out.accept("Fire Event: " + event.toString()),
                null, null);
        PrinterFactory factory = gameType.getPrinterFactory();

        Single game = new Single(field, gamePlayer, factory);
        game.newGame();
        return game;
    }

}
