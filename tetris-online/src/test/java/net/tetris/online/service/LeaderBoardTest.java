package net.tetris.online.service;

import net.tetris.services.levels.AllFigureLevels;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
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
        fixture.setupConfiguration(serviceConfiguration);
        leaderBoard = new LeaderBoard(serviceConfiguration);
        leaderBoard.init();
    }

    @After
    public void tearDown() throws IOException {
        fixture.tearDown();
    }

    @Test
    public void shouldAddScores(){
        leaderBoard.addScore("vasya", 1, AllFigureLevels.class, "123");

        assertEquals(1, leaderBoard.getScores().get(0).getScore());
    }

    @Test
    public void shouldAddScoreWhenGreater(){
        leaderBoard.addScore("vasya", 1, AllFigureLevels.class, "123");

        leaderBoard.addScore("vasya", 2, AllFigureLevels.class, "123");

        assertEquals(2, leaderBoard.getScores().get(0).getScore());
    }

    @Test
    public void shouldIgnoreScoreWhenLess(){
        leaderBoard.addScore("vasya", 2, AllFigureLevels.class, "123");

        leaderBoard.addScore("vasya", 1, AllFigureLevels.class, "123");

        assertEquals(2, leaderBoard.getScores().get(0).getScore());
    }

    @Test
    public void shouldSortWhenSeveralPlayers(){
        leaderBoard.addScore("vasya", 1, AllFigureLevels.class, "123");
        leaderBoard.addScore("petya", 2, AllFigureLevels.class, "123");

        List<Score> scores = leaderBoard.getScores();
        assertEquals(2, scores.size());
        assertScoreAtIndex(scores, "petya", 2, 0);
        assertScoreAtIndex(scores, "vasya", 1, 1);
    }

    @Test
    public void shouldStoreScores() throws IOException {
        leaderBoard.addScore("vasya", 1, AllFigureLevels.class, "345");

        LeaderBoard newLeaderBoard = createLeaderBoard();

        assertScoreAtIndex(newLeaderBoard.getScores(), "vasya", 1, 0);
    }

    @Test
    public void shouldOverwriteScoresFile() throws IOException {
        leaderBoard.addScore("vasya", 1, AllFigureLevels.class, "345");
        leaderBoard.addScore("vasya", 2, AllFigureLevels.class, "456");

        LeaderBoard newBoard = createLeaderBoard();

        assertEquals(1, newBoard.getScores().size());
    }

    private LeaderBoard createLeaderBoard() throws IOException {
        LeaderBoard newLeaderBoard = new LeaderBoard(serviceConfiguration);
        newLeaderBoard.init();
        return newLeaderBoard;
    }

    private void assertScoreAtIndex(List<Score> scores, String expectedName, int expectedScore, int index) {
        assertEquals(expectedScore, scores.get(index).getScore());
        assertEquals(expectedName, scores.get(index).getPlayerName());
    }
}
