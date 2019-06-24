package com.codenjoy.dojo.excitebike.model;

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
import java.util.function.Consumer;

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

    public PlayersSpawnSystemParametrizedTest(String name, String init, String expected) {
        this.name = name;
        this.init = init;
        this.expected = expected;
    }

    public PlayersSpawnSystemParametrizedTest(String name, String init, String expected, int newPlayerNumberAfterInit) {
        this.name = name;
        this.init = init;
        this.expected = expected;
        this.newPlayerNumberAfterInit = newPlayerNumberAfterInit;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object> data() {
        return Lists.newArrayList(
                new Object[]{"ThreePlayersInFirstChessColumn",
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
                new Object[]{"FivePlayersInFirstFullChessColumn",
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
                new Object[]{"SevenPlayersInFirstAnSecondChessColumns",
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
                new Object[]{"TenPlayersInFirstAnSecondFullChessColumns",
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
                new Object[]{"123",
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
                                "       \n" +
                                "    e  \n" +
                                "■■■■■■■\n",
                        1
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
