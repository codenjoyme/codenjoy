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


import com.codenjoy.dojo.services.*;

public class LocalGameRunner {

    public static int TIMEOUT = 1000;

    public static void run(GameType gameType, Solver solver, ClientBoard board) {
        Game game = gameType.newGame(new EventListener() {
            @Override
            public void event(Object event) {
                System.out.println("Fire Event: " + event.toString());
            }
        }, new PrinterFactoryImpl(), null);

        game.newGame();
        while (true) {
            Object data = game.getBoardAsString();
            board.forString(data.toString());

            System.out.println(board.toString());

            String answer = solver.get(board);

            System.out.println("Answer: " + answer);

            new PlayerCommand(game.getJoystick(), answer).execute();

            try {
                Thread.sleep(TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            game.tick();
            if (game.isGameOver()) {
                game.newGame();
            }
            System.out.println("------------------------------------------------------------------------------------");
        }
    }

}
