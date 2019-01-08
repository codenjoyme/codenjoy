package com.codenjoy.dojo.snakebattle.model.board;

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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.snakebattle.model.Player;
import com.codenjoy.dojo.snakebattle.model.hero.Hero;
import com.codenjoy.dojo.snakebattle.model.level.LevelImpl;
import com.codenjoy.dojo.snakebattle.services.Events;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Kors
 */
public class SnakeBoardTest {

    private SnakeBoard game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void givenFl(String board) {
        given(board);
        assertE(board);
    }

    private void given(String board) {
        LevelImpl level = new LevelImpl(board);
        Hero hero = level.getHero();

        game = new SnakeBoard(level, dice);
        game.debugMode = true;
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        if (hero != null) {
            player.setHero(hero);
            hero.init(game);
        }
        this.hero = game.getHeroes().get(0);
        this.hero.setActive(true);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    // карта со своей змейкой
    @Test
    public void shouldSnakeOnBoard() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ →►  ☼" +
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
                "→►     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼→►    ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼#→►   ☼" +
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
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼ $ @ ☼" +
                "☼  ●  ☼" +
                "☼  % ○☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼ $ @ ☼" +
                "☼  ●  ☼" +
                "☼  % ○☼" +
                "☼☼☼☼☼☼☼");
    }

    // тест событий
    @Test
    public void shouldGoldEvent_whenEatIt() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼→►$  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼→►$  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        verify(listener).event(Events.GOLD);

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ →►  ☼" +
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
                "☼→►○  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼→►○  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        verify(listener).event(Events.APPLE);

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼→═►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldStoneAndDieEvents_whenEatStone_lengthIsTooShort() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼→►●  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼→►●  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        verify(listener).event(Events.STONE);
        verify(listener).event(Events.DIE);
        assertEquals(false, hero.isAlive());
        assertEquals(true, hero.isActive());

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ →☻  ☼" +
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

    // тест продолжения движения без дополнительных указаний
    @Test
    public void shouldMoveByInertia_whenNoCommand_directionLeftToRight() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼→►   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  →► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   →►☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldMoveByInertia_whenNoCommand_directionRightToLeft() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ◄←☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ◄← ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ◄←  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼◄←   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldMoveByInertia_whenNoCommand_directionUpToDown() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼  ↓  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
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
                "☼  ↑  ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ▲  ☼" +
                "☼  ↑  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ▲  ☼" +
                "☼  ↑  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼  ▲  ☼" +
                "☼  ↑  ☼" +
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
                "☼  →► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ↓ ☼" +
                "☼   ▼ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldTurnRight() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  →► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldTurnUp() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  →► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼   ▲ ☼" +
                "☼   ↑ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldTurnLeft() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ◄←  ☼" +
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
                "☼→►○ ○ ○  ☼" +
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
                "☼→═► ○ ○  ☼" +
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
                "☼ →═►○ ○  ☼" +
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
                "☼ →══► ○  ☼" +
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
                "☼  →══►○  ☼" +
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
                "☼  →═══►  ☼" +
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
                "☼   →═══► ☼" +
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
                "☼ →═► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  →═►☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        verify(listener).event(Events.DIE);
        assertEquals(false, hero.isAlive());
        assertEquals(true, hero.isActive());

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   →═☻" +
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
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.setActive(false);
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ᓕ&  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ᓕ&  ☼" +
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
                "☼  →► ☼" +
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
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  →► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldCantMovingBack_fromLeftToRight() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ◄← ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ◄←  ☼" +
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
                "☼  ↑  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ▲  ☼" +
                "☼  ↑  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldCantMovingBack_fromDownToUp() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // съедая пилюлю полёта, змейка перелетает камни
    @Test
    public void flyingOverStones() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ →►% ☼" +
                "☼   ● ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();
        assertEquals("Змейка не съела пилюлю полёта!", 10, hero.getFlyingCount());
        hero.down();
        game.tick();
        assertEquals("Змейка съела камень в полёте!", 0, hero.getStonesCount());
        assertTrue("Змейка умерла от камня в полёте!", hero.isAlive());
        game.tick();
        game.tick();

        // камень остался на месте
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ● ☼" +
                "☼   ↓ ☼" +
                "☼   ♠ ☼" +
                "☼☼☼☼☼☼☼");
    }

    // съедая пилюлю полёта, змейка может летать над собой
    @Test
    public void flyingOverMyself() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼→►○○○☼" +
                "☼ %○○○☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        // удлиннение
        game.tick();
        game.tick();
        game.tick();
        hero.down();
        game.tick();
        hero.left();
        game.tick();
        game.tick();
        game.tick();

        // закручиваемся
        hero.up();
        game.tick();
        hero.right();
        game.tick();
        hero.down();
        game.tick();
        hero.right();
        game.tick();
        hero.down();
        game.tick();
        game.tick();

        // змея не укоротилась
        assertE("☼☼☼☼☼☼☼" +
                "☼ ╔╗  ☼" +
                "☼ ╚←╗ ☼" +
                "☼   ║ ☼" +
                "☼   ♠ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertEquals("Змейка укоротила себя в полёте!", 8, hero.size());
    }

    // съедая пилюлю ярости, змейка ест камни без ущерба
    @Test
    public void furyEatingStones() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ →►@ ☼" +
                "☼   ● ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();
        assertEquals("Змейка не съела пилюлю ярости!", 10, hero.getFuryCount());
        hero.down();
        game.tick();
        assertTrue("Змейка умерла от камня при ярости!", hero.isAlive());
        assertEquals("Змейка не съела камень при ярости!", 1, hero.getStonesCount());
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ↓ ☼" +
                "☼   ♥ ☼" +
                "☼☼☼☼☼☼☼");
    }
}
