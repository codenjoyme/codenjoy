package com.codenjoy.dojo.excitebike.model;

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


import com.codenjoy.dojo.excitebike.model.items.bike.Bike;
import com.codenjoy.dojo.excitebike.services.parse.MapParserImpl;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameFieldImplTest {

    private GameFieldImpl game;
    private Bike bike;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();

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
        MapParserImpl parser = new MapParserImpl(board);
        Bike bike = parser.getBikes().get(0);

        game = new GameFieldImpl(parser, dice);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        player.setHero(bike);
        bike.init(game);
        this.bike = game.getBikes().get(0);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldFieldAtStart() {
        givenFl("■■■■■" +
                " o ▼ " +
                "  » ░" +
                " ▲ ▒ " +
                "■■■■■");

        assertE("■■■■■" +
                " o ▼ " +
                "  » ░" +
                " ▲ ▒ " +
                "■■■■■");
    }

    @Test
    public void shouldShiftTrack() {
        givenFl("■■■■■" +
                " o ▼ " +
                "  » ░" +
                " ▲ ▒ " +
                "■■■■■");

        when(dice.next(anyInt())).thenReturn(1);
        game.tick();

        assertE("■■■■■" +
                " o▼  " +
                " » ░▒" +
                "▲ ▒  " +
                "■■■■■");
    }

    @Test
    public void shouldReplaceShiftableElementToBike() {
        givenFl("■■■■■" +
                " o░  " +
                "     " +
                "     " +
                "■■■■■");

        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        assertE("■■■■■" +
                " o   " +
                "     " +
                "     " +
                "■■■■■");

        game.tick();

        assertE("■■■■■" +
                "░o   " +
                "     " +
                "     " +
                "■■■■■");
    }

    @Test
    public void shouldMoveBikeVertically() {
        when(dice.next(anyInt())).thenReturn(5);

        givenFl("■■■■■" +
                " o   " +
                "     " +
                "     " +
                "■■■■■");

        bike.down();
        game.tick();

        assertE("■■■■■" +
                "     " +
                " o   " +
                "     " +
                "■■■■■");

        bike.up();
        game.tick();

        assertE("■■■■■" +
                " o   " +
                "     " +
                "     " +
                "■■■■■");
    }

    @Test
    public void shouldInclineBikeToLeftAndRight() {
        when(dice.next(anyInt())).thenReturn(5);

        givenFl("■■■■■" +
                "     " +
                "  o  " +
                "     " +
                "■■■■■");

        bike.right();
        game.tick();

        assertE("■■■■■" +
                "     " +
                "  )  " +
                "     " +
                "■■■■■");

        bike.left();
        game.tick();

        assertE("■■■■■" +
                "     " +
                "  o  " +
                "     " +
                "■■■■■");

        bike.left();
        game.tick();

        assertE("■■■■■" +
                "     " +
                "  (  " +
                "     " +
                "■■■■■");

        bike.right();
        game.tick();

        assertE("■■■■■" +
                "     " +
                "  o  " +
                "     " +
                "■■■■■");
    }

    @Test
    public void shouldFallBike() {
        when(dice.next(anyInt())).thenReturn(5);

        givenFl("■■■■■" +
                "     " +
                "  o  " +
                "     " +
                "■■■■■");

        bike.crush();
        game.tick();

        assertE("■■■■■" +
                "     " +
                " ~   " +
                "     " +
                "■■■■■");
    }

    @Test
    public void shouldIgnoreMovingAfterFallingBike() {
        when(dice.next(anyInt())).thenReturn(5);

        givenFl("■■■■■" +
                "     " +
                "  o  " +
                "     " +
                "■■■■■");

        bike.crush();
        game.tick();

        assertE("■■■■■" +
                "     " +
                " ~   " +
                "     " +
                "■■■■■");

        bike.up();
        game.tick();

        assertE("■■■■■" +
                "     " +
                "~    " +
                "     " +
                "■■■■■");
    }

    @Test
    public void shouldInhibitBike() {
        when(dice.next(anyInt())).thenReturn(5);

        givenFl("■■■■■" +
                "     " +
                "o▒   " +
                "     " +
                "■■■■■");

        game.tick();

        assertE("■■■■■" +
                "     " +
                "o    " +
                "     " +
                "■■■■■");
    }

    @Test
    public void shouldInhibitBike2() {
        when(dice.next(anyInt())).thenReturn(5);

        givenFl("■■■■■" +
                "     " +
                "   o▒" +
                "     " +
                "■■■■■");

        game.tick();

        assertE("■■■■■" +
                "     " +
                " o ▒ " +
                "     " +
                "■■■■■");
    }

    @Test
    public void shouldInhibitBikeAfterMovingUp() {
        when(dice.next(anyInt())).thenReturn(5);

        givenFl("■■■■■" +
                "   ▒ " +
                "  o  " +
                "     " +
                "■■■■■");

        bike.up();
        game.tick();

        assertE("■■■■■" +
                "o ▒  " +
                "     " +
                "     " +
                "■■■■■");
    }

    @Test
    public void shouldAccelerateBike() {
        when(dice.next(anyInt())).thenReturn(5);

        givenFl("■■■■■" +
                "     " +
                "o»   " +
                "     " +
                "■■■■■");

        game.tick();

        assertE("■■■■■" +
                "     " +
                "» o  " +
                "     " +
                "■■■■■");
    }

    @Test
    public void shouldAccelerateBike2() {
        when(dice.next(anyInt())).thenReturn(5);

        givenFl("■■■■■" +
                "     " +
                "   o»" +
                "     " +
                "■■■■■");

        game.tick();

        assertE("■■■■■" +
                "     " +
                "   »o" +
                "     " +
                "■■■■■");
    }

    @Test
    public void shouldAccelerateBikeAfterMovingUp() {
        when(dice.next(anyInt())).thenReturn(5);

        givenFl("■■■■■" +
                "   » " +
                "  o  " +
                "     " +
                "■■■■■");

        bike.up();
        game.tick();

        assertE("■■■■■" +
                "  » o" +
                "     " +
                "     " +
                "■■■■■");
    }

    @Test
    public void shouldObstructBike() {
        when(dice.next(anyInt())).thenReturn(5);

        givenFl("■■■■■" +
                "     " +
                "   o█" +
                "     " +
                "■■■■■");

        game.tick();

        assertE("■■■■■" +
                "     " +
                "  ~█ " +
                "     " +
                "■■■■■");

        game.tick();

        assertE("■■■■■" +
                "     " +
                " ~█  " +
                "     " +
                "■■■■■");

        game.tick();

        assertE("■■■■■" +
                "     " +
                "~█   " +
                "     " +
                "■■■■■");

        game.tick();

        assertE("■■■■■" +
                "     " +
                "█    " +
                "     " +
                "■■■■■");

        game.tick();

        assertE("■■■■■" +
                "     " +
                "     " +
                "     " +
                "■■■■■");
    }

    @Test
    public void shouldCrushEnemyBikeAfterClash() {
        when(dice.next(anyInt())).thenReturn(5);

        givenFl("■■■■■" +
                "     " +
                "   o " +
                "     " +
                "■■■■■");

        Bike enemyBike = new Bike(bike.getX(), bike.getY()-1);
        Player enemyPlayer = new Player(listener);
        game.newGame(enemyPlayer);
        enemyPlayer.setHero(enemyBike);
        enemyBike.init(game);

        assertE("■■■■■" +
                "     " +
                "   o " +
                "   o " +
                "■■■■■");


        bike.down();
        game.tick();

        assertFalse(enemyBike.isAlive());
        assertTrue(bike.isAlive());

        //        assertE("■■■■■" +
//                "     " +
//                "   o " +
//                "  ~  " +
//                "■■■■■");

    }

    @Test
    public void shouldDoNothingAfterBikesClashEachOther() {
        when(dice.next(anyInt())).thenReturn(5);

        givenFl("■■■■■" +
                "     " +
                "   o " +
                "     " +
                "■■■■■");

        Bike enemyBike = new Bike(bike.getX(), bike.getY()-1);
        Player enemyPlayer = new Player(listener);
        game.newGame(enemyPlayer);
        enemyPlayer.setHero(enemyBike);
        enemyBike.init(game);

        assertE("■■■■■" +
                "     " +
                "   o " +
                "   o " +
                "■■■■■");

        bike.down();
        enemyBike.up();
        game.tick();

        assertE("■■■■■" +
                "     " +
                "   o " +
                "   o " +
                "■■■■■");

        bike.up();
        game.tick();

        assertE("■■■■■" +
                "   o " +
                "     " +
                "   o " +
                "■■■■■");

        //TODO
//        bike.down();
//        enemyBike.up();
//        game.tick();
//
//        assertE("■■■■■" +
//                "   o " +
//                "     " +
//                "   o " +
//                "■■■■■");
    }

    @Test
    public void shouldMoveBikes() {
        when(dice.next(anyInt())).thenReturn(5);

        givenFl("■■■■■" +
                "     " +
                "   o " +
                "     " +
                "■■■■■");

        Bike enemyBike = new Bike(bike.getX(), bike.getY()-1);
        Player enemyPlayer = new Player(listener);
        game.newGame(enemyPlayer);
        enemyPlayer.setHero(enemyBike);
        enemyBike.init(game);

        assertE("■■■■■" +
                "     " +
                "   o " +
                "   o " +
                "■■■■■");

        bike.up();
        enemyBike.up();
        game.tick();

        assertE("■■■■■" +
                "   o " +
                "   o " +
                "     " +
                "■■■■■");

        bike.down();
        enemyBike.down();
        game.tick();

        assertE("■■■■■" +
                "     " +
                "   o " +
                "   o " +
                "■■■■■");


        enemyBike.up();
        bike.up();
        game.tick();

        assertE("■■■■■" +
                "   o " +
                "   o " +
                "     " +
                "■■■■■");


        enemyBike.down();
        bike.down();
        game.tick();

        assertE("■■■■■" +
                "     " +
                "   o " +
                "   o " +
                "■■■■■");
    }
}
