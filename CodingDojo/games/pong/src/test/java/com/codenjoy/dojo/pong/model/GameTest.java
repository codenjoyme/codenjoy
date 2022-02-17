package com.codenjoy.dojo.pong.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.pong.TestGameSettings;
import com.codenjoy.dojo.pong.services.Event;
import com.codenjoy.dojo.pong.services.GameSettings;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.QDirection;
import com.codenjoy.dojo.services.dice.MockDice;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class GameTest {

    private Pong game;
    private MockDice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer;
    private Hero hero;
    private GameSettings settings;

    @Before
    public void setup() {
        dice = new MockDice();
        printer = new PrinterFactoryImpl();
        settings = new TestGameSettings();
    }

    private void dice(Integer... next) {
        dice.then(next);
    }

    private void givenFl(String board) {
        Level level = new Level(board);
        game = new Pong(level, dice, settings);

        if (!level.hero().isEmpty()) {
            hero = level.hero().get(0);
            listener = mock(EventListener.class);
            player = new Player(listener, settings);
            game.newGame(player);
            player.setHero(hero);
            hero.init(game);
        }
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldFieldAtStart() {
        givenFl("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");

        assertE("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
    }

    @Test
    public void shouldBallDownOnTick() {
        givenFl("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
        game.setBallDirection(new BallDirection(QDirection.DOWN));
        game.tick();

        assertE("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
    }

    @Test
    public void shouldBallUpLeftTick() {
        givenFl("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
        game.setBallDirection(new BallDirection(QDirection.LEFT));
        game.tick();

        assertE("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|   o    |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
    }

    @Test
    public void shouldBallRightOnTick() {
        givenFl("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
        game.setBallDirection(new BallDirection(QDirection.RIGHT));
        game.tick();

        assertE("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|     o  |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
    }

    @Test
    public void shouldBallUpOnTick() {
        givenFl("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
        game.setBallDirection(new BallDirection(QDirection.UP));
        game.tick();

        assertE("----------" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
    }

    @Test
    public void shouldBallRevertDirectionUpToDownBeforeWall() {
        givenFl("          " +
                "----------" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");

        game.setBallDirection(new BallDirection(QDirection.UP));
        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
    }


    @Test
    public void shouldBallRevertDirectionDownToUpBeforeWall() {
        givenFl("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "----------" +
                "          " +
                "          ");

        game.setBallDirection(new BallDirection(QDirection.DOWN));
        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "----------" +
                "          " +
                "          ");
    }

    @Test
    public void shouldBallRevertDirectionFromLeftToRightBeforeWall() {
        givenFl("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|o       |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");

        game.setBallDirection(new BallDirection(QDirection.LEFT));
        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "| o      |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
    }

    @Test
    public void shouldBallRevertDirectionToRightBeforeWall() {
        givenFl("          " +
                "----------" +
                "|       o|" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
        game.setBallDirection(new BallDirection(QDirection.RIGHT));
        game.tick();

        assertE("          " +
                "----------" +
                "|      o |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
    }

    @Test
    public void shouldBallChangeDiagonalDirectionRightUp() {
        givenFl("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|   o    |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
        game.setBallDirection(new BallDirection(QDirection.RIGHT_UP));

        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
    }

    @Test
    public void shouldBallChangeDiagonalDirectionLeftUp() {
        givenFl("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|   o    |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
        game.setBallDirection(new BallDirection(QDirection.LEFT_UP));

        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|  o     |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
    }

    @Test
    public void shouldBallChangeDiagonalDirectionLeftDown() {
        givenFl("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|   o    |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
        game.setBallDirection(new BallDirection(QDirection.LEFT_DOWN));

        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|  o     |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
    }

    @Test
    public void shouldBallChangeDiagonalDirectionRightDown() {
        givenFl("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|   o    |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");

        game.setBallDirection(new BallDirection(QDirection.RIGHT_DOWN));
        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
    }

    @Test
    public void shouldHeroCanMoveTheirPanel() {
        givenFl("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|   o    |" +
                "|       H|" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");

        assertE("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|   o   H|" +
                "|       H|" +
                "|       H|" +
                "|        |" +
                "----------" +
                "          ");

        game.setBallDirection(new BallDirection(QDirection.RIGHT_DOWN));
        hero.down();
        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o  H|" +
                "|       H|" +
                "|       H|" +
                "----------" +
                "          ");

        hero.up();
        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|       H|" +
                "|       H|" +
                "|     o H|" +
                "|        |" +
                "----------" +
                "          ");
    }

    @Test
    public void shouldHeroLoseBall() {
        givenFl("          " +
                "----------" +
                "|        |" +
                "|     o  |" +
                "|        |" +
                "|       H|" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");

        assertE("          " +
                "----------" +
                "|        |" +
                "|     o  |" +
                "|       H|" +
                "|       H|" +
                "|       H|" +
                "|        |" +
                "----------" +
                "          ");

        game.setBallDirection(new BallDirection(QDirection.RIGHT_UP));
        hero.down();
        game.tick();

        assertE("          " +
                "----------" +
                "|      o |" +
                "|        |" +
                "|        |" +
                "|       H|" +
                "|       H|" +
                "|       H|" +
                "----------" +
                "          ");

        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|       o|" +
                "|        |" +
                "|       H|" +
                "|       H|" +
                "|       H|" +
                "----------" +
                "          ");

        verifyNoMoreInteractions(listener);

        game.tick();

        verify(listener).event(Event.LOSE);

        assertE("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|       H|" +
                "|       H|" +
                "|       H|" +
                "----------" +
                "          ");
    }

    @Test
    public void shouldHeroHitCornerBall() {
        givenFl("          " +
                "----------" +
                "|        |" +
                "|     o  |" +
                "|        |" +
                "|       H|" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");

        assertE("          " +
                "----------" +
                "|        |" +
                "|     o  |" +
                "|       H|" +
                "|       H|" +
                "|       H|" +
                "|        |" +
                "----------" +
                "          ");

        game.setBallDirection(new BallDirection(QDirection.RIGHT_UP));
        hero.up();
        game.tick();

        assertE("          " +
                "----------" +
                "|      o |" +
                "|       H|" +
                "|       H|" +
                "|       H|" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");

        hero.up();
        game.tick();

        assertE("          " +
                "----------" +
                "|       H|" +
                "|     o H|" +
                "|       H|" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");

        verifyNoMoreInteractions(listener);

        game.tick();

        assertE("          " +
                "----------" +
                "|       H|" +
                "|       H|" +
                "|    o  H|" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
    }

    @Test
    public void shouldHeroHitOtherBall() {
        givenFl("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|     o  |" +
                "|       H|" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");

        assertE("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|     o H|" +
                "|       H|" +
                "|       H|" +
                "|        |" +
                "----------" +
                "          ");

        game.setBallDirection(new BallDirection(QDirection.RIGHT_UP));
        hero.up();
        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|      oH|" +
                "|       H|" +
                "|       H|" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");

        hero.up();
        game.tick();

        assertE("          " +
                "----------" +
                "|     o H|" +
                "|       H|" +
                "|       H|" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");

        game.tick();

        assertE("          " +
                "----------" +
                "|       H|" +
                "|    o  H|" +
                "|       H|" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");

        verifyNoMoreInteractions(listener);
    }

    // TODO закончить тесты

}
















