package net.tetris.online.service;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * User: serhiy.zelenin
 * Date: 10/26/12
 * Time: 2:11 PM
 */
public class LeaderBoardTest {

    private LeaderBoard leaderBoard;
    private ServiceConfigFixture fixture;
    private ServiceConfiguration serviceConfiguration;

    @Before
    public void setUp() throws Exception {
        fixture = new ServiceConfigFixture();
        serviceConfiguration = Mockito.mock(ServiceConfiguration.class);
        fixture.setup();
        serviceConfiguration = fixture.getConfiguration();
        leaderBoard = new LeaderBoard(serviceConfiguration);
        leaderBoard.init();
    }

    @After
    public void tearDown() throws IOException {
        fixture.tearDown();
    }

    @Test
    public void shouldAddScores(){
        leaderBoard.addScore("vasya", 1, "AllFigureLevels", "123", 345);

        assertEquals(1, leaderBoard.getScores().get(0).getScore());
    }

    @Test
    public void shouldAddScoreWhenGreater(){
        leaderBoard.addScore("vasya", 1, "AllFigureLevels", "123", 345);

        leaderBoard.addScore("vasya", 2, "AllFigureLevels", "123", 345);

        assertEquals(2, leaderBoard.getScores().get(0).getScore());
    }

    @Test
    public void shouldIgnoreScoreWhenLess(){
        leaderBoard.addScore("vasya", 2, "AllFigureLevels", "123", 11);

        leaderBoard.addScore("vasya", 1, "AllFigureLevels", "123", 22);

        assertEquals(2, leaderBoard.getScores().get(0).getScore());
        assertEquals(11, leaderBoard.getScores().get(0).getLevel());
    }

    @Test
    public void shouldSortWhenSeveralPlayers(){
        leaderBoard.addScore("vasya", 1, "AllFigureLevels", "123", 345);
        leaderBoard.addScore("petya", 2, "AllFigureLevels", "123", 345);

        List<Score> scores = leaderBoard.getScores();
        assertEquals(2, scores.size());
        assertScoreAtIndex(scores, "petya", 2, 345, 0);
        assertScoreAtIndex(scores, "vasya", 1, 345, 1);
    }

    @Test
    public void shouldStoreScores() throws IOException {
        leaderBoard.addScore("vasya", 1, "AllFigureLevels", "345", 345);

        LeaderBoard newLeaderBoard = createLeaderBoard();

        assertScoreAtIndex(newLeaderBoard.getScores(), "vasya", 1, 345, 0);
    }

    @Test
    public void shouldOverwriteScoresFile() throws IOException {
        leaderBoard.addScore("vasya", 1, "AllFigureLevels", "345", 345);
        leaderBoard.addScore("vasya", 2, "AllFigureLevels", "456", 345);

        LeaderBoard newBoard = createLeaderBoard();

        assertEquals(1, newBoard.getScores().size());
    }

    @Test
    public void shouldIgnoreLessScore() throws IOException {
        leaderBoard.addScore("vasya", 80, "AllFigureLevels", "123", 1);
        leaderBoard.addScore("vasya", -470, "AllFigureLevels", "123", 1);

        assertEquals(1, leaderBoard.getScores().size());
        assertEquals(80, leaderBoard.getScores().get(0).getScore());
    }

    @Test
    public void shouldAddScoreWhenAnotherPlayerHasBiggest(){
        leaderBoard.addScore("vasya", 100, "AllFigureLevels", "123", 1);

        leaderBoard.addScore("petya", 99, "AllFigureLevels", "123", 1);

        assertEquals(2, leaderBoard.getScores().size());
    }

    private LeaderBoard createLeaderBoard() throws IOException {
        LeaderBoard newLeaderBoard = new LeaderBoard(serviceConfiguration);
        newLeaderBoard.init();
        return newLeaderBoard;
    }

    private void assertScoreAtIndex(List<Score> scores, String expectedName, int expectedScore, int level, int index) {
        assertEquals(expectedScore, scores.get(index).getScore());
        assertEquals(expectedName, scores.get(index).getPlayerName());
        assertEquals(level, scores.get(index).getLevel());
    }
}
