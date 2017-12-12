package com.codenjoy.dojo.pong.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class PongTest {

    private Pong game;
//    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
//        Hero hero = level.getHero().get(0);

        game = new Pong(level, dice);
//        listener = mock(EventListener.class);
//        player = new Player(listener);
//        game.newGame(player);
//        player.hero = hero;
//        hero.init(game);
//        this.hero = game.getHeroes().get(0);
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
        game.setBallDirection(new BallDirection(Direction.DOWN));
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
        game.setBallDirection(new BallDirection(Direction.LEFT));
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
        game.setBallDirection(new BallDirection(Direction.RIGHT));
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
        game.setBallDirection(new BallDirection(Direction.UP));
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

        game.setBallDirection(new BallDirection(Direction.UP));

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

        game.setBallDirection(new BallDirection(Direction.DOWN));

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
        game.setBallDirection(new BallDirection(Direction.LEFT));

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
        game.setBallDirection(new BallDirection(Direction.RIGHT));
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
        game.setBallDirection(new BallDirection(Direction.RIGHT, Direction.UP));

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
        game.setBallDirection(new BallDirection(Direction.LEFT, Direction.UP));

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
        game.setBallDirection(new BallDirection(Direction.LEFT, Direction.DOWN));

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
        game.setBallDirection(new BallDirection(Direction.RIGHT, Direction.DOWN));

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

}
















