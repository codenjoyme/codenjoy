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


import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocalGameRunnerTest {

    private GamePlayer gamePlayer;
    private EventListener listener;
    private GameType gameType;
    private Solver solver;
    private ClientBoard board;
    private int id;

    @Test
    public void runGame() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 100;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 3;
        LocalGameRunner.printWelcome = false;

        id = 0;

        gameType = mock(GameType.class);
        SettingsReader settings = new TestGameSettings();
        GameField field = new GameField() {
            public GamePlayer player;

            @Override
            public BoardReader reader() {
                messages.add("GET_READER" + id());

                return new BoardReader<GamePlayer>() {
                    @Override
                    public int size() {
                        return id++;
                    }

                    @Override
                    public Iterable<? extends Point> elements(GamePlayer player) {
                        return new LinkedList<Point>(){{
                            add(pt(1, id++));
                            add(pt(2, id++));
                            add(pt(3, id++));
                        }};
                    }

                    @Override
                    public String toString() {
                        return String.format("size:%s,elements:%s", size(), elements(null));
                    }
                };
            }

            @Override
            public void newGame(GamePlayer player) {
                player.newHero(this);
                this.player = player;
                messages.add("NEW_GAME" + id());
            }

            @Override
            public void remove(GamePlayer player) {
                messages.add("REMOVE_GAME" + id());
            }

            @Override
            public SettingsReader settings() {
                return settings;
            }

            @Override
            public void tick() {
                messages.add("TICK_GAME" + id());
                player.getHero().tick();
            }
        };
        when(gameType.getSettings()).thenReturn((Settings) settings);
        when(gameType.createGame(anyInt(), any(Settings.class))).thenReturn(field);
        when(gameType.getPrinterFactory()).thenReturn(PrinterFactory.get(
                (BoardReader reader, GamePlayer player)
                        -> "PRINTER_PRINTS_BOARD" + id() + "{reader=" + reader + ",player=" + player + "}")
        );

        listener = event -> messages.add("GOT_EVENT" + id() + "{" + event + "}");

        PlayerScores scores = mock(PlayerScores.class);
        when(gameType.getPlayerScores(anyInt(), any(Settings.class))).thenReturn(scores);
        when(scores.getScore()).thenAnswer(inv -> "SCORE" + id());

        gamePlayer = new GamePlayer(listener, settings) {
            PlayerHero hero;

            @Override
            public PlayerHero getHero() {
                return hero;
            }

            @Override
            public void newHero(GameField field) {
                hero = new PlayerHero() {
                    @Override
                    public boolean isAlive() {
                        return true;
                    }

                    @Override
                    public void down() {
                        gamePlayer.event("EVENT" + id() + "(DOWN)");
                    }

                    @Override
                    public void up() {
                        gamePlayer.event("EVENT" + id() + "(UP)");
                    }

                    @Override
                    public void left() {
                        gamePlayer.event("EVENT" + id() + "(LEFT)");
                    }

                    @Override
                    public void right() {
                        gamePlayer.event("EVENT" + id() + "(RIGHT)");
                    }

                    @Override
                    public void act(int... p) {
                        gamePlayer.event("EVENT" + id() + "(ACT{" + Arrays.toString(p) + "})");
                    }

                    @Override
                    public void tick() {
                        messages.add("TICK_HERO" + id());
                    }
                };
                hero.init(field);
            }

            @Override
            public boolean isAlive() {
                return true;
            }

            @Override
            public String toString() {
                return "PLAYER" + id();
            }
        };
        when(gameType.createPlayer(any(EventListener.class), any(String.class), any(Settings.class)))
                .thenReturn(gamePlayer);

        solver = board -> {
            String command = "ACT(1,5),LEFT,RIGHT,ACT(9),UP,DOWN";
            messages.add("SOLVER_SAID_COMMAND" + id() + "{" + command + "}");
            return command;
        };

        board = new ClientBoard() {
            @Override
            public ClientBoard forString(String input) {
                messages.add("CLIENT_GOT_BOARD" + id() + "{" + input + "}");
                return board;
            }

            @Override
            public String toString() {
                return "CLIENT_BOARD_PRINTED_TO_STRING" + id();
            }
        };

        // when
        LocalGameRunner.run(gameType, solver, board);

        // then
        assertEquals("GET_READER#0\n" +
                "NEW_GAME#1\n" +
                "CLIENT_GOT_BOARD#8{PRINTER_PRINTS_BOARD#2{reader=size:3,elements:[[1,4], [2,5], [3,6]],player=PLAYER#7}}\n" +
                "1:CLIENT_BOARD_PRINTED_TO_STRING#9\n" +
                "SOLVER_SAID_COMMAND#10{ACT(1,5),LEFT,RIGHT,ACT(9),UP,DOWN}\n" +
                "1:Scores: SCORE#11\n" +
                "1:Answer: ACT(1,5),LEFT,RIGHT,ACT(9),UP,DOWN\n" +
                "GOT_EVENT#13{EVENT#12(ACT{[1, 5]})}\n" +
                "GOT_EVENT#15{EVENT#14(LEFT)}\n" +
                "GOT_EVENT#17{EVENT#16(RIGHT)}\n" +
                "GOT_EVENT#19{EVENT#18(ACT{[9]})}\n" +
                "GOT_EVENT#21{EVENT#20(UP)}\n" +
                "GOT_EVENT#23{EVENT#22(DOWN)}\n" +
                "TICK_GAME#24\n" +
                "TICK_HERO#25\n" +
                "------------------------------------------\n" +
                "CLIENT_GOT_BOARD#32{PRINTER_PRINTS_BOARD#26{reader=size:27,elements:[[1,28], [2,29], [3,30]],player=PLAYER#31}}\n" +
                "1:CLIENT_BOARD_PRINTED_TO_STRING#33\n" +
                "SOLVER_SAID_COMMAND#34{ACT(1,5),LEFT,RIGHT,ACT(9),UP,DOWN}\n" +
                "1:Scores: SCORE#35\n" +
                "1:Answer: ACT(1,5),LEFT,RIGHT,ACT(9),UP,DOWN\n" +
                "GOT_EVENT#37{EVENT#36(ACT{[1, 5]})}\n" +
                "GOT_EVENT#39{EVENT#38(LEFT)}\n" +
                "GOT_EVENT#41{EVENT#40(RIGHT)}\n" +
                "GOT_EVENT#43{EVENT#42(ACT{[9]})}\n" +
                "GOT_EVENT#45{EVENT#44(UP)}\n" +
                "GOT_EVENT#47{EVENT#46(DOWN)}\n" +
                "TICK_GAME#48\n" +
                "TICK_HERO#49\n" +
                "------------------------------------------\n" +
                "CLIENT_GOT_BOARD#56{PRINTER_PRINTS_BOARD#50{reader=size:51,elements:[[1,52], [2,53], [3,54]],player=PLAYER#55}}\n" +
                "1:CLIENT_BOARD_PRINTED_TO_STRING#57\n" +
                "SOLVER_SAID_COMMAND#58{ACT(1,5),LEFT,RIGHT,ACT(9),UP,DOWN}\n" +
                "1:Scores: SCORE#59\n" +
                "1:Answer: ACT(1,5),LEFT,RIGHT,ACT(9),UP,DOWN\n" +
                "GOT_EVENT#61{EVENT#60(ACT{[1, 5]})}\n" +
                "GOT_EVENT#63{EVENT#62(LEFT)}\n" +
                "GOT_EVENT#65{EVENT#64(RIGHT)}\n" +
                "GOT_EVENT#67{EVENT#66(ACT{[9]})}\n" +
                "GOT_EVENT#69{EVENT#68(UP)}\n" +
                "GOT_EVENT#71{EVENT#70(DOWN)}\n" +
                "TICK_GAME#72\n" +
                "TICK_HERO#73\n" +
                "------------------------------------------",
                String.join("\n", messages));
    }

    private String id() {
        return "#" + id++;
    }

}
