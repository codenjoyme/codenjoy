package com.codenjoy.dojo.services;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: sanja
 * Date: 11.01.14
 * Time: 2:45
 */
public class ActionLoggerTest {

    @Test
    public void shouldLog() {
        ActionLogger logger = new ActionLogger();

        PlayerGames playerGames = new PlayerGames();

        addPlayer(playerGames, "board1", 123, "player1", "game1");
        addPlayer(playerGames, "board2", 234, "player2", "game2");

        logger.log(playerGames);

        assertEquals("[BoardLog{playerName='player1', board='board1', gameType='game1', score=123}, " +
                "BoardLog{playerName='player2', board='board2', gameType='game2', score=234}]", logger.getAll().toString());
    }

    private void addPlayer(PlayerGames playerGames, String board, int value, String name, String gameName) {
        Game game = getBoard(board);
        PlayerScores score = getScore(value);

        Player player = new Player(name, "password", "127.0.0.1", gameName, score, null, Protocol.WS);
        playerGames.add(player, game, mock(PlayerController.class));
    }

    private PlayerScores getScore(int value) {
        PlayerScores score = mock(PlayerScores.class);
        when(score.getScore()).thenReturn(value);
        return score;
    }

    private Game getBoard(String board) {
        Game game = mock(Game.class);
        when(game.getBoardAsString()).thenReturn(board);
        return game;
    }
}
