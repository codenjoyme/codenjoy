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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameFieldImplSystemTest {

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

    private void init(String board) {
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

    @Test
    public void shouldFieldAtStart() {
        //given
        String board = "■■■■■" +
                " o ▼ " +
                "  »  " +
                " ▲ ▒ " +
                "■■■■■";

        //when
        init(board);

        //then
        String expected = "■■■■■" +
                " o ▼ " +
                "  »  " +
                " ▲ ▒ " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldShiftTrack() {
        //given
        String board = "■■■■■" +
                " o ▼ " +
                "  »  " +
                " ▲ ▒ " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(1);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                " o▼  " +
                " »  ▒" +
                "▲ ▒  " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldMoveBikeToDown() {
        //given
        String board = "■■■■■" +
                " o   " +
                "     " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.down();
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " o   " +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldMoveBikeToUp() {
        //given
        String board = "■■■■■" +
                "     " +
                " o   " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.up();
        game.tick();

        //then
        String expected = "■■■■■" +
                " o   " +
                "     " +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldInclineBikeToRight__IfBikeIsNotInclined() {
        //given
        String board = "■■■■■" +
                "     " +
                "  o  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.right();
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  )  " +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldInclineBikeToRight__IfBikeIsInclinedToLeft() {
        //given
        String board = "■■■■■" +
                "     " +
                "  o  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        bike.left();
        game.tick();

        //when
        bike.right();
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  o  " +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldInclineBikeToLeft__IfBikeIsNotInclined() {
        //given
        String board = "■■■■■" +
                "     " +
                "  o  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.left();
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  (  " +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }


    @Test
    public void shouldInclineBikeToLeft__IfBikeIsInclinedToRight() {
        //given
        String board = "■■■■■" +
                "     " +
                "  o  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        bike.right();
        game.tick();

        //when
        bike.left();
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  o  " +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldFallBike() {
        //given
        String board = "■■■■■" +
                "     " +
                "  o  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.crush();
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " ~   " +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldIgnoreMovingAfterBikeIsFallen() {
        //given
        String board = "■■■■■" +
                "     " +
                "  o  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        bike.crush();
        game.tick();

        //when
        bike.up();
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "~    " +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldInhibitBike() {
        //given
        String board = "■■■■■" +
                "     " +
                "o▒   " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "o    " +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldInhibitBike2() {
        //given
        String board = "■■■■■" +
                "     " +
                "   o▒" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " o ▒ " +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldInhibitBikeAfterMovingUp() {
        //given
        String board = "■■■■■" +
                "   ▒ " +
                "  o  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.up();
        game.tick();

        //then
        String expected = "■■■■■" +
                "o ▒  " +
                "     " +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldAccelerateBike() {
        //given
        String board = "■■■■■" +
                "     " +
                "o»   " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "» o  " +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldAccelerateBike2() {
        //given
        String board = "■■■■■" +
                "     " +
                "   o»" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "   »o" +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldAccelerateBikeAfterMovingUp() {
        //given
        String board = "■■■■■" +
                "   » " +
                "  o  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.up();
        game.tick();

        //then
        String expected = "■■■■■" +
                "  » o" +
                "     " +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldObstructBike() {
        //given
        String board = "■■■■■" +
                "     " +
                "   o█" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  ~█ " +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldMoveObstructedBikeAndObstacleTogether() {
        //given
        String board = "■■■■■" +
                "     " +
                "   o█" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " ~█  " +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

        @Test
    public void shouldRemoveObstructedBikeFromBoard__IfBikeXCoordinateIsOutOfBound() {
        //given
        String board = "■■■■■" +
                "     " +
                " o█  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "█    " +
                "     " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

    @Test
    public void shouldSpawnAcceleratorOnTheHighestLine(){
        //given
        String board = "■■■■■" +
                "     " +
                "     " +
                "o    " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(0, 0, 2);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "    »" +
                "     " +
                "o    " +
                "■■■■■";
        assertEquals(TestUtils.injectN(expected), printer.getPrinter(game.reader(), player).print());
    }

}
