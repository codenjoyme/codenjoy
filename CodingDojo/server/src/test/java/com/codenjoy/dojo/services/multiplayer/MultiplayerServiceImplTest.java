package com.codenjoy.dojo.services.multiplayer;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
    private GameType team4;

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
        team4 = setupGameType("team4", MultiplayerType.TEAM.apply(4));
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

        assertThat(field1)
                .notIn(field2)
                .notIn(field3)
                .check();
    }

    private static class GroupsAsserter {

        private List<List<GameField>> groups;

        public GroupsAsserter(GameField[] group) {
            groups = new LinkedList();
            notIn(group);
        }

        public GroupsAsserter notIn(GameField... anotherGroup) {
            groups.add(Arrays.asList(anotherGroup));
            return this;
        }

        public void check() {
            List<String> expected = new LinkedList<>();
            List<String> actual = new LinkedList<>();

            for (int i = 0; i < groups.size(); i++) {
                List<GameField> group = groups.get(i);
                for (int ii = 0; ii < group.size(); ii++) {
                    for (int jj = ii + 1; jj < group.size(); jj++) {
                        if (jj == ii) continue;
                        String message = String.format("[G%s:%s]==[G%s:%s]", i + 1, ii + 1, i + 1, jj + 1);
                        expected.add(message);
                        if (group.get(ii) == group.get(jj)) {
                            actual.add(message);
                        } else {
                            actual.add(message.replace("==", "!="));
                        }
                    }
                }
                for (int j = i + 1; j < groups.size(); j++) {
                    List<GameField> group1 = groups.get(i);
                    List<GameField> group2 = groups.get(j);
                    for (int ii = 0; ii < group1.size(); ii++) {
                        for (int jj = 0; jj < group2.size(); jj++) {
                            String message = String.format("[G%s:%s]!=[G%s:%s]", i + 1, ii + 1, j + 1, jj + 1);
                            expected.add(message);
                            if (group1.get(ii) != group2.get(jj)) {
                                actual.add(message);
                            } else {
                                actual.add(message.replace("!=", "=="));
                            }
                        }
                    }
                }
                assertEquals(expected.toString().replaceAll(",", ",\n"),
                        actual.toString().replaceAll(",", ",\n"));
            }
        }
    }

    private GroupsAsserter assertThat(GameField... group1) {
        return new GroupsAsserter(group1);
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

        assertThat(field1, field2)
                .notIn(field3, field4)
                .notIn(field5)
                .check();
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

        assertThat(field1, field2, field3)
                .notIn(field4, field5)
                .check();
    }

    @Test
    public void shouldXPlayerOnBoard_whenTeam() {
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Player player5 = new Player();

        PlayerGame playerGame1 = multiplayer.playerWantsToPlay(team4, player1, null);
        PlayerGame playerGame2 = multiplayer.playerWantsToPlay(team4, player2, null);
        PlayerGame playerGame3 = multiplayer.playerWantsToPlay(team4, player3, null);
        PlayerGame playerGame4 = multiplayer.playerWantsToPlay(team4, player4, null);
        PlayerGame playerGame5 = multiplayer.playerWantsToPlay(team4, player5, null);

        GameField field1 = playerGame1.getGame().getField();
        GameField field2 = playerGame2.getGame().getField();
        GameField field3 = playerGame3.getGame().getField();
        GameField field4 = playerGame4.getGame().getField();
        GameField field5 = playerGame5.getGame().getField();

        assertThat(field1, field2, field3, field4)
                .notIn(field5)
                .check();
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

        assertThat(field1, field2, field3, field4)
                .notIn(field5)
                .check();
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

        assertThat(field1, field2, field3)
                .check();
    }
}
