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


import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactory;
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

        id = 0;

        gameType = mock(GameType.class);
        GameField field = new GameField() {
            public GamePlayer player;

            @Override
            public BoardReader reader() {
                messages.add("GET_READER" + id());

                return new BoardReader() {
                    @Override
                    public int size() {
                        return id++;
                    }

                    @Override
                    public Iterable<? extends Point> elements() {
                        return new LinkedList<Point>(){{
                            add(pt(1, id++));
                            add(pt(2, id++));
                            add(pt(3, id++));
                        }};
                    }

                    @Override
                    public String toString() {
                        return String.format("size:%s,elements:%s", size(), elements());
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
            public void tick() {
                messages.add("TICK_GAME" + id());
                player.getHero().tick();
            }
        };
        when(gameType.createGame(anyInt())).thenReturn(field);
        when(gameType.getPrinterFactory()).thenReturn(PrinterFactory.get((BoardReader reader, GamePlayer player) -> {
            return "PRINTER_PRINTS_BOARD{reader=" + reader + ",player=" + player + "}" + id();
        }));

        listener = new EventListener() {
            @Override
            public void event(Object event) {
                messages.add("GOT_EVENT{" + event + "}" + id());
            }
        };

        gamePlayer = new GamePlayer(listener) {
            PlayerHero hero;

            @Override
            public PlayerHero getHero() {
                return hero;
            }

            @Override
            public void newHero(GameField field) {
                hero = new PlayerHero() {
                    @Override
                    public void down() {
                        gamePlayer.event("EVENT(DOWN)" + id());
                    }

                    @Override
                    public void up() {
                        gamePlayer.event("EVENT(UP)" + id());
                    }

                    @Override
                    public void left() {
                        gamePlayer.event("EVENT(LEFT)" + id());
                    }

                    @Override
                    public void right() {
                        gamePlayer.event("EVENT(RIGHT)" + id());
                    }

                    @Override
                    public void act(int... p) {
                        gamePlayer.event("EVENT(ACT{" + Arrays.toString(p) + "})" + id());
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
        when(gameType.createPlayer(any(EventListener.class), any(String.class)))
                .thenReturn(gamePlayer);

        solver = new Solver() {
            @Override
            public String get(ClientBoard board) {
                String command = "ACT(1,5),LEFT,RIGHT,ACT(9),UP,DOWN";
                messages.add("SOLVER_SAID_COMMAND{" + command + "}" + id());
                return command;
            }
        };

        board = new ClientBoard() {
            @Override
            public ClientBoard forString(String input) {
                messages.add("GLIENT_GOT_BOARD{" + input + "}" + id());
                return board;
            }

            @Override
            public String toString() {
                return "CLIENT_BOARD_PRINTED_TO_SRING" + id();
            }
        };

        // when
        LocalGameRunner.run(gameType, solver, board);

        // then
        assertEquals("GET_READER#0\n" +
                        "NEW_GAME#1\n" +
                        "GLIENT_GOT_BOARD{PRINTER_PRINTS_BOARD{reader=size:2,elements:[[1,3], [2,4], [3,5]],player=PLAYER#6}#7}#8\n" +
                        "1:CLIENT_BOARD_PRINTED_TO_SRING#9\n" +
                        "SOLVER_SAID_COMMAND{ACT(1,5),LEFT,RIGHT,ACT(9),UP,DOWN}#10\n" +
                        "1:Answer: ACT(1,5),LEFT,RIGHT,ACT(9),UP,DOWN\n" +
                        "GOT_EVENT{EVENT(ACT{[1, 5]})#11}#12\n" +
                        "GOT_EVENT{EVENT(LEFT)#13}#14\n" +
                        "GOT_EVENT{EVENT(RIGHT)#15}#16\n" +
                        "GOT_EVENT{EVENT(ACT{[9]})#17}#18\n" +
                        "GOT_EVENT{EVENT(UP)#19}#20\n" +
                        "GOT_EVENT{EVENT(DOWN)#21}#22\n" +
                        "TICK_GAME#23\n" +
                        "TICK_HERO#24\n" +
                        "------------------------------------------\n" +
                        "GLIENT_GOT_BOARD{PRINTER_PRINTS_BOARD{reader=size:25,elements:[[1,26], [2,27], [3,28]],player=PLAYER#29}#30}#31\n" +
                        "1:CLIENT_BOARD_PRINTED_TO_SRING#32\n" +
                        "SOLVER_SAID_COMMAND{ACT(1,5),LEFT,RIGHT,ACT(9),UP,DOWN}#33\n" +
                        "1:Answer: ACT(1,5),LEFT,RIGHT,ACT(9),UP,DOWN\n" +
                        "GOT_EVENT{EVENT(ACT{[1, 5]})#34}#35\n" +
                        "GOT_EVENT{EVENT(LEFT)#36}#37\n" +
                        "GOT_EVENT{EVENT(RIGHT)#38}#39\n" +
                        "GOT_EVENT{EVENT(ACT{[9]})#40}#41\n" +
                        "GOT_EVENT{EVENT(UP)#42}#43\n" +
                        "GOT_EVENT{EVENT(DOWN)#44}#45\n" +
                        "TICK_GAME#46\n" +
                        "TICK_HERO#47\n" +
                        "------------------------------------------\n" +
                        "GLIENT_GOT_BOARD{PRINTER_PRINTS_BOARD{reader=size:48,elements:[[1,49], [2,50], [3,51]],player=PLAYER#52}#53}#54\n" +
                        "1:CLIENT_BOARD_PRINTED_TO_SRING#55\n" +
                        "SOLVER_SAID_COMMAND{ACT(1,5),LEFT,RIGHT,ACT(9),UP,DOWN}#56\n" +
                        "1:Answer: ACT(1,5),LEFT,RIGHT,ACT(9),UP,DOWN\n" +
                        "GOT_EVENT{EVENT(ACT{[1, 5]})#57}#58\n" +
                        "GOT_EVENT{EVENT(LEFT)#59}#60\n" +
                        "GOT_EVENT{EVENT(RIGHT)#61}#62\n" +
                        "GOT_EVENT{EVENT(ACT{[9]})#63}#64\n" +
                        "GOT_EVENT{EVENT(UP)#65}#66\n" +
                        "GOT_EVENT{EVENT(DOWN)#67}#68\n" +
                        "TICK_GAME#69\n" +
                        "TICK_HERO#70\n" +
                        "------------------------------------------",
                String.join("\n", messages));
    }

    private String id() {
        return "#" + id++;
    }

}
