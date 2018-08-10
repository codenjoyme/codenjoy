package com.codenjoy.dojo.services.multiplayer;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactory;
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

    private GameType single1;
    private GameType multiple1;
    private GameType tournament1;

    private List<GameField> fields = new LinkedList<>();

    @Before
    public void setup() {
        playerGames = new PlayerGames(mock(Statistics.class));
        multiplayer = new MultiplayerServiceImpl(playerGames);
        printerFactory = mock(PrinterFactory.class);

        single1 = setupGameType("single1", MultiplayerType.SINGLE);
        tournament1 = setupGameType("tournament1", MultiplayerType.TOURNAMENT);
        multiple1 = setupGameType("multiple1", MultiplayerType.MULTIPLE);
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

        PlayerGame playerGame1 = multiplayer.playerWantsToPlay(single1, player1, null);
        PlayerGame playerGame2 = multiplayer.playerWantsToPlay(single1, player2, null);
        PlayerGame playerGame3 = multiplayer.playerWantsToPlay(single1, player3, null);

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

        PlayerGame playerGame1 = multiplayer.playerWantsToPlay(tournament1, player1, null);
        PlayerGame playerGame2 = multiplayer.playerWantsToPlay(tournament1, player2, null);
        PlayerGame playerGame3 = multiplayer.playerWantsToPlay(tournament1, player3, null);
        PlayerGame playerGame4 = multiplayer.playerWantsToPlay(tournament1, player4, null);
        PlayerGame playerGame5 = multiplayer.playerWantsToPlay(tournament1, player5, null);

        GameField field1 = playerGame1.getGame().getField();
        GameField field2 = playerGame2.getGame().getField();
        GameField field3 = playerGame3.getGame().getField();
        GameField field4 = playerGame4.getGame().getField();
        GameField field5 = playerGame5.getGame().getField();

        assertSame(field1, field2);
        assertSame(field3, field4);

        assertNotSame(field1, field3);
        assertNotSame(field3, field5);
        assertNotSame(field1, field5);
    }

    @Test
    public void shouldAllPlayersGoToOneField_whenMultiple() {
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();

        PlayerGame playerGame1 = multiplayer.playerWantsToPlay(multiple1, player1, null);
        PlayerGame playerGame2 = multiplayer.playerWantsToPlay(multiple1, player2, null);
        PlayerGame playerGame3 = multiplayer.playerWantsToPlay(multiple1, player3, null);

        GameField field1 = playerGame1.getGame().getField();
        GameField field2 = playerGame2.getGame().getField();
        GameField field3 = playerGame3.getGame().getField();

        assertSame(field1, field2);
        assertSame(field2, field3);
        assertSame(field3, field1);
    }
}
