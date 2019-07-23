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

import com.codenjoy.dojo.excitebike.model.items.bike.BikeType;
import com.codenjoy.dojo.excitebike.services.SettingsHandler;
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
import java.util.stream.IntStream;

import static com.codenjoy.dojo.excitebike.TestUtils.parseBikes;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class PlayersSpawnSystemParametrizedTest {

    private String init;
    private String expected;
    private int newPlayerNumberAfterInit;

    public PlayersSpawnSystemParametrizedTest(String name, int newPlayerNumberAfterInit, String init, String expected) {
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
                                "Ḃ      \n" +
                                " Ḃ     \n" +
                                "B      \n" +
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
                                "Ḃ      \n" +
                                " Ḃ     \n" +
                                "Ḃ      \n" +
                                " Ḃ     \n" +
                                "B      \n" +
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
                                "Ḃ      \n" +
                                " Ḃ     \n" +
                                "Ḃ      \n" +
                                " Ḃ  Ḃ  \n" +
                                "B  Ḃ   \n" +
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
                                "Ḃ  Ḃ   \n" +
                                " Ḃ  Ḃ  \n" +
                                "Ḃ  Ḃ   \n" +
                                " Ḃ  Ḃ  \n" +
                                "B  Ḃ   \n" +
                                "■■■■■■■\n"
                },
                new Object[]{"shouldAddOneNewBike",
                        1,
                        "■■■■■■■" +
                                "       " +
                                "       " +
                                "  B    " +
                                "       " +
                                "   B   " +
                                "■■■■■■■",
                        "■■■■■■■\n" +
                                "       \n" +
                                "       \n" +
                                "  B    \n" +
                                " Ḃ     \n" +
                                "   Ḃ   \n" +
                                "■■■■■■■\n"
                },
                new Object[]{"shouldAddManyNewBikes",
                        3,
                        "■■■■■■■" +
                                "       " +
                                "    B  " +
                                "B      " +
                                "     B " +
                                "   B B " +
                                "■■■■■■■",
                        "■■■■■■■\n" +
                                "Ḃ      \n" +
                                " Ḃ  B  \n" +
                                "Ḃ      \n" +
                                " Ḃ   Ḃ \n" +
                                "   Ḃ Ḃ \n" +
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
        GameField field = new GameFieldImpl(mapParser, dice, new SettingsHandler());
        PrinterFactory factory = new PrinterFactoryImpl();

        List<Game> games = new ArrayList<>();

        if (parseBikes(init).isEmpty()) {
            int playersNumber = StringUtils.countMatches(expected, BikeType.OTHER_BIKE.ch()) + 1;
            IntStream.range(0, playersNumber).forEach(value -> games.add(createNewGame(field, factory)));
        } else {
            parseBikes(init).forEach(bike -> {
                Game game = createNewGame(field, factory);
                games.add(game);
                ((Player) game.getPlayer()).setHero(bike);
            });
        }

        //when
        IntStream.range(0, newPlayerNumberAfterInit).mapToObj(i -> createNewGame(field, factory)).forEach(games::add);
        String res = (String) games.get(0).getBoardAsString();

        //then
        assertThat(expected, is(res));
    }

    private Game createNewGame(GameField field, PrinterFactory factory) {
        Game game = new Single(new Player(mock(EventListener.class)), factory);
        game.on(field);
        game.newGame();
        return game;
    }

}
