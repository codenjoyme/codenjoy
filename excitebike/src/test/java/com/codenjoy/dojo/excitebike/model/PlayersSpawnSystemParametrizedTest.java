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
                new Object[]{"1. shouldAddThreeBikesToFirstColumn",
                        3,
                        "■■■■■■■" +
                                "       " +
                                "       " +
                                "       " +
                                "       " +
                                "       " +
                                "■■■■■■■",
                        "■■■■■■■\n" +
                                "Ḃ      \n" +
                                "       \n" +
                                "Ḃ      \n" +
                                "       \n" +
                                "B      \n" +
                                "■■■■■■■\n"
                },
                new Object[]{"2. shouldAddFiveBikesInChessOrder",
                        5,
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
                new Object[]{"3. shouldAddSevenBikesInChessOrder",
                        7,
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
                                "Ḃ Ḃ    \n" +
                                " Ḃ     \n" +
                                "B Ḃ    \n" +
                                "■■■■■■■\n"
                },
                new Object[]{"4. shouldAddTenBikesInChessOrder",
                        10,
                        "■■■■■■■" +
                                "       " +
                                "       " +
                                "       " +
                                "       " +
                                "       " +
                                "■■■■■■■",
                        "■■■■■■■\n" +
                                "Ḃ Ḃ    \n" +
                                " Ḃ Ḃ   \n" +
                                "Ḃ Ḃ    \n" +
                                " Ḃ Ḃ   \n" +
                                "B Ḃ    \n" +
                                "■■■■■■■\n"
                },
                new Object[]{"5. shouldAddOneNewBikeBikesToFirstColumn",
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
                                "       \n" +
                                "Ḃ  Ḃ   \n" +
                                "■■■■■■■\n"
                },
                new Object[]{"6. shouldAddThreeNewBikes",
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
                                "    B  \n" +
                                "Ḃ      \n" +
                                " Ḃ   Ḃ \n" +
                                "Ḃ  Ḃ Ḃ \n" +
                                "■■■■■■■\n"
                },
                new Object[]{"7. shouldAddThreeBikesInFirstColumn__atSpringboardBeginning",
                        3,
                        "╔════╗■" +
                                "/════\\ " +
                                "/════\\ " +
                                "/════\\ " +
                                "/════\\ " +
                                "╚////╝ " +
                                "■■■■■■■",
                        "╔════╗■\n" +
                                "Ĺ════\\ \n" +
                                "/════\\ \n" +
                                "Ĺ════\\ \n" +
                                "/════\\ \n" +
                                "M////╝ \n" +
                                "■■■■■■■\n"
                },
                new Object[]{"8. shouldAddThreeBikesInFirstColumn__atSpringboardTop",
                        3,
                        "════╗■■" +
                                "════\\  " +
                                "════\\  " +
                                "════\\  " +
                                "════\\  " +
                                "////╝  " +
                                "■■■■■■■",
                        "Ḃ═══╗■■\n" +
                                "════\\  \n" +
                                "Ḃ═══\\  \n" +
                                "════\\  \n" +
                                "B═══\\  \n" +
                                "////╝  \n" +
                                "■■■■■■■\n"
                },
                new Object[]{"9. shouldAddThreeBikesInFirstColumn__atSpringboardEnding",
                        3,
                        "╗■■■■■■" +
                                "\\      " +
                                "\\      " +
                                "\\      " +
                                "\\      " +
                                "╝      " +
                                "■■■■■■■",
                        "╗■■■■■■\n" +
                                "Ř      \n" +
                                "\\      \n" +
                                "Ř      \n" +
                                "\\      \n" +
                                "S      \n" +
                                "■■■■■■■\n"
                },
                new Object[]{"10. shouldAddManyBikes__coveringWholeFieldInChessOrder",
                        8,
                        "■■■■■" +
                                "     " +
                                "     " +
                                "     " +
                                "■■■■■",
                        "■■■■■\n" +
                                "Ḃ Ḃ Ḃ\n" +
                                " Ḃ Ḃ \n" +
                                "B Ḃ Ḃ\n" +
                                "■■■■■\n"
                },
                new Object[]{"11. shouldAddManyBikes__coveringWholeFieldInChessOrderAndTwoMore",
                        10,
                        "■■■■■" +
                                "     " +
                                "     " +
                                "     " +
                                "■■■■■",
                        "■■■■■\n" +
                                "Ḃ Ḃ Ḃ\n" +
                                "ḂḂ Ḃ \n" +
                                "BḂḂ Ḃ\n" +
                                "■■■■■\n"
                },
                new Object[]{"12. shouldAddManyBikes__fullyCoveringWholeField",
                        15,
                        "■■■■■" +
                                "     " +
                                "     " +
                                "     " +
                                "■■■■■",
                        "■■■■■\n" +
                                "ḂḂḂḂḂ\n" +
                                "ḂḂḂḂḂ\n" +
                                "BḂḂḂḂ\n" +
                                "■■■■■\n"
                },
                new Object[]{"13. shouldAddManyBikes__coveringWholeSpringboardAndLinesBeforeAndAfterInChessOrder",
                        18,
                        "■╔═══╗■" +
                                " /═══\\ " +
                                " /═══\\ " +
                                " /═══\\ " +
                                " /═══\\ " +
                                " ╚///╝ " +
                                "■■■■■■■",
                        "■╔Ḃ═Ḃ╗■\n" +
                                "Ḃ/═Ḃ═\\Ḃ\n" +
                                " ĹḂ═ḂŘ \n" +
                                "Ḃ/═Ḃ═\\Ḃ\n" +
                                " ĹḂ═ḂŘ \n" +
                                "B╚///╝Ḃ\n" +
                                "■■■■■■■\n"
                },
                new Object[]{"14. shouldAddManyBikes__fullyCoveringWholeSpringboardAndLinesBeforeAndAfter",
                        35,
                        "■╔═══╗■" +
                                " /═══\\ " +
                                " /═══\\ " +
                                " /═══\\ " +
                                " /═══\\ " +
                                " ╚///╝ " +
                                "■■■■■■■",
                        "■╔ḂḂḂ╗■\n" +
                                "ḂĹḂḂḂŘḂ\n" +
                                "ḂĹḂḂḂŘḂ\n" +
                                "ḂĹḂḂḂŘḂ\n" +
                                "ḂĹḂḂḂŘḂ\n" +
                                "BṀ///ŜḂ\n" +
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

        parseBikes(init).forEach(bike -> {
            Game game = createNewGame(field, factory);
            games.add(game);
            ((Player) game.getPlayer()).setHero(bike);
        });


        //when
        IntStream.range(0, newPlayerNumberAfterInit).mapToObj(i -> createNewGame(field, factory)).forEach(games::add);
        String res = (String) games.get(0).getBoardAsString();

        //then
        assertThat(res, is(expected));
    }

    private Game createNewGame(GameField field, PrinterFactory factory) {
        Game game = new Single(new Player(mock(EventListener.class)), factory);
        game.on(field);
        game.newGame();
        return game;
    }

}
