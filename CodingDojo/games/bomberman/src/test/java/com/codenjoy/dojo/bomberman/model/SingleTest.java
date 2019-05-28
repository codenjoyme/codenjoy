package com.codenjoy.dojo.bomberman.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.bomberman.services.Events;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;

import static com.codenjoy.dojo.bomberman.model.BombermanTest.DestroyWallAt;
import static com.codenjoy.dojo.bomberman.model.BombermanTest.MeatChopperAt;
import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SingleTest {

    public static final int SIZE = 5;
    private Game game2;
    private Walls walls = emptyWalls();
    private Hero bomberman2;
    private Hero bomberman1;
    private GameSettings settings;
    private Level level;
    private Game game1;
    private Bomberman board;
    private EventListener listener1;
    private EventListener listener2;
    private int bombsCount = 1;
    private Dice meatChopperDice;
    private Dice bombermanDice;
    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    public void givenBoard() {
        settings = mock(GameSettings.class);

        level = mock(Level.class);
        when(level.bombsCount()).thenReturn(bombsCount);
        when(level.bombsPower()).thenReturn(1);

        bombermanDice = mock(Dice.class);

        dice(bombermanDice,  0, 0);
        bomberman1 = new Hero(level, bombermanDice);
        dice(bombermanDice,  0, 0);
        bomberman2 = new Hero(level, bombermanDice);
        when(settings.getBomberman(any(Level.class))).thenReturn(bomberman1, bomberman2);

        when(settings.getLevel()).thenReturn(level);
        when(settings.getBoardSize()).thenReturn(v(SIZE));
        when(settings.getWalls(any(Bomberman.class))).thenReturn(walls);

        board = new Bomberman(settings);

        listener1 = mock(EventListener.class);
        listener2 = mock(EventListener.class);

        game1 = new Single(new Player(listener1), printerFactory);
        game1.on(board);
        game2 = new Single(new Player(listener2), printerFactory);
        game2.on(board);

        game1.newGame();
        game2.newGame();
    }

    private void dice(Dice dice, int... values) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }

    private Walls emptyWalls() {
        Walls walls = mock(WallsImpl.class);
        when(walls.iterator()).thenReturn(new LinkedList<Wall>().iterator());
        return walls;
    }

    @Test
    public void shouldGameReturnsRealJoystick() {
        givenBoard();
        bomberman1.act();
        bomberman2.up();
        tick();
        bomberman2.up();
        tick();
        tick();
        tick();
        tick();

        assertFalse(bomberman1.isAlive());
        assertTrue(bomberman2.isAlive());

        Joystick joystick1 = game1.getJoystick();
        Joystick joystick2 = game1.getJoystick();

        // when
        game1.newGame();
        game2.newGame();

        // then
        assertNotSame(joystick1, game1.getJoystick());
        assertNotSame(joystick2, game1.getJoystick());
    }

    @Test
    public void shouldGetTwoBombermansOnBoard() {
        givenBoard();

        assertSame(bomberman1, game1.getJoystick());
        assertSame(bomberman2, game2.getJoystick());

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♥   \n", game1);

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♥☺   \n", game2);
    }

    @Test
    public void shouldOnlyOneListenerWorksWhenOneBombermanKillAnother() {
        givenBoard();

        bomberman1.act();
        bomberman1.up();
        tick();
        bomberman1.up();
        tick();
        tick();
        tick();
        tick();

        assertBoard(
                "     \n" +
                "     \n" +
                "☺    \n" +
                "҉    \n" +
                "҉♣   \n", game1);

        verify(listener1, only()).event(Events.KILL_OTHER_BOMBERMAN);
        verify(listener2, only()).event(Events.KILL_BOMBERMAN);
    }

    private void tick() {
        board.tick();
    }

    @Test
    public void shouldPrintOtherBombBomberman() {
        givenBoard();

        bomberman1.act();
        bomberman1.up();

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☻♥   \n", game1);

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♠☺   \n", game2);
    }

    @Test
    public void shouldBombermanCantGoToAnotherBomberman() {
        givenBoard();

        bomberman1.right();
        tick();

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♥   \n", game1);
    }

    private void assertBoard(String board, Game game) {
        assertEquals(board, game.getBoardAsString());
    }

    // бомбермен может идти на митчопера, при этом он умирает
    @Test
    public void shouldKllOtherBombermanWhenBombermanGoToMeatChopper() {
        walls = new MeatChopperAt(2, 0, new WallsImpl());
        givenBoard();

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♥&  \n", game1);

        bomberman2.right();
        tick();
        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺ ♣  \n", game1);

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♥ Ѡ  \n", game2);

        verifyNoMoreInteractions(listener1);
        verify(listener2, only()).event(Events.KILL_BOMBERMAN);
    }

    // если митчопер убил другого бомбермена, как это на моей доске отобразится? Хочу видеть трупик
    @Test
    public void shouldKllOtherBombermanWhenMeatChopperGoToIt() {
        meatChopperAt(2, 0);
        givenBoard();

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♥&  \n", game1);

        dice(meatChopperDice, Direction.LEFT.value());
        tick();

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♣   \n", game1);

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♥Ѡ   \n", game2);

        verifyNoMoreInteractions(listener1);
        verify(listener2, only()).event(Events.KILL_BOMBERMAN);
    }

    // А что если бомбермен идет на митчопера а тот идет на встречу к нему - бомбермен проскочит или умрет? должен умереть!
    @Test
    public void shouldKllOtherBombermanWhenMeatChopperAndBombermanMoves() {
        meatChopperAt(2, 0);
        givenBoard();

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♥&  \n", game1);

        dice(meatChopperDice, Direction.LEFT.value());
        bomberman2.right();
        tick();

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺&♣  \n", game1);

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♥&Ѡ  \n", game2);

        verifyNoMoreInteractions(listener1);
        verify(listener2, only()).event(Events.KILL_BOMBERMAN);
    }

    private void meatChopperAt(int x, int y) {
        meatChopperDice = mock(Dice.class);
        dice(meatChopperDice, x, y);
        Field temp = mock(Field.class);
        when(temp.size()).thenReturn(SIZE);
        MeatChoppers meatchoppers = new MeatChoppers(new WallsImpl(), temp, v(1), meatChopperDice);
        meatchoppers.regenerate();
        walls = meatchoppers;
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
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺3 ♥ \n", game1);

        bomberman2.left();
        tick();
        bomberman2.left();
        tick();

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺1♥  \n", game1);
    }

    @Test
    public void shouldBombKillAllBomberman() {
        shouldBombermanCantGoToBombFromAnotherBomberman();

        tick();
        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "Ѡ҉♣  \n", game1);

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "♣҉Ѡ  \n", game2);
    }

    @Test
    public void shouldNewGamesWhenKillAll() {
        shouldBombKillAllBomberman();
        when(settings.getBomberman(any(Level.class))).thenReturn(new Hero(level, bombermanDice), new Hero(level, bombermanDice));

        game1.newGame();
        game2.newGame();
        tick();
        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♥   \n", game1);

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♥☺   \n", game2);
    }

    // на поле можно чтобы каждый поставил то количество бомб которое ему позволено и не более того
    @Test
    public void shouldTwoBombsOnBoard() {
        bombsCount = 1;

        givenBoard();

        bomberman1.act();
        bomberman1.up();

        bomberman2.act();
        bomberman2.up();

        tick();

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♥   \n" +
                "44   \n", game1);

        bomberman1.act();
        bomberman1.up();

        bomberman2.act();
        bomberman2.up();

        tick();

        assertBoard(
                "     \n" +
                "     \n" +
                "☺♥   \n" +
                "     \n" +
                "33   \n", game1);

    }

    @Test
    public void shouldFourBombsOnBoard() {
        bombsCount = 2;

        givenBoard();

        bomberman1.act();
        bomberman1.up();

        bomberman2.act();
        bomberman2.up();

        tick();

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♥   \n" +
                "44   \n", game1);

        bomberman1.act();
        bomberman1.up();

        bomberman2.act();
        bomberman2.up();

        tick();

        assertBoard(
                "     \n" +
                "     \n" +
                "☺♥   \n" +
                "44   \n" +
                "33   \n", game1);

        bomberman1.act();
        bomberman1.up();

        bomberman2.act();
        bomberman2.up();

        tick();

        assertBoard(
                "     \n" +
                "☺♥   \n" +
                "     \n" +
                "33   \n" +
                "22   \n", game1);

    }

    @Test
    public void shouldFourBombsOnBoard_checkTwoBombsPerBomberman() {
        bombsCount = 2;

        givenBoard();

        bomberman1.act();
        bomberman1.up();

        tick();

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                "☺    \n" +
                "4♥   \n", game1);

        bomberman1.act();
        bomberman1.up();

        tick();

        assertBoard(
                "     \n" +
                "     \n" +
                "☺    \n" +
                "4    \n" +
                "3♥   \n", game1);

        bomberman1.act();
        bomberman1.up();

        tick();

        assertBoard(
                "     \n" +
                "☺    \n" +
                "     \n" +
                "3    \n" +
                "2♥   \n", game1);
    }

    @Test
    public void shouldFireEventWhenKillWallOnlyForOneBomberman() {
        walls = new DestroyWallAt(0, 0, new WallsImpl());
        givenBoard();

        bomberman1.act();
        bomberman1.right();
        bomberman2.up();
        tick();
        bomberman1.right();
        bomberman2.up();
        tick();
        bomberman1.right();
        bomberman2.up();
        tick();
        tick();
        tick();

        assertBoard(
                " ♥   \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "H҉҉ ☺\n", game1);

        verify(listener1).event(Events.KILL_DESTROY_WALL);
        verifyNoMoreInteractions(listener2);
    }

    @Test
    public void shouldFireEventWhenKillMeatChopper() {
        walls = new MeatChopperAt(0, 0, new WallsImpl());
        givenBoard();

        bomberman1.act();
        bomberman1.right();
        bomberman2.up();
        tick();
        bomberman1.right();
        bomberman2.up();
        tick();
        bomberman1.right();
        bomberman2.up();
        tick();
        tick();
        tick();

        assertBoard(
                " ♥   \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "x҉҉ ☺\n", game1);

        verify(listener1).event(Events.KILL_MEAT_CHOPPER);
        verifyNoMoreInteractions(listener2);
    }

    @Test
    public void bug() {
        walls = new DestroyWallAt(0, 0, new MeatChopperAt(1, 0, new MeatChopperAt(2, 0, new WallsImpl())));
        givenBoard();

        assertBoard(
                "     \n" +
                "     \n" +
                "     \n" +
                " ☺♥  \n" +
                "#&&  \n", game1);

        bomberman1.act();
        bomberman1.up();
        bomberman2.act();
        bomberman2.up();
        tick();
        bomberman1.left();
        bomberman2.right();
        tick();
        tick();
        tick();
        tick();

        assertBoard(
                "     \n" +
                "     \n" +
                "☺҉҉♥ \n" +
                "҉҉҉҉ \n" +
                "#xx  \n", game1);

        verify(listener1, only()).event(Events.KILL_MEAT_CHOPPER);
        verify(listener2, only()).event(Events.KILL_MEAT_CHOPPER);
    }
}
