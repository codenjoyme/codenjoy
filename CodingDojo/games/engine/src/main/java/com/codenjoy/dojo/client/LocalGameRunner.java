package com.codenjoy.dojo.client;

import com.codenjoy.dojo.services.*;

public class LocalGameRunner {

    public static void run(GameType gameType, Solver solver, AbstractBoard board) {
        Game game = gameType.newGame(new EventListener() {
            @Override
            public void event(Object event) {
                System.out.println("Fire Event: " + event.toString());
            }
        }, new PrinterFactoryImpl());

        game.newGame();
        while (true) {
            String data = game.getBoardAsString();
            board.forString(data);

            System.out.print(board.toString());

            String answer = solver.get(board);

            System.out.println("Answer: " + answer);

            new PlayerCommand(game.getJoystick(), answer).execute();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            game.tick();
            if (game.isGameOver()) {
                game.newGame();
            }
            System.out.println("----------------------------");
        }
    }

}
