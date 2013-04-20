package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.services.BombermanEvents;
import com.codenjoy.dojo.services.EventListener;
import org.junit.Test;

import java.util.LinkedList;

import static com.codenjoy.dojo.bomberman.model.BoardTest.*;
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
    private Walls walls = emptyWalls();
    private Bomberman bomberman2;
    private Bomberman bomberman1;
    private GameSettings settings;
    private Level level;
    private SingleBoard game1;
    private Board board;
    private EventListener listener1;
    private EventListener listener2;
    private int bombsCount = 1;

    public void givenBoard() {
        settings = mock(GameSettings.class);

        level = mock(Level.class);
        when(level.bombsCount()).thenReturn(bombsCount);
        when(level.bombsPower()).thenReturn(1);

        bomberman1 = new MyBomberman(level);
        bomberman2 = new MyBomberman(level);
        when(settings.getBomberman(any(Level.class))).thenReturn(bomberman1, bomberman2);

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

    private Walls emptyWalls() {
        Walls walls = mock(WallsImpl.class);
        when(walls.iterator()).thenReturn(new LinkedList<Wall>().iterator());
        return walls;
    }

    private void setPosition(int x, int y, Bomberman bomberman) {
        when(bomberman.getX()).thenReturn(x);
        when(bomberman.getY()).thenReturn(y);
    }

    @Test
    public void shouldGetTwoBombermansOnBoard() {
        givenBoard();

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
        givenBoard();

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
        givenBoard();

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
        givenBoard();

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

    // бомбермен может идти на митчопера, при этом он умирает
    @Test
    public void shouldKllOtherBombermanWhenBombermanGoToMeatChopper() {
        walls = new MeatChopperAt(2, 0, new WallsImpl());
        givenBoard();

        assertBoard(
                "☺♥&  \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n", game1);

        bomberman2.right();
        tick();
        assertBoard(
                "☺ ♣  \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n", game1);

        assertBoard(
                "♥ Ѡ  \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n", game2);

        verifyNoMoreInteractions(listener1);
        verify(listener2, only()).event(BombermanEvents.KILL_BOMBERMAN.name());
    }

    // если митчопер убил другого бомбермена, как это на моей доске отобразится? Хочу видеть трупик
    @Test
    public void shouldKllOtherBombermanWhenMeatChopperGoToIt() {
        Dice dice = mock(Dice.class);
        meatChopperAt(dice, 2, 0);
        givenBoard();

        assertBoard(
                "☺♥&  \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n", game1);

        when(dice.next(anyInt())).thenReturn(Direction.LEFT.value);
        tick();

        assertBoard(
                "☺♣   \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n", game1);

        assertBoard(
                "♥Ѡ   \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n", game2);

        verifyNoMoreInteractions(listener1);
        verify(listener2, only()).event(BombermanEvents.KILL_BOMBERMAN.name());
    }

    // А что если бомбермен идет на митчопера а тот идет на встречу к нему - бомбермен проскочит или умрет? должен умереть!
    @Test
    public void shouldKllOtherBombermanWhenMeatChopperAndBombermanMoves() {
        Dice dice = mock(Dice.class);
        meatChopperAt(dice, 2, 0);
        givenBoard();

        assertBoard(
                "☺♥&  \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n", game1);

        when(dice.next(anyInt())).thenReturn(Direction.LEFT.value);
        bomberman2.right();
        tick();

        assertBoard(
                "☺&♣  \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n", game1);

        assertBoard(
                "♥&Ѡ  \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n", game2);

        verifyNoMoreInteractions(listener1);
        verify(listener2, only()).event(BombermanEvents.KILL_BOMBERMAN.name());
    }

    private void meatChopperAt(Dice dice, int x, int y) {
        when(dice.next(anyInt())).thenReturn(x, y);
        walls = new MeatChoppers(new WallsImpl(), SIZE, 1, dice);
    }

    //  бомбермены не могут ходить по бомбам ни по своим ни по чужим
    @Test
    public void shouldBombermanCantGoToBombFromAnotherBomberman() {
        givenBoard();

        bomberman2.act();
        bomberman2.right();
        tick();
        bomberman2.right();
        bomberman1.right();
        tick();

        assertBoard(
                "☺3 ♥ \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n", game1);

        bomberman2.left();
        tick();
        bomberman2.left();
        tick();

        assertBoard(
                "☺1♥  \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n", game1);
    }

    @Test
    public void shouldBombKillAllBomberman() {
        shouldBombermanCantGoToBombFromAnotherBomberman();

        tick();
        assertBoard(
                "Ѡ҉♣  \n" +
                " ҉   \n" +
                "     \n" +
                "     \n" +
                "     \n", game1);

        assertBoard(
                "♣҉Ѡ  \n" +
                " ҉   \n" +
                "     \n" +
                "     \n" +
                "     \n", game2);
    }

    @Test
    public void shouldNewGamesWhenKillAll() {
        shouldBombKillAllBomberman();
        when(settings.getBomberman(any(Level.class))).thenReturn(new MyBomberman(level), new MyBomberman(level));

        game1.newGame();
        game2.newGame();
        tick();
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

    // на поле можно чтобы каждый поставил то количество бомб которое ему позволено и не более того
    @Test
    public void shouldTwoBombsOnBoard() {
        bombsCount = 1;

        givenBoard();

        bomberman1.act();
        bomberman1.down();

        bomberman2.act();
        bomberman2.down();

        tick();

        assertBoard(
                "44   \n" +
                "☺♥   \n" +
                "     \n" +
                "     \n" +
                "     \n", game1);

        bomberman1.act();
        bomberman1.down();

        bomberman2.act();
        bomberman2.down();

        tick();

        assertBoard(
                "33   \n" +
                "     \n" +
                "☺♥   \n" +
                "     \n" +
                "     \n", game1);

    }

    @Test
    public void shouldFourBombsOnBoard() {
        bombsCount = 2;

        givenBoard();

        bomberman1.act();
        bomberman1.down();

        bomberman2.act();
        bomberman2.down();

        tick();

        assertBoard(
                "44   \n" +
                "☺♥   \n" +
                "     \n" +
                "     \n" +
                "     \n", game1);

        bomberman1.act();
        bomberman1.down();

        bomberman2.act();
        bomberman2.down();

        tick();

        assertBoard(
                "33   \n" +
                "44   \n" +
                "☺♥   \n" +
                "     \n" +
                "     \n", game1);

        bomberman1.act();
        bomberman1.down();

        bomberman2.act();
        bomberman2.down();

        tick();

        assertBoard(
                "22   \n" +
                "33   \n" +
                "     \n" +
                "☺♥   \n" +
                "     \n", game1);

    }

    @Test
    public void shouldFourBombsOnBoard_checkTwoBombsPerBomberman() {
        bombsCount = 2;

        givenBoard();

        bomberman1.act();
        bomberman1.down();

        tick();

        assertBoard(
                "4♥   \n" +
                "☺    \n" +
                "     \n" +
                "     \n" +
                "     \n", game1);

        bomberman1.act();
        bomberman1.down();

        tick();

        assertBoard(
                "3♥   \n" +
                "4    \n" +
                "☺    \n" +
                "     \n" +
                "     \n", game1);

        bomberman1.act();
        bomberman1.down();

        tick();

        assertBoard(
                "2♥   \n" +
                "3    \n" +
                "     \n" +
                "☺    \n" +
                "     \n", game1);
    }

    @Test
    public void shouldFireEventWhenKillWallOnlyForOneBomberman() {
        walls = new BoardTest.DestroyWallAt(0, 0, new WallsImpl());
        givenBoard();

        bomberman1.act();
        bomberman1.right();
        bomberman2.down();
        tick();
        bomberman1.right();
        bomberman2.down();
        tick();
        bomberman1.right();
        bomberman2.down();
        tick();
        tick();
        tick();

        assertBoard(
                "H҉҉ ☺\n" +
                " ҉   \n" +
                "     \n" +
                "     \n" +
                " ♥   \n", game1);

        verify(listener1).event(BombermanEvents.KILL_DESTROY_WALL.name());
        verifyNoMoreInteractions(listener2);
    }

    @Test
    public void shouldFireEventWhenKillMeatChopper() {
        walls = new MeatChopperAt(0, 0, new WallsImpl());
        givenBoard();

        bomberman1.act();
        bomberman1.right();
        bomberman2.down();
        tick();
        bomberman1.right();
        bomberman2.down();
        tick();
        bomberman1.right();
        bomberman2.down();
        tick();
        tick();
        tick();

        assertBoard(
                "x҉҉ ☺\n" +
                " ҉   \n" +
                "     \n" +
                "     \n" +
                " ♥   \n", game1);

        verify(listener1).event(BombermanEvents.KILL_MEAT_CHOPPER.name());
        verifyNoMoreInteractions(listener2);
    }
}
