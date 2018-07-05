package com.codenjoy.dojo.client;

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


import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerCommand;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.Single;

import java.util.function.Consumer;

public class LocalGameRunner { // TODO test me

    public static int timeout = 1000;
    public static Consumer<String> out = System.out::println;
    public static Integer countIterations = null;

    public static void run(GameType gameType, Solver solver, ClientBoard board) {
        GameField field = gameType.createGame();
        GamePlayer gamePlayer = gameType.createPlayer(
                event -> out.accept("Fire Event: " + event.toString()),
                null, null);
        PrinterFactory printerFactory = gameType.getPrinterFactory();

        Game game = new Single(field, gamePlayer, printerFactory);
        game.newGame();

        Integer count = countIterations;
        while (count == null || (count != null && count-- > 0)) {
            Object data = game.getBoardAsString();
            board.forString(data.toString());

            out.accept(board.toString());

            String answer = solver.get(board);

            out.accept("Answer: " + answer);

            new PlayerCommand(game.getJoystick(), answer).execute();

            if (timeout > 0) {
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            game.tick();
            if (game.isGameOver()) {
                game.newGame();
            }
            out.accept("------------------------------------------------------------------------------------");
        }
    }

}
