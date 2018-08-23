package com.codenjoy.dojo.tetris.model;

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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TetrisTest {

    private Tetris game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();
    private FigureQueue queue;
    private PrinterFactoryImpl printerFactory;

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
        printerFactory = new PrinterFactoryImpl<>();

        LevelImpl level = new LevelImpl(board);
        List<Plot> plots = level.plots();

        queue = mock(FigureQueue.class);

        game = new Tetris(queue, level.size());
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        game.setPlots(plots);
        hero = game.getPlayer().getHero();
    }

    private void assrtDr(String expected) {
        Printer printer = printerFactory.getPrinter(new BoardReader() {
            @Override
            public int size() {
                return game.size();
            }

            @Override
            public Iterable<? extends Point> elements() {
                return hero.dropped();
            }
        }, player);

        assertEquals(com.codenjoy.dojo.utils.TestUtils.injectN(expected),
                printer.print());
    }

    @Test
    public void shouldEmptyFieldAtStart() {
        givenFl("     " +
                "     " +
                "     " +
                "     " +
                "     ");

        assrtDr("     " +
                "     " +
                "     " +
                "     " +
                "     ");
    }

    @Test
    public void shouldOFiguresAtStart() {
        givenFl("      " +
                "      " +
                "      " +
                "      " +
                " OO   " +
                " OO   ");

        assrtDr("      " +
                "      " +
                "      " +
                "      " +
                " OO   " +
                " OO   ");
    }

    @Test
    public void shouldIFiguresAtStart() {
        givenFl("      " +
                "      " +
                " I    " +
                " I    " +
                " I    " +
                " I    ");

        assrtDr("      " +
                "      " +
                " I    " +
                " I    " +
                " I    " +
                " I    ");
    }

    @Test
    public void shouldJFiguresAtStart() {
        givenFl("      " +
                "      " +
                "      " +
                " J    " +
                " J    " +
                "JJ    ");

        assrtDr("      " +
                "      " +
                "      " +
                " J    " +
                " J    " +
                "JJ    ");
    }

    @Test
    public void shouldLFiguresAtStart() {
        givenFl("      " +
                "      " +
                "      " +
                " L    " +
                " L    " +
                " LL   ");

        assrtDr("      " +
                "      " +
                "      " +
                " L    " +
                " L    " +
                " LL   ");
    }

    @Test
    public void shouldSFiguresAtStart() {
        givenFl("      " +
                "      " +
                "      " +
                "      " +
                "  SS  " +
                " SSO  ");

        assrtDr("      " +
                "      " +
                "      " +
                "      " +
                "  SS  " +
                " SSO  ");
    }

    @Test
    public void shouldZFiguresAtStart() {
        givenFl("      " +
                "      " +
                "      " +
                "      " +
                "  ZZ  " +
                "  OZZ ");

        assrtDr("      " +
                "      " +
                "      " +
                "      " +
                "  ZZ  " +
                "  OZZ ");
    }

    @Test
    public void shouldTFiguresAtStart() {
        givenFl("      " +
                "      " +
                "      " +
                "      " +
                "   T  " +
                "  TTT ");

        assrtDr("      " +
                "      " +
                "      " +
                "      " +
                "   T  " +
                "  TTT ");
    }

    @Test
    public void shouldSomeFiguresAtStart() {
        givenFl("       " +
                "   SSZ " +
                "  SSZZ " +
                "ILOIZI " +
                "ILLIJI " +
                "IOOIJI " +
                "IOOJJI ");

        assrtDr("       " +
                "   SSZ " +
                "  SSZZ " +
                "ILOIZI " +
                "ILLIJI " +
                "IOOIJI " +
                "IOOJJI ");
    }

}
