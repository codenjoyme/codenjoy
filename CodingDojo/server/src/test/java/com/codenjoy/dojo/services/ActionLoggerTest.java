package com.codenjoy.dojo.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: sanja
 * Date: 11.01.14
 * Time: 2:45
 */
public class ActionLoggerTest {

    private static ActionLogger logger;

    @Before
    public void setup() {
        logger = new ActionLogger("target/logs.db" + new Random().nextInt(), 1);
    }

    @After
    public void tearDown() {
        logger.removeDatabase();
    }

    @Test
    public void shouldLogWhenEnabled() throws InterruptedException {
        logger.resume();

        act();

        assertEquals("[BoardLog{playerName='player1', board='board1', gameType='game1', score=123}, " +
                "BoardLog{playerName='player2', board='board2', gameType='game2', score=234}]", logger.getAll().toString());
    }

    @Test
    public void shouldNotLogWhenNotEnabled() throws InterruptedException {
        act();

        assertEquals("[]", logger.getAll().toString());
    }

    private void act() throws InterruptedException {
        PlayerGames playerGames = new PlayerGames();
        playerGames.statistics = mock(Statistics.class);

        addPlayer(playerGames, "board1", 123, "player1", "game1");
        addPlayer(playerGames, "board2", 234, "player2", "game2");

        logger.log(playerGames);
    }

    private void addPlayer(PlayerGames playerGames, String board, int value, String name, String gameName) {
        Game game = getBoard(board);
        PlayerScores score = getScore(value);

        Player player = new Player(name, "127.0.0.1", PlayerTest.mockGameType(gameName), score, null, Protocol.WS);
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
