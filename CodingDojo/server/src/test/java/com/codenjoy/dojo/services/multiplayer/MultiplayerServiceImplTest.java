package com.codenjoy.dojo.services.multiplayer;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultiplayerServiceImplTest {

    private MultiplayerService multiplayer;

    private PlayerGames playerGames;
    private PrinterFactory printerFactory;

    private GameType single;
    private GameType multiple;
    private GameType tournament;
    private GameType triple;
    private GameType quadro;

    private List<GameField> fields = new LinkedList<>();

    @Before
    public void setup() {
        playerGames = new PlayerGames(mock(Statistics.class));
        multiplayer = new MultiplayerServiceImpl(playerGames);
        printerFactory = mock(PrinterFactory.class);

        single = setupGameType("single", MultiplayerType.SINGLE);
        tournament = setupGameType("tournament", MultiplayerType.TOURNAMENT);
        triple = setupGameType("triple", MultiplayerType.TRIPLE);
        quadro = setupGameType("quadro", MultiplayerType.QUADRO);
        multiple = setupGameType("multiple", MultiplayerType.MULTIPLE);
    }

    private GameType setupGameType(String gameName, MultiplayerType type) {
        GameType result = mock(GameType.class);
        when(result.getMultiplayerType()).thenReturn(type);
        when(result.name()).thenReturn(gameName);
        when(result.getPrinterFactory()).thenReturn(printerFactory);
        when(result.createGame()).thenAnswer(inv -> createGameField());
        return result;
    }

    private GameField createGameField() {
        GameField field = mock(GameField.class);
        when(field.reader()).thenReturn(mock(BoardReader.class));
        fields.add(field);
        return field;
    }

    @Test
    public void shouldEveryPlayerGoToTheirOwnField_whenSingle() {
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();

        PlayerGame playerGame1 = multiplayer.playerWantsToPlay(single, player1, null);
        PlayerGame playerGame2 = multiplayer.playerWantsToPlay(single, player2, null);
        PlayerGame playerGame3 = multiplayer.playerWantsToPlay(single, player3, null);

        GameField field1 = playerGame1.getGame().getField();
        GameField field2 = playerGame2.getGame().getField();
        GameField field3 = playerGame3.getGame().getField();

        assertNotSame(field1, field2);
        assertNotSame(field2, field3);
        assertNotSame(field3, field1);
    }

    @Test
    public void shouldTwoPlayerOnBoard_whenTournament() {
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Player player5 = new Player();

        PlayerGame playerGame1 = multiplayer.playerWantsToPlay(tournament, player1, null);
        PlayerGame playerGame2 = multiplayer.playerWantsToPlay(tournament, player2, null);
        PlayerGame playerGame3 = multiplayer.playerWantsToPlay(tournament, player3, null);
        PlayerGame playerGame4 = multiplayer.playerWantsToPlay(tournament, player4, null);
        PlayerGame playerGame5 = multiplayer.playerWantsToPlay(tournament, player5, null);

        GameField field1 = playerGame1.getGame().getField();
        GameField field2 = playerGame2.getGame().getField();
        GameField field3 = playerGame3.getGame().getField();
        GameField field4 = playerGame4.getGame().getField();
        GameField field5 = playerGame5.getGame().getField();

        assertSame(field1, field2);
        assertSame(field3, field4);

        assertNotSame(field1, field3, field5);
    }

    public void assertSame(GameField... fields) {
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields.length; j++) {
                if (j == i) continue;
                Assert.assertSame(String.format("[%s]==[%s]", i, j),
                        fields[i], fields[j]);
            }
        }
    }

    public void assertNotSame(GameField... fields) {
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields.length; j++) {
                if (j == i) continue;
                Assert.assertNotSame(String.format("[%s]!=[%s]", i, j),
                        fields[i], fields[j]);
            }
        }
    }

    @Test
    public void shouldThreePlayerOnBoard_whenTriple() {
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Player player5 = new Player();

        PlayerGame playerGame1 = multiplayer.playerWantsToPlay(triple, player1, null);
        PlayerGame playerGame2 = multiplayer.playerWantsToPlay(triple, player2, null);
        PlayerGame playerGame3 = multiplayer.playerWantsToPlay(triple, player3, null);
        PlayerGame playerGame4 = multiplayer.playerWantsToPlay(triple, player4, null);
        PlayerGame playerGame5 = multiplayer.playerWantsToPlay(triple, player5, null);

        GameField field1 = playerGame1.getGame().getField();
        GameField field2 = playerGame2.getGame().getField();
        GameField field3 = playerGame3.getGame().getField();
        GameField field4 = playerGame4.getGame().getField();
        GameField field5 = playerGame5.getGame().getField();

        assertSame(field1, field2, field3);
        assertSame(field4, field5);

        assertNotSame(field1, field4);
        assertNotSame(field2, field5);
        assertNotSame(field3, field5);
    }

    @Test
    public void shouldFourPlayerOnBoard_whenQuadro() {
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Player player5 = new Player();

        PlayerGame playerGame1 = multiplayer.playerWantsToPlay(quadro, player1, null);
        PlayerGame playerGame2 = multiplayer.playerWantsToPlay(quadro, player2, null);
        PlayerGame playerGame3 = multiplayer.playerWantsToPlay(quadro, player3, null);
        PlayerGame playerGame4 = multiplayer.playerWantsToPlay(quadro, player4, null);
        PlayerGame playerGame5 = multiplayer.playerWantsToPlay(quadro, player5, null);

        GameField field1 = playerGame1.getGame().getField();
        GameField field2 = playerGame2.getGame().getField();
        GameField field3 = playerGame3.getGame().getField();
        GameField field4 = playerGame4.getGame().getField();
        GameField field5 = playerGame5.getGame().getField();

        assertSame(field1, field2, field3, field4);
        assertSame(field5);

        assertNotSame(field1, field5);
        assertNotSame(field2, field5);
        assertNotSame(field3, field5);
        assertNotSame(field4, field5);
    }

    @Test
    public void shouldAllPlayersGoToOneField_whenMultiple() {
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();

        PlayerGame playerGame1 = multiplayer.playerWantsToPlay(multiple, player1, null);
        PlayerGame playerGame2 = multiplayer.playerWantsToPlay(multiple, player2, null);
        PlayerGame playerGame3 = multiplayer.playerWantsToPlay(multiple, player3, null);

        GameField field1 = playerGame1.getGame().getField();
        GameField field2 = playerGame2.getGame().getField();
        GameField field3 = playerGame3.getGame().getField();

        assertSame(field1, field2);
        assertSame(field2, field3);
        assertSame(field3, field1);
    }
}
