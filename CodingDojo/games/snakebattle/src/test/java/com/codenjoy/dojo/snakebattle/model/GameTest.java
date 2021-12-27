package com.codenjoy.dojo.snakebattle.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.snakebattle.TestGameSettings;
import com.codenjoy.dojo.snakebattle.model.board.SnakeBoard;
import com.codenjoy.dojo.snakebattle.model.hero.Hero;
import com.codenjoy.dojo.snakebattle.services.Event;
import com.codenjoy.dojo.snakebattle.services.GameSettings;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.utils.events.EventsListenersAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class GameTest {

    private SnakeBoard game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer;
    private GameSettings settings;
    private EventsListenersAssert events;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        settings = new TestGameSettings();
        printer = new PrinterFactoryImpl();
        events = new EventsListenersAssert(() -> Arrays.asList(listener), Event.class);
    }

    @After
    public void after() {
        verifyAllEvents("");
    }

    private void givenFl(String board) {
        given(board);
        assertE(board);
    }

    private void given(String board) {
        Level level = new Level(board);
        game = new SnakeBoard(level, dice, settings);
        hero = level.hero(game);
        listener = mock(EventListener.class);
        player = new Player(listener, settings);
        player.setHero(hero);
        game.newGame(player);
        if (hero == null) {
            hero = game.getHeroes().get(0);
        }
        hero.setActive(true);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    public void verifyAllEvents(String expected) {
        assertEquals(expected, events.getEvents());
    }

    // карта со своей змейкой
    @Test
    public void shouldSnakeOnBoard() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // старт змейки из "стартового бокса"
    @Test
    public void shouldGetOutFromStartPoint() {
        given("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼#     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "╘►     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼╘►    ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼#╘►   ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");
    }

    // карта с яблоками, камнями, пилюлями полёта, пилюлями ярости, деньгами
    @Test
    public void shouldBoardWithElements() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼ $ ® ☼" +
                "☼  ●  ☼" +
                "☼  © ○☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼ $ ® ☼" +
                "☼  ●  ☼" +
                "☼  © ○☼" +
                "☼☼☼☼☼☼☼");
    }

    // тест событий
    @Test
    public void shouldGoldEvent_whenEatIt() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼╘►$  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼╘►$  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents("[GOLD]");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // тест событий
    @Test
    public void shouldAppleEvent_whenEatIt() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼╘►○  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼╘►○  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents("[APPLE]");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼╘═►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldStoneAndDieEvents_whenEatStone_lengthIsTooShort() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼╘►●  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼╘►●  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents("[DIE]");

        assertEquals(false, hero.isAlive());
        assertEquals(true, hero.isActive());

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ╘☻  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertEquals(false, hero.isAlive());
        assertEquals(true, hero.isActive());
    }

    @Test
    public void shouldStoneAndAlive_whenEatStone_lengthIsOk() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼╔═══╗☼" +
                "☼╚═╗╘╝☼" +
                "☼  ▼  ☼" +
                "☼  ●  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼╔═══╗☼" +
                "☼╚═╗╘╝☼" +
                "☼  ▼  ☼" +
                "☼  ●  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents("[STONE]");

        assertEquals(true, hero.isAlive());
        assertEquals(true, hero.isActive());

        assertE("☼☼☼☼☼☼☼" +
                "☼╔═╕  ☼" +
                "☼╚═╗  ☼" +
                "☼  ║  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼╔╕   ☼" +
                "☼╚═╗  ☼" +
                "☼  ║  ☼" +
                "☼  ║  ☼" +
                "☼  ▼  ☼" +
                "☼☼☼☼☼☼☼");

        assertEquals(true, hero.isAlive());
        assertEquals(true, hero.isActive());
    }

    // тест продолжения движения без дополнительных указаний
    @Test
    public void shouldMoveByInertia_whenNoCommand_directionLeftToRight() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼╘►   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ╘► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ╘►☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldMoveByInertia_whenNoCommand_directionRightToLeft() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ◄╕☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ◄╕ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ◄╕  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼◄╕   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldMoveByInertia_whenNoCommand_directionUpToDown() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼  ╓  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ╓  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ╓  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ╓  ☼" +
                "☼  ▼  ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldMoveByInertia_whenNoCommand_directionDownToUp() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ▲  ☼" +
                "☼  ╙  ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ▲  ☼" +
                "☼  ╙  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ▲  ☼" +
                "☼  ╙  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼  ▲  ☼" +
                "☼  ╙  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // тесты движения в заданную сторону
    @Test
    public void shouldTurnDown() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ╘► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ╓ ☼" +
                "☼   ▼ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldTurnRight() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ╓  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ╘► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldTurnUp() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ╘► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼   ▲ ☼" +
                "☼   ╙ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldTurnLeft() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ╓  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ◄╕  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // тест роста
    @Test
    public void shouldGrow_whenEatApple() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼╘►○ ○ ○  ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents("[APPLE]");

        assertE("☼☼☼☼☼☼☼☼☼☼☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼╘═► ○ ○  ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼☼☼☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼ ╘═►○ ○  ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents("[APPLE]");

        assertE("☼☼☼☼☼☼☼☼☼☼☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼ ╘══► ○  ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼☼☼☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼  ╘══►○  ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents("[APPLE]");

        assertE("☼☼☼☼☼☼☼☼☼☼☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼  ╘═══►  ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼☼☼☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼   ╘═══► ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼");
    }

    // тест смерти об стену
    @Test
    public void shouldDie_whenEatWall() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ╘═► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ╘═►☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents("[DIE]");

        assertEquals(false, hero.isAlive());
        assertEquals(true, hero.isActive());

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ╘═☻" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertEquals(false, hero.isAlive());
        assertEquals(true, hero.isActive());
    }

    // пока змея не активна, её направление "последнего движения" не меняется
    @Test
    public void shouldStopAndDontMove_whenNotActive() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ╘═► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.setActive(false);
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ~═& ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ~═& ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        hero.setActive(true);
        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ╘═►☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // змея не может разворачиваться в противоположную сторону
    @Test
    public void shouldCantMovingBack_fromRightToLeft() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ╘► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldCantMovingBack_fromLeftToRight() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ◄╕ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ◄╕  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldCantMovingBack_fromUpToDown() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ▲  ☼" +
                "☼  ╙  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ▲  ☼" +
                "☼  ╙  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldCantMovingBack_fromDownToUp() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ╓  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ╓  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldTurnClockwise() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘═► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ╘╗ ☼" +
                "☼   ▼ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");


        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼   ╓ ☼" +
                "☼   ║ ☼" +
                "☼   ▼ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ╓ ☼" +
                "☼  ◄╝ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ◄═╕ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ▲   ☼" +
                "☼ ╚╕  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ▲   ☼" +
                "☼ ║   ☼" +
                "☼ ╙   ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╔►  ☼" +
                "☼ ╙   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘═► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldTurnContrClockwise() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ╘═► ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ▲ ☼" +
                "☼  ╘╝ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼   ▲ ☼" +
                "☼   ║ ☼" +
                "☼   ╙ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ◄╗ ☼" +
                "☼   ╙ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ◄═╕ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╔╕  ☼" +
                "☼ ▼   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╓   ☼" +
                "☼ ║   ☼" +
                "☼ ▼   ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ╓   ☼" +
                "☼ ╚►  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ╘═► ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // съедая пилюлю полёта, змейка перелетает камни
    @Test
    public void shouldFlyingOverStones_whenEatFlyingPill() {
        givenFl("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼╘►© ●  ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        assertEquals(false, hero.isFlying());

        game.tick();

        verifyAllEvents("[FLYING]");

        assertEquals(10, hero.getFlyingCount());
        assertEquals(true, hero.isFlying());

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼ ╘♠ ●  ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        game.tick();

        assertEquals(9, hero.getFlyingCount());
        assertEquals(true, hero.isFlying());

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼  ╘♠●  ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        game.tick();

        assertEquals(8, hero.getFlyingCount());
        assertEquals(0, hero.getFuryCount());
        assertEquals(0, hero.getStonesCount());
        assertEquals(true, hero.isFlying());
        assertEquals(true, hero.isAlive());

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼   ╘♠  ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        game.tick();
        game.tick();

        assertEquals(6, hero.getFlyingCount());
        assertEquals(0, hero.getStonesCount());
        assertEquals(true, hero.isFlying());
        assertEquals(true, hero.isAlive());

        // камень остался на месте
        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼    ●╘♠☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldDisableFlyingPillEffect_when10Ticks() {
        shouldFlyingOverStones_whenEatFlyingPill();

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼    ●╘♠☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        hero.up();
        game.tick();
        game.tick();
        game.tick();

        assertEquals(3, hero.getFlyingCount());
        assertEquals(true, hero.isFlying());

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼      ♠☼" +
                "☼      ╙☼" +
                "☼       ☼" +
                "☼    ●  ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        hero.left();
        game.tick();
        game.tick();

        assertEquals(1, hero.getFlyingCount());
        assertEquals(true, hero.isFlying());

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼    ♠╕ ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼    ●  ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        game.tick();

        assertEquals(0, hero.getFlyingCount());
        assertEquals(false, hero.isFlying());

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼   ◄╕  ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼    ●  ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");
    }

    // съедая пилюлю полёта, змейка может летать над собой
    @Test
    public void shouldFlyingOverMyself_whenEatFlyingPill() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼╓    ☼" +
                "☼║    ☼" +
                "☼╚═══╗☼" +
                "☼  ©◄╝☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents("[FLYING]");

        assertEquals(9, hero.size());

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼╓    ☼" +
                "☼╚═══╗☼" +
                "☼  ♠═╝☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.up();
        game.tick();

        // змея не укоротилась
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼╘═♠═╗☼" +
                "☼  ╚═╝☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertEquals(9, hero.size());

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ♠  ☼" +
                "☼ ╘║═╗☼" +
                "☼  ╚═╝☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼  ♠  ☼" +
                "☼  ║  ☼" +
                "☼  ║═╗☼" +
                "☼  ╚═╝☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼ ♠╗  ☼" +
                "☼  ║  ☼" +
                "☼  ║╘╗☼" +
                "☼  ╚═╝☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // съедая пилюлю ярости, змейка ест камни без ущерба
    @Test
    public void shouldEatStones_whenEatFuryPill() {
        givenFl("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼╘►® ●  ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        assertEquals(false, hero.isFury());

        game.tick();

        verifyAllEvents("[FURY]");

        assertEquals(10, hero.getFuryCount());
        assertEquals(true, hero.isFury());

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼ ╘♥ ●  ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        game.tick();

        assertEquals(9, hero.getFuryCount());
        assertEquals(0, hero.getStonesCount());
        assertEquals(true, hero.isFury());

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼  ╘♥●  ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents("[STONE]");

        assertEquals(8, hero.getFuryCount());
        assertEquals(0, hero.getFlyingCount());
        assertEquals(1, hero.getStonesCount());
        assertEquals(true, hero.isFury());
        assertEquals(true, hero.isAlive());

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼   ╘♥  ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        game.tick();
        game.tick();

        assertEquals(6, hero.getFuryCount());
        assertEquals(1, hero.getStonesCount());
        assertEquals(true, hero.isFury());
        assertEquals(true, hero.isAlive());

        // камень пропал
        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼     ╘♥☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldDropStone_afterFury() {
        shouldEatStones_whenEatFuryPill();

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼     ╘♥☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        assertEquals(1, hero.getStonesCount());

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼      ♥☼" +
                "☼      ╙☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");


        hero.act();
        game.tick();

        assertEquals(0, hero.getStonesCount());

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼      ♥☼" +
                "☼      ╙☼" +
                "☼      ●☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼      ♥☼" +
                "☼      ╙☼" +
                "☼       ☼" +
                "☼      ●☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldDisableFuryPillEffect_when10Ticks() {
        shouldEatStones_whenEatFuryPill();

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼     ╘♥☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        hero.up();
        game.tick();
        game.tick();
        game.tick();

        assertEquals(3, hero.getFuryCount());
        assertEquals(true, hero.isFury());

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼      ♥☼" +
                "☼      ╙☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        hero.left();
        game.tick();
        game.tick();

        assertEquals(1, hero.getFuryCount());
        assertEquals(true, hero.isFury());

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼    ♥╕ ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        game.tick();

        assertEquals(0, hero.getFuryCount());
        assertEquals(false, hero.isFury());

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼   ◄╕  ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldDropStone_afterFuryDisabled() {
        shouldDisableFuryPillEffect_when10Ticks();

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼   ◄╕  ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        assertEquals(1, hero.getStonesCount());

        hero.act();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼  ◄╕●  ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");


        assertEquals(0, hero.getStonesCount());

        game.tick();

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼ ◄╕ ●  ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");
    }

    // разворот на 180 короткой змейки невозможен
    @Test
    public void shouldTurn180_whenLengthIs2() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼╘►   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.up(); // раньше так срабатывало
        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // разворот на 180 с откусыванием хвоста невозможен
    @Test
    public void shouldTurn180_whenLengthIs5() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼╘═══►☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.down(); // раньше так срабатывало
        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ╘═══☻" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        verifyAllEvents("[DIE]");
    }

}
