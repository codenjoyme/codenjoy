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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class PlayersSpawnSystemParametrizedTest {

    private String name;
    private String init;
    private String expected;
    private boolean defaultSpawn;

    public PlayersSpawnSystemParametrizedTest(String name, String init, String expected, boolean defaultSpawn) {
        this.name = name;
        this.init = init;
        this.expected = expected;
        this.defaultSpawn = defaultSpawn;
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
                                "■■■■■■■\n",
                        true
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
                                "■■■■■■■\n",
                        true
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
                                "■■■■■■■\n",
                        true
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
                                "■■■■■■■\n",
                        true
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
                        false
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


//        int playersNumber = defaultSpawn ? StringUtils.countMatches(expected, BikeType.OTHER_BIKE.ch())+1 : StringUtils.countMatches(init, BikeType.BIKE.ch());
        List<Game> games = new ArrayList<>();

        if (defaultSpawn) {
            int playersNumber = StringUtils.countMatches(expected, BikeType.OTHER_BIKE.ch()) + 1;
            for (int i = 0; i < playersNumber; i++) {
                Game game = new Single(new Player(mock(EventListener.class)), factory);
                game.on(field);
                games.add(game);
                game.newGame();
            }
        } else {
            for (Bike bike: mapParser.getBikes()) {
                Game game = new Single(new Player(mock(EventListener.class)), factory);
                game.on(field);
                games.add(game);
                game.newGame();
                ((Player)game.getPlayer()).setHero(bike);
            }
        }

        //when
        String res = (String) games.get(0).getBoardAsString();

        //then
        assertEquals(expected, res);
    }


}
