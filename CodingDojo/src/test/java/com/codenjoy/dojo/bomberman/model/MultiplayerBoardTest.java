package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.Game;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: sanja
 * Date: 20.04.13
 * Time: 13:40
 */
public class MultiplayerBoardTest {

    public static final int SIZE = 5;
    private SingleBoard game2;
    private WallsImpl walls;
    private Bomberman bomberman2;
    private Bomberman bomberman1;
    private GameSettings settings;
    private Level level;
    private SingleBoard game1;
    private Board board;

    @Before
    public void setup() {
        settings = mock(GameSettings.class);

        bomberman1 = mock(Bomberman.class);
        bomberman2 = mock(Bomberman.class);
        setPosition(0, 0, bomberman1);
        setPosition(SIZE - 1, SIZE - 1, bomberman2);
        when(settings.getBomberman(any(Level.class))).thenReturn(bomberman1, bomberman2);

        level = mock(Level.class);

        walls = mock(WallsImpl.class);
        when(walls.iterator()).thenReturn(new LinkedList<Wall>().iterator());

        when(settings.getLevel()).thenReturn(level);
        when(settings.getBoardSize()).thenReturn(SIZE);
        when(settings.getWalls()).thenReturn(walls);

        board = new Board(settings, null);
        game1 = new SingleBoard(board);
        game2 = new SingleBoard(board);

        game1.newGame();
        game2.newGame();

    }

    private void setPosition(int x, int y, Bomberman bomberman) {
        when(bomberman.getX()).thenReturn(x);
        when(bomberman.getY()).thenReturn(y);
    }

    @Test
    public void shouldGetTwoBombermansOnBoard() {
        assertSame(bomberman1, game1.getJoystick());
        assertSame(bomberman2, game2.getJoystick());

        assertBoard(
                "☺    " +
                "     " +
                "     " +
                "     " +
                "    &", game1);

        assertBoard(
                "&    " +
                "     " +
                "     " +
                "     " +
                "    ☺", game2);
    }

    private void assertBoard(String board, SingleBoard game) {
        assertEquals(board, game.getBoardAsString().replaceAll("\n", ""));
    }

}
