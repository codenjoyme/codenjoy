package com.codenjoy.dojo.excitebike.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.excitebike.model.items.bike.Bike;
import com.codenjoy.dojo.excitebike.model.items.bike.BikeType;
import com.codenjoy.dojo.excitebike.services.parse.MapParser;
import com.codenjoy.dojo.excitebike.services.parse.MapParserImpl;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class PlayersSpawnSystemParametrizedTest {

    private String name;
    private String init;
    private String expected;
    private int newPlayerNumberAfterInit;

    public PlayersSpawnSystemParametrizedTest(String name, int newPlayerNumberAfterInit, String init, String expected) {
        this.name = name;
        this.newPlayerNumberAfterInit = newPlayerNumberAfterInit;
        this.init = init;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object> data() {
        return Lists.newArrayList(
                new Object[]{"shouldAddThreeBikesInFirstChessColumn",
                        0,
                        "■■■■■■■" +
                                "       " +
                                "       " +
                                "       " +
                                "       " +
                                "       " +
                                "■■■■■■■",
                        "■■■■■■■\n" +
                                "       \n" +
                                "       \n" +
                                " e     \n" +
                                "  e    \n" +
                                " o     \n" +
                                "■■■■■■■\n"
                },
                new Object[]{"shouldAddFiveBikesInFirstFullChessColumn",
                        0,
                        "■■■■■■■" +
                                "       " +
                                "       " +
                                "       " +
                                "       " +
                                "       " +
                                "■■■■■■■",
                        "■■■■■■■\n" +
                                " e     \n" +
                                "  e    \n" +
                                " e     \n" +
                                "  e    \n" +
                                " o     \n" +
                                "■■■■■■■\n"
                },
                new Object[]{"shouldAddSevenBikesInFirstAnSecondChessColumns",
                        0,
                        "■■■■■■■" +
                                "       " +
                                "       " +
                                "       " +
                                "       " +
                                "       " +
                                "■■■■■■■",
                        "■■■■■■■\n" +
                                " e     \n" +
                                "  e    \n" +
                                " e     \n" +
                                "  e  e \n" +
                                " o  e  \n" +
                                "■■■■■■■\n"
                },
                new Object[]{"shouldAddTenBikesInFirstAnSecondFullChessColumns",
                        0,
                        "■■■■■■■" +
                                "       " +
                                "       " +
                                "       " +
                                "       " +
                                "       " +
                                "■■■■■■■",
                        "■■■■■■■\n" +
                                " e  e  \n" +
                                "  e  e \n" +
                                " e  e  \n" +
                                "  e  e \n" +
                                " o  e  \n" +
                                "■■■■■■■\n"
                },
                new Object[]{"shouldAddOneNewBike",
                        1,
                        "■■■■■■■" +
                                "       " +
                                "       " +
                                "   o   " +
                                "       " +
                                "    o  " +
                                "■■■■■■■",
                        "■■■■■■■\n" +
                                "       \n" +
                                "       \n" +
                                "   o   \n" +
                                "  e    \n" +
                                "    e  \n" +
                                "■■■■■■■\n"
                },
                new Object[]{"shouldAddManyNewBikes",
                        3,
                        "■■■■■■■" +
                                "       " +
                                "     o " +
                                " o     " +
                                "      o" +
                                "    o o" +
                                "■■■■■■■",
                        "■■■■■■■\n" +
                                " e     \n" +
                                "  e  o \n" +
                                " e     \n" +
                                "  e   e\n" +
                                "    e e\n" +
                                "■■■■■■■\n"
                }
        );
    }

    @Test
    public void shouldSpawnPlayers() {
        //given
        MapParser mapParser = new MapParserImpl(init);

        Dice dice = mock(Dice.class);
        when(dice.next(anyInt())).thenReturn(5);
        GameField field = new GameFieldImpl(mapParser, dice);
        PrinterFactory factory = new PrinterFactoryImpl();

        List<Game> games = new ArrayList<>();

        if (mapParser.getBikes().isEmpty()) {
            int playersNumber = StringUtils.countMatches(expected, BikeType.OTHER_BIKE.ch()) + 1;
            for (int i = 0; i < playersNumber; i++) {
                Game game = createNewGame(field, factory);
                games.add(game);
            }
        } else {
            for (Bike bike : mapParser.getBikes()) {
                Game game = createNewGame(field, factory);
                games.add(game);
                ((Player) game.getPlayer()).setHero(bike);
            }
        }

        //when
        for (int i = 0; i < newPlayerNumberAfterInit; i++) {
            Game game = createNewGame(field, factory);
            games.add(game);
        }
        String res = (String) games.get(0).getBoardAsString();

        //then
        assertEquals(expected, res);
    }

    private Game createNewGame(GameField field, PrinterFactory factory) {
        Game game = new Single(new Player(mock(EventListener.class)), factory);
        game.on(field);
        game.newGame();
        return game;
    }

}
