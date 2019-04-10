package com.codenjoy.dojo.hex.model;

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


import com.codenjoy.dojo.hex.services.Event;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class HexTest {

    private Hex game;
    private Dice dice;

    private EventListener listener1;
    private Hero hero1;
    private Player player1;
    private Joystick joystick1;

    private EventListener listener2;
    private Hero hero2;
    private Player player2;
    private Joystick joystick2;

    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
        game = new Hex(level, dice);

        hero1 = level.getHeroes().get(0);
        setupPlayer1();

        if (level.getHeroes().size() > 1) {
            hero2 = level.getHeroes().get(1);
            setupPlayer2();
        }
    }

    private void setupPlayer1() {
        listener1 = mock(EventListener.class);
        player1 = new Player(listener1);
        joystick1 = player1.getJoystick();

        dice(hero1.getX(), hero1.getY());

        game.newGame(player1);
    }

    private void setupPlayer2() {
        listener2 = mock(EventListener.class);
        player2 = new Player(listener2);
        joystick2 = player2.getJoystick();

        dice(hero2.getX(), hero2.getY());

        game.newGame(player2);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected), printerFactory.getPrinter(
                game.reader(), player1).print());
    }

    @Test
    public void shouldFieldAtStart() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☻ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldSplitUpWhenGoUp() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☻ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(2, 2);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼ ☺ ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldSplitDownWhenGoDown() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☻ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");


        joystick1.act(2, 2);
        joystick1.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼ ☺ ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldSplitLeftWhenGoLeft() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☻ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");


        joystick1.act(2, 2);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

    }

    @Test
    public void shouldSplitLeftAndRight() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☻ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");


        joystick1.act(2, 2);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(2, 2);
        joystick1.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldSplitLeftThenUp() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☻ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");


        joystick1.act(2, 2);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(1, 2);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☺  ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

    }

    @Test
    public void shouldHitTheWall() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☻ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");


        joystick1.act(2, 2);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(1, 2);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

    }

    @Test
    public void shouldHitTheHero() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☻ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");


        joystick1.act(2, 2);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(1, 2);
        joystick1.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        assertEquals(2, player1.getHeroes().size());

    }

    @Test
    public void shouldNotPickEmptyField() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☻ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(1, 1);

        joystick1.up();
        game.tick();

        joystick1.down();
        game.tick();

        joystick1.left();
        game.tick();

        joystick1.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldTwoPlayersOnBoard() {
        givenFl("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼   ☼" +
                "☼☻  ☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldSecondPlayerMoveDownAndFirstUp() {
        givenFl("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼   ☼" +
                "☼☻  ☼" +
                "☼☼☼☼☼");

        joystick2.act(3, 1);
        joystick2.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼  ♥☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        joystick1.act(1, 3);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼☺ ♥☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

    }

    @Test
    public void shouldSecondPlayerMoveDownAndFirstUpDuringOneTick() {
        givenFl("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼   ☼" +
                "☼☻  ☼" +
                "☼☼☼☼☼");

        joystick2.act(3, 1);
        joystick2.down();
        joystick1.act(1, 3);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼☺ ♥☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

    }

    @Test
    public void shouldAnnigilateWhenTwoPlayersMoveTowardsEachOther() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ♥☼" +
                "☼☻  ☼" +
                "☼☼☼☼☼");

        joystick1.act(1, 3);
        joystick1.right();
        joystick2.act(3, 2);
        joystick2.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ♥♥☼" +
                "☼☺☺ ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldDoNothingWhenTwoPlayersMoveTowardsEachOtherWhenBadY() {
        givenFl("☼☼☼☼☼" +
                "☼♥  ☼" +
                "☼   ☼" +
                "☼☻  ☼" +
                "☼☼☼☼☼");

        joystick1.act(1, 3);
        joystick1.right();
        joystick2.act(1, 1);
        joystick2.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼♥♥ ☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldDoNothingWhenTwoPlayersMoveTowardsEachOther2() {
        givenFl("☼☼☼☼☼☼" +
                "☼♥   ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☻   ☼" +
                "☼☼☼☼☼☼");

        joystick1.act(1, 4);
        joystick1.up();
        joystick2.act(1, 1);
        joystick2.down();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼♥   ☼" +
                "☼♥   ☼" +
                "☼☺   ☼" +
                "☼☺   ☼" +
                "☼☼☼☼☼☼");
    }

    @Test
    public void shouldKillWhenPlayerMoveTowardsStanding() {
        // given
        givenFl("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼   ☼" +
                "☼☻  ☼" +
                "☼☼☼☼☼");

        joystick2.act(3, 1);
        joystick2.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼  ♥☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        // when
        joystick1.act(1, 3);
        joystick1.right();
        game.tick();

        //then
        assertE("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼  ☺☼" +
                "☼☺☺ ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldCaptureManyEnemies() {
        // given
        givenFl("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼   ☼" +
                "☼☻  ☼" +
                "☼☼☼☼☼");

        joystick2.act(3, 1);
        joystick2.down();
        game.tick();
        joystick2.act(3, 2);
        joystick2.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼  ♥☼" +
                "☼☺ ♥☼" +
                "☼☼☼☼☼");

        // when
        joystick2.act(3, 1);
        joystick2.left();

        joystick1.act(1, 3);
        joystick1.right();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼ ♥♥☼" +
                "☼  ☺☼" +
                "☼☺☺☺☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldNotMoveOnTheSamePlace() {
        givenFl("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼   ☼" +
                "☼  ☻☼" +
                "☼☼☼☼☼");

        // when
        joystick2.act(3, 1);
        joystick2.down();
        joystick1.act(3, 3);
        joystick1.up();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼  ☼☼" +
                "☼  ☺☼" +
                "☼☼☼☼☼");

        assertEquals(1, player1.getHeroes().size());
        assertEquals(1, player2.getHeroes().size());
    }

    // начисляются очки за зажваченных героев
    // 1) я размножил и должен получить + за новое место
    @Test
    public void shouldScoreOnDuplicate() {
        givenFl("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        // when
        joystick1.act(3, 1);
        joystick1.down();
        game.tick();

        // then
        verify(listener1).event(new Event(Event.EventEnum.WIN, 1));

        assertE("☼☼☼☼☼" +
                "☼  ☺☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // 2) я захватил противника, у меня должно быть столько плюсов, сколько я захватил, а у напарника столько же минусов
    // 2.1) я захватил 1 фишку
    @Test
    public void shouldScoreOnCapture() {
        givenFl("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼   ☼" +
                "☼☻  ☼" +
                "☼☼☼☼☼");

        joystick2.act(3, 1);
        joystick2.down();
        game.tick();
        reset(listener1);
        reset(listener2);

        assertE("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼  ♥☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        // when
        joystick1.act(1, 3);
        joystick1.right();
        game.tick();

        // then
        verify(listener1).event(new Event(Event.EventEnum.WIN, 2));
        verify(listener2).event(new Event(Event.EventEnum.LOOSE, 1));

        assertE("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼  ☺☼" +
                "☼☺☺ ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldNotSaveLastActive() {
        givenFl("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼   ☼" +
                "☼☻  ☼" +
                "☼☼☼☼☼");

        joystick2.act(3, 1);
        joystick2.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼  ♥☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");


        joystick1.act(1, 3);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼☺ ♥☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        // when
        joystick1.act(2, 3);
        joystick1.right();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼☺ ♥☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");
    }

    // 2.2) я захватил 2 фишки
    @Test
    public void shouldScoreTwoOnCaptureTwo() {
        givenFl("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼   ☼" +
                "☼☻  ☼" +
                "☼☼☼☼☼");

        joystick2.act(3, 1);
        joystick2.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼  ♥☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");


        joystick1.act(1, 3);
        joystick1.up();
        game.tick();

        reset(listener1);
        reset(listener2);


        assertE("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼☺ ♥☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        // when
        joystick1.act(1, 2);
        joystick1.right();
        game.tick();



        assertE("☼☼☼☼☼" +
                "☼  ☺☼" +
                "☼☺☺☺☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        // then
        verify(listener1).event(new Event(Event.EventEnum.WIN, 3));
        verify(listener2).event(new Event(Event.EventEnum.LOOSE, 2));

    }

    @Test
    public void shouldNotMoveFromEmptySpace() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☻  ☼" +
                "☼☼☼☼☼");

        joystick1.act(2, 2);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");


    }

    // Новая игра должна давать новое поле
    @Test
    public void shouldStartNewGameOnGameOver() {
        shouldScoreTwoOnCaptureTwo();

        game.tick();

        assertFalse(player2.isAlive());
        assertFalse(player1.isAlive());

        dice(1, 1);
        game.newGame(player1);
        dice(3, 3);
        game.newGame(player2);

        assertE("☼☼☼☼☼" +
                "☼  ♥☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");
    }

    // можно ходить через 2 клеточки, при этом герой мувается + учитывать очки при захвате
    @Test
    public void shouldJumpUp() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☻  ☼" +
                "☼☼☼☼☼");

        joystick1.act(1, 3, 1);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        verifyNoMoreInteractions(listener1);
    }

    @Test
    public void shouldJumpDown() {
        givenFl("☼☼☼☼☼" +
                "☼☻  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(1, 1, 1);
        joystick1.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldJumpLeft() {
        givenFl("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(3, 1, 1);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldJumpRight() {
        givenFl("☼☼☼☼☼" +
                "☼☻  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(1, 1, 1);
        joystick1.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // герой не прыгает на ледующий шаг, если уже прыгал
    @Test
    public void shouldNoTwiceJump() {
        shouldJumpRight();
        assertE("☼☼☼☼☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(3, 1);
        joystick1.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☺☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // захват вражеских игроков, при прыжке
    @Test
    public void shouldGetOnJump() {
        givenFl("☼☼☼☼☼☼" +
                "☼   ♥☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☻ ☼" +
                "☼☼☼☼☼☼");

        joystick2.act(4, 1);
        joystick2.left();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼  ♥♥☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼☼☼☼☼☼");
        reset(listener1, listener2);

        // when
        joystick1.act(3, 4, 1);
        joystick1.up();
        game.tick();

        // then
        assertE("☼☼☼☼☼☼" +
                "☼  ☺☺☼" +
                "☼  ☺ ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        verify(listener1).event(new Event(Event.EventEnum.WIN, 2));
        verify(listener2).event(new Event(Event.EventEnum.LOOSE, 2));
    }

    @Test
    public void shouldGetOnJumpBlack() {
        givenFl("☼☼☼☼☼☼" +
                "☼   ♥☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☻ ☼" +
                "☼☼☼☼☼☼");

        // when
        joystick2.act(4, 1, 1);
        joystick2.down();
        game.tick();

        // then
        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼   ♥☼" +
                "☼  ♥ ☼" +
                "☼☼☼☼☼☼");

        verify(listener1).event(new Event(Event.EventEnum.LOOSE, 1));
        verify(listener2).event(new Event(Event.EventEnum.WIN, 1));
    }

    // геймовер, когда оба игрока jump нули и анигизировались
    @Test
    public void shouldBreakWhenJumpAtOnePlace() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼  ♥  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ☻  ☼" +
                "☼☼☼☼☼☼☼");

        // when
        joystick2.act(3, 1, 1);
        joystick2.down();

        joystick1.act(3, 5, 1);
        joystick1.up();
        game.tick();

        // then
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ☼  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        verify(listener1).event(new Event(Event.EventEnum.LOOSE, 1));
        verify(listener2).event(new Event(Event.EventEnum.LOOSE, 1));

        // when
        game.tick();

        // then
        assertFalse(player2.isAlive());
        assertFalse(player1.isAlive());
    }

    // если прыгаем друг к дружке впритык, то не захватываем территорий
    @Test
    public void shouldDoNothingWhenJumpAtOnePlace() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼  ♥   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ☻   ☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        joystick2.act(3, 1, 1);
        joystick2.down();

        joystick1.act(3, 6, 1);
        joystick1.up();
        game.tick();

        // then
        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ♥   ☼" +
                "☼  ☺   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyNoMoreInteractions(listener1, listener2);
    }

    // если ходим друг к дружке впритык, то не захватываем территорий
    @Test
    public void shouldDoNothingWhenMoveAtOnePlace() {
        givenFl("☼☼☼☼☼☼" +
                "☼  ♥ ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☻ ☼" +
                "☼☼☼☼☼☼");

        // when
        joystick2.act(3, 1);
        joystick2.down();

        joystick1.act(3, 4);
        joystick1.up();
        game.tick();

        // then
        assertE("☼☼☼☼☼☼" +
                "☼  ♥ ☼" +
                "☼  ♥ ☼" +
                "☼  ☺ ☼" +
                "☼  ☺ ☼" +
                "☼☼☼☼☼☼");

        verify(listener1).event(new Event(Event.EventEnum.WIN, 1));
        verify(listener2).event(new Event(Event.EventEnum.WIN, 1));
    }

    // геймовер, когда некуда больше ходить
    public void givenNoSpace() {
        givenFl("☼☼☼☼☼" +
                "☼ ♥ ☼" +
                "☼   ☼" +
                "☼ ☻ ☼" +
                "☼☼☼☼☼");

        joystick2.act(2, 1);
        joystick2.left();

        joystick1.act(2, 3);
        joystick1.left();
        game.tick();


        joystick2.act(2, 1);
        joystick2.right();

        joystick1.act(2, 3);
        joystick1.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼♥♥♥☼" +
                "☼   ☼" +
                "☼☺☺☺☼" +
                "☼☼☼☼☼");

        joystick2.act(1, 1);
        joystick2.down();

        joystick1.act(3, 3);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼♥☺☺☼" +
                "☼♥ ☺☼" +
                "☼♥♥☺☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldGameOverWhenNoSpace_whenGo() {
        givenNoSpace();
        assertE("☼☼☼☼☼" +
                "☼♥☺☺☼" +
                "☼♥ ☺☼" +
                "☼♥♥☺☼" +
                "☼☼☼☼☼");

        assertTrue(player2.isAlive());
        assertTrue(player1.isAlive());

        joystick2.act(2, 3);
        joystick2.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼♥♥♥☼" +
                "☼♥♥♥☼" +
                "☼♥♥♥☼" +
                "☼☼☼☼☼");

        assertTrue(player2.isAlive());
        assertTrue(player1.isAlive());

        // when
        game.tick();

        // then
        assertFalse(player1.isAlive());
        assertFalse(player2.isAlive());
    }

    @Test
    public void shouldGameOverWhenNoSpace_whenAnig() {
        givenNoSpace();
        assertE("☼☼☼☼☼" +
                "☼♥☺☺☼" +
                "☼♥ ☺☼" +
                "☼♥♥☺☼" +
                "☼☼☼☼☼");

        // when
        assertTrue(player2.isAlive());
        assertTrue(player1.isAlive());

        joystick2.act(2, 3);
        joystick2.up();

        joystick1.act(2, 1);
        joystick1.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼♥☺☺☼" +
                "☼♥☼☺☼" +
                "☼♥♥☺☼" +
                "☼☼☼☼☼");

        assertTrue(player2.isAlive());
        assertTrue(player1.isAlive());

        // when
        game.tick();

        // then
        assertFalse(player1.isAlive());
        assertFalse(player2.isAlive());
    }

    // нельзя ходить на место занятое другим игроком
    @Test
    public void shouldNoWayAtHero() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ♥ ☼" +
                "☼ ☻ ☼" +
                "☼☼☼☼☼");

        joystick1.act(2, 3);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ♥ ☼" +
                "☼ ☺ ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldNoWayAtHero2() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☻ ☼" +
                "☼☼☼☼☼");

        joystick1.act(2, 3);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼ ☺ ☼" +
                "☼☼☼☼☼");

        // when
        joystick1.act(2, 3);
        joystick1.up();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼ ☺ ☼" +
                "☼☼☼☼☼");

        assertEquals(2, game.getHeroes().size());
    }
}
