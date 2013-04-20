package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.services.BombermanEvents;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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
    private EventListener listener1;
    private EventListener listener2;

    @Before
    public void setup() {
        settings = mock(GameSettings.class);

        level = mock(Level.class);
        when(level.bombsCount()).thenReturn(1);
        when(level.bombsPower()).thenReturn(1);

        bomberman1 = new MyBomberman(level);
        bomberman2 = new MyBomberman(level);
        when(settings.getBomberman(any(Level.class))).thenReturn(bomberman1, bomberman2);

        walls = mock(WallsImpl.class);
        when(walls.iterator()).thenReturn(new LinkedList<Wall>().iterator());

        when(settings.getLevel()).thenReturn(level);
        when(settings.getBoardSize()).thenReturn(SIZE);
        when(settings.getWalls()).thenReturn(walls);

        board = new Board(settings);

        listener1 = mock(EventListener.class);
        listener2 = mock(EventListener.class);

        game1 = new SingleBoard(board, listener1);
        game2 = new SingleBoard(board, listener2);

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
                "☺♥   \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n", game1);

        assertBoard(
                "♥☺   \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n", game2);
    }

    @Test
    public void shouldOnlyOneListenerWorksWhenOneBombermanKillAnother() {
        bomberman1.act();
        bomberman1.down();
        tick();
        bomberman1.down();
        tick();
        tick();
        tick();
        tick();

        assertBoard(
                "҉♣   \n" +
                "҉    \n" +
                "☺    \n" +
                "     \n" +
                "     \n", game1);

        verify(listener1, only()).event(BombermanEvents.KILL_MEAT_CHOPPER.name());
        verify(listener2, only()).event(BombermanEvents.KILL_BOMBERMAN.name());
    }

    private void tick() {
        board.tick();
        board.tick();
    }

    @Test
    public void shouldPrintOtherBombBomberman() {
        bomberman1.act();
        bomberman1.down();

        assertBoard(
                "☻♥   \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n", game1);

        assertBoard(
                "♠☺   \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n", game2);
    }

    @Test
    public void shouldBombermanCantGoToAnotherBomberman() {
        bomberman1.right();
        tick();

        assertBoard(
                "☺♥   \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n", game1);
    }

    private void assertBoard(String board, SingleBoard game) {
        assertEquals(board, game.getBoardAsString());
    }

    // на поле можно чтобы каждый поставил по бомбе

}
