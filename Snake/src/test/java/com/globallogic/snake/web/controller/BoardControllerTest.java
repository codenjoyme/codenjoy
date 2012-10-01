package com.globallogic.snake.web.controller;

import com.globallogic.snake.services.Player;
import com.globallogic.snake.services.PlayerScores;
import com.globallogic.snake.services.PlayerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
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
        when(playerService.findPlayer(anyString())).thenReturn(new Player("vasya", "http://11.11.11.11", new PlayerScores(0)));

        boardController.board(model, "vasya");

        assertEquals("vasya", getPlayers().get(0).getName());
    }

    private List<Player> getPlayers() {
        return (List<Player>) model.get("players");
    }
}
