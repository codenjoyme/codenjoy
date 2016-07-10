package net.tetris.web.controller;

import net.tetris.services.Player;
import net.tetris.services.PlayerScores;
import net.tetris.services.PlayerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.*;
import static net.tetris.TestUtils.emptyLevels;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * User: serhiy.zelenin
 * Date: 5/19/12
 * Time: 10:13 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class BoardControllerTest {
    @Mock
    private PlayerService playerService;
    private BoardController boardController;
    private ModelMap model;

    @Before
    public void setUp() throws Exception {
        boardController = new BoardController(playerService);
        model = new ModelMap();
    }

    @Test
    public void shouldCreateEmptyListWhenPlayerNotFound() {
        when(playerService.findPlayer(anyString())).thenReturn(null);

        boardController.board(model, "vasya");

        assertTrue(getPlayers().isEmpty());
    }

    @Test
    public void shouldReturnPlayerWhenFound() {
        when(playerService.findPlayer(anyString())).thenReturn(getDummyPlayer());

        boardController.board(model, "vasya");

        assertEquals("vasya", getPlayers().get(0).getName());
    }

    private Player getDummyPlayer() {
        return new Player("vasya", "http://11.11.11.11", new PlayerScores(0), emptyLevels(), null);
    }

    @Test
    public void shouldReturnAllPlayersWhenAllBoard() {
        when(playerService.getPlayers()).thenReturn(Arrays.asList(getDummyPlayer(), getDummyPlayer()));

        boardController.boardAll(model);

        assertEquals(2, getPlayers().size());
    }

    @Test
    public void checkAllPlayersBoardFlag() {
        boardController.boardAll(model);
        assertTrue((Boolean) model.get("allPlayersScreen"));

        boardController.board(model, "");
        assertFalse((Boolean) model.get("allPlayersScreen"));
    }

    private List<Player> getPlayers() {
        return (List<Player>) model.get("players");
    }
}
