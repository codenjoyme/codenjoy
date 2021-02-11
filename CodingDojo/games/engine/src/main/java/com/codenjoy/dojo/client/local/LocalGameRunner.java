package com.codenjoy.dojo.client.local;

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


import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class LocalGameRunner {

    public static final String SEP = "------------------------------------------";

    public static int timeout = 10;
    public static boolean printScores = true;
    public static boolean printWelcome = false;
    public static boolean printBoardOnly = false;
    public static Consumer<String> out = System.out::println;
    public static Integer countIterations = null;
    public static boolean printConversions = true;
    public static boolean printDice = true;
    public static boolean printTick = false;
    public static String showPlayers = null;
    public static boolean exit = false;
    public static int waitForPlayers = 1;

    private GameField field;
    private List<Game> games;
    private GameType gameType;
    private List<Solver> solvers;
    private List<ClientBoard> boards;
    private List<PlayerScores> scores;
    private Integer tick;

    {
        if (printWelcome) {
            out.accept(VersionReader.getWelcomeMessage());
        }
    }

    public static LocalGameRunner run(GameType gameType, Solver solver, ClientBoard board) {
        return run(gameType, Arrays.asList(solver), Arrays.asList(board));
    }

    public static LocalGameRunner run(GameType gameType,
                           List<Solver> solvers,
                           List<ClientBoard> boards)
    {
        LocalGameRunner runner = new LocalGameRunner(gameType);

        for (int i = 0; i < solvers.size(); i++) {
            runner.add(solvers.get(i), boards.get(i));
        }

        return runner.run(tick -> {});
    }

    public LocalGameRunner(GameType gameType) {
        this.gameType = gameType;

        solvers = new LinkedList<>();
        boards = new LinkedList<>();
        games = new LinkedList<>();
        scores = new LinkedList<>();

        field = gameType.createGame(LevelProgress.levelsStartsFrom1);
    }

    public LocalGameRunner run(Consumer<Integer> onTick) {
        tick = 0;
        while (!exit && (countIterations == null || this.tick++ < countIterations)) {
            try {
                if (timeout > 0) {
                    try {
                        Thread.sleep(timeout);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (games.size() < waitForPlayers) {
                    tick = 0;
                    continue;
                }

                synchronized (this) {
                    List<String> answers = new LinkedList<>();
                    for (Game game : games) {
                        answers.add(askAnswer(games.indexOf(game)));
                    }

                    for (Game game : games) {
                        int index = games.indexOf(game);
                        String answer = answers.get(index);

                        if (answer != null) {
                            new PlayerCommand(game.getJoystick(), answer).execute();
                        }
                    }

                    for (int index = 0; index < games.size(); index++) {
                        Game single = games.get(index);
                        if (single.isGameOver()) {
                            print(index, "PLAYER_GAME_OVER -> START_NEW_GAME");
                            single.newGame();
                        }
                    }

                    field.tick();

                    out.accept(SEP);
                }

                if (onTick != null) {
                    onTick.accept(tick);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    private String askAnswer(int index) {
        ClientBoard board = board(index);

        Object data = game(index).getBoardAsString();
        board.forString(data.toString());

        if (printBoardOnly) {
            print(index, ((AbstractBoard) board).boardAsString());
        } else {
            print(index, board.toString());
        }

        String answer = solver(index).get(board);

        if (printScores) {
            print(index, "Scores: " + scores.get(index).getScore());
        }

        print(index, "Answer: " + answer);
        return answer;
    }

    private void print(int index, String message) {
        if (StringUtils.isEmpty(showPlayers)
                || Arrays.asList(showPlayers.split(","))
                        .contains(String.valueOf(index + 1)))
        {
            out.accept(player(index, message));
        }
    }

    private Solver solver(int index) {
        return solvers.get(index);
    }

    private ClientBoard board(int index) {
        return boards.get(index);
    }

    public synchronized void add(Solver solver, ClientBoard board) {
        solvers.add(solver);
        boards.add(board);
        games.add(createGame());
    }

    public synchronized void remove(Solver solver) {
        int index = solvers.indexOf(solver);
        solvers.remove(index);
        boards.remove(index);
        scores.remove(index);
        Game game = games.remove(index);
        field.remove(game.getPlayer());
    }

    private Game game(int index) {
        return games.get(index);
    }

    public static Dice getDice(int... numbers) {
        int[] index = {0};
        return (n) -> {
            int next = numbers[index[0]];
            if (printDice) {
                out.accept("DICE:" + next);
            }
            if (next >= n) {
                next = next % n;
                if (printConversions) {
                    out.accept("DICE_CORRECTED < " + n + " :" + next);
                }
            }
            if (++index[0] == numbers.length) {
                index[0] = 0; // начинать с начала, если мы дошли до конца
            }
            return next;
        };
    }

    public static int[] generateXorShift(String soul, long max, long count) {
        long[] current = new long[] { soul.hashCode() };
        System.out.println("Soul = " + soul);
        int[] result = IntStream.generate(() -> {
            long a0 = current[0] % soul.length();
            int a1 = soul.charAt((int)Math.abs(a0));
            long a2 = (current[0] << (a1 % 5)) ^ current[0];
            long a3 = (current[0] >>> (a1 % 6)) ^ (current[0] << (a1 % 2));
            current[0] = a2 ^ a3;
            return (int) Math.abs(current[0] % max);
        }).limit(count).toArray();
        return result;
    }

    private String player(int index, String message) {
        String preffix = (index + 1) + ":";
        if (printTick) {
            preffix = tick + ": " + preffix;
        }
        return preffix + message.replaceAll("\\n", "\n" + preffix);
    }

    private Game createGame() {
        PlayerScores score = gameType.getPlayerScores(0);
        scores.add(score);
        int index = scores.indexOf(score);

        GamePlayer gamePlayer = gameType.createPlayer(
                event -> {
                    print(index, "Fire Event: " + event.toString());
                    score.event(event);
                },
                getPlayerId());

        PrinterFactory factory = gameType.getPrinterFactory();

        Game game = new Single(gamePlayer, factory);
        game.on(field);
        game.newGame();
        return game;
    }

    private String getPlayerId() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

}
