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
import com.codenjoy.dojo.tetris.services.Events;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

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

        Level level = new LevelImpl(board);
        List<Plot> plots = level.plots();

        queue = mock(FigureQueue.class);

        Levels levels = mock(Levels.class);
        when(levels.getCurrentLevelNumber()).thenReturn(0);

        game = new Tetris(levels, queue, level.size());
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        Glass glass = game.getPlayer().getHero().glass();
        game.setPlots(glass, plots);
        hero = game.getPlayer().getHero();
        reset(listener);
    }

    private void assrtDr(String expected) {
        Printer printer = printerFactory.getPrinter(new BoardReader() {
            @Override
            public int size() {
                return game.size();
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Plot>() {{
                    addAll(hero.dropped());
                    addAll(hero.currentFigure());
                }};
            }
        }, player);

        assertEquals(com.codenjoy.dojo.utils.TestUtils.injectN(expected),
                printer.print().toString().replaceAll(" ", "."));
    }

    @Test
    public void shouldEmptyFieldAtStart() {
        givenFl("....." +
                "....." +
                "....." +
                "....." +
                ".....");

        assrtDr("....." +
                "....." +
                "....." +
                "....." +
                ".....");
    }

    @Test
    public void shouldOFiguresAtStart() {
        givenFl("......" +
                "......" +
                "......" +
                "......" +
                ".OO..." +
                ".OO...");

        assrtDr("......" +
                "......" +
                "......" +
                "......" +
                ".OO..." +
                ".OO...");
    }

    @Test
    public void shouldIFiguresAtStart() {
        givenFl("......" +
                "......" +
                ".I...." +
                ".I...." +
                ".I...." +
                ".I....");

        assrtDr("......" +
                "......" +
                ".I...." +
                ".I...." +
                ".I...." +
                ".I....");
    }

    @Test
    public void shouldJFiguresAtStart() {
        givenFl("......" +
                "......" +
                "......" +
                ".J...." +
                ".J...." +
                "JJ....");

        assrtDr("......" +
                "......" +
                "......" +
                ".J...." +
                ".J...." +
                "JJ....");
    }

    @Test
    public void shouldLFiguresAtStart() {
        givenFl("......" +
                "......" +
                "......" +
                ".L...." +
                ".L...." +
                ".LL...");

        assrtDr("......" +
                "......" +
                "......" +
                ".L...." +
                ".L...." +
                ".LL...");
    }

    @Test
    public void shouldSFiguresAtStart() {
        givenFl("......" +
                "......" +
                "......" +
                "......" +
                "..SS.." +
                ".SSO..");

        assrtDr("......" +
                "......" +
                "......" +
                "......" +
                "..SS.." +
                ".SSO..");
    }

    @Test
    public void shouldZFiguresAtStart() {
        givenFl("......" +
                "......" +
                "......" +
                "......" +
                "..ZZ.." +
                "..OZZ.");

        assrtDr("......" +
                "......" +
                "......" +
                "......" +
                "..ZZ.." +
                "..OZZ.");
    }

    @Test
    public void shouldTFiguresAtStart() {
        givenFl("......" +
                "......" +
                "......" +
                "......" +
                "...T.." +
                "..TTT.");

        assrtDr("......" +
                "......" +
                "......" +
                "......" +
                "...T.." +
                "..TTT.");
    }

    @Test
    public void shouldSomeFiguresAtStart() {
        givenFl("......." +
                "...SSZ." +
                "..SSZZ." +
                "ILOIZI." +
                "ILLIJI." +
                "IOOIJI." +
                "IOOJJI.");

        assrtDr("......." +
                "...SSZ." +
                "..SSZZ." +
                "ILOIZI." +
                "ILLIJI." +
                "IOOIJI." +
                "IOOJJI.");
    }

    @Test
    public void shouldCleanGlass_whenAct00() {
        givenFl("......." +
                "...SSZ." +
                "..SSZZ." +
                "ILOIZI." +
                "ILLIJI." +
                "IOOIJI." +
                "IOOJJI.");

        when(queue.next()).thenReturn(Type.O.create());
        hero.act(0, 0);
        game.tick();

        assrtDr("..OO..." +
                "..OO..." +
                "......." +
                "......." +
                "......." +
                "......." +
                ".......");

        verify(listener).event(Events.glassOverflown(1));
    }

    @Test
    public void shouldCleanGlass_whenOverflown() {
        givenFl("......." +
                ".....Z." +
                "....ZZ." +
                "ILOIZI." +
                "ILLIJI." +
                "IOOIJI." +
                "IOOJJI.");

        when(queue.next()).thenReturn(Type.O.create());
        game.tick();

        assrtDr("..OO..." +
                "..OO.Z." +
                "....ZZ." +
                "ILOIZI." +
                "ILLIJI." +
                "IOOIJI." +
                "IOOJJI.");

        verifyNoMoreInteractions(listener);

        hero.down();
        game.tick();

        assrtDr("..OO..." +
                "..OO.Z." +
                "..OOZZ." +
                "ILOIZI." +
                "ILLIJI." +
                "IOOIJI." +
                "IOOJJI.");

        verify(listener).event(Events.figuresDropped(1, Type.O.getColor().index()));

        hero.down();
        game.tick();

        assrtDr("..OO..." +
                "..OO..." +
                "......." +
                "......." +
                "......." +
                "......." +
                ".......");

        // TODO разобраться, почему не настает этот случай
        verify(listener).event(Events.glassOverflown(1));
    }

    @Test
    public void shouldDropFirstFigure_whenO() {
        givenFl("......" +
                "......" +
                "......" +
                "......" +
                "......" +
                "......");

        when(queue.next()).thenReturn(Type.O.create());
        game.tick();

        assrtDr("..OO.." +
                "..OO.." +
                "......" +
                "......" +
                "......" +
                "......");

        hero.down();
        game.tick();

        assrtDr("..OO.." +
                "..OO.." +
                "......" +
                "......" +
                "..OO.." +
                "..OO..");
    }

    @Test
    public void shouldDropFirstFigure_whenI() {
        givenFl("......" +
                "......" +
                "......" +
                "......" +
                "......" +
                "......");

        when(queue.next()).thenReturn(Type.I.create());
        game.tick();

        assrtDr("..I..." +
                "..I..." +
                "......" +
                "......" +
                "......" +
                "......");

        hero.down();
        game.tick();

        assrtDr("..I..." +
                "..I..." +
                "..I..." +
                "..I..." +
                "..I..." +
                "..I...");
    }

    @Test
    public void shouldDropFirstFigure_whenJ() {
        givenFl("......" +
                "......" +
                "......" +
                "......" +
                "......" +
                "......");

        when(queue.next()).thenReturn(Type.J.create());
        game.tick();

        assrtDr("...J.." +
                "..JJ.." +
                "......" +
                "......" +
                "......" +
                "......");

        hero.down();
        game.tick();

        assrtDr("...J.." +
                "..JJ.." +
                "......" +
                "...J.." +
                "...J.." +
                "..JJ..");
    }

    @Test
    public void shouldDropFirstFigure_whenL() {
        givenFl("......" +
                "......" +
                "......" +
                "......" +
                "......" +
                "......");

        when(queue.next()).thenReturn(Type.L.create());
        game.tick();

        assrtDr("..L..." +
                "..LL.." +
                "......" +
                "......" +
                "......" +
                "......");

        hero.down();
        game.tick();

        assrtDr("..L..." +
                "..LL.." +
                "......" +
                "..L..." +
                "..L..." +
                "..LL..");
    }

    @Test
    public void shouldDropFirstFigure_whenT() {
        givenFl("......" +
                "......" +
                "......" +
                "......" +
                "......" +
                "......");

        when(queue.next()).thenReturn(Type.T.create());
        game.tick();

        assrtDr("...T.." +
                "..TTT." +
                "......" +
                "......" +
                "......" +
                "......");

        hero.down();
        game.tick();

        assrtDr("...T.." +
                "..TTT." +
                "......" +
                "......" +
                "...T.." +
                "..TTT.");
    }

    @Test
    public void shouldDropFirstFigure_whenS() {
        givenFl("......" +
                "......" +
                "......" +
                "......" +
                "......" +
                "......");

        when(queue.next()).thenReturn(Type.S.create());
        game.tick();

        assrtDr("...SS." +
                "..SS.." +
                "......" +
                "......" +
                "......" +
                "......");

        hero.down();
        game.tick();

        assrtDr("...SS." +
                "..SS.." +
                "......" +
                "......" +
                "...SS." +
                "..SS..");
    }

    @Test
    public void shouldDropFirstFigure_whenZ() {
        givenFl("......" +
                "......" +
                "......" +
                "......" +
                "......" +
                "......");

        when(queue.next()).thenReturn(Type.Z.create());
        game.tick();

        assrtDr("..ZZ.." +
                "...ZZ." +
                "......" +
                "......" +
                "......" +
                "......");

        hero.down();
        game.tick();

        assrtDr("..ZZ.." +
                "...ZZ." +
                "......" +
                "......" +
                "..ZZ.." +
                "...ZZ.");
    }

    @Test
    public void shouldRemoveLine_whenO_and6x6() {
        int level = 1;
        int figure = Type.O.getColor().index();
        givenFl("......" +
                "......" +
                "......" +
                "......" +
                "......" +
                "......");

        when(queue.next()).thenReturn(Type.O.create());
        game.tick();

        assrtDr("..OO.." +
                "..OO.." +
                "......" +
                "......" +
                "......" +
                "......");

        hero.left();
        hero.left();
        hero.down();
        game.tick();

        assrtDr("..OO.." +
                "..OO.." +
                "......" +
                "......" +
                "OO...." +
                "OO....");

        verify(listener).event(Events.figuresDropped(level, figure));
        reset(listener);

        hero.down();
        game.tick();

        assrtDr("..OO.." +
                "..OO.." +
                "......" +
                "......" +
                "OOOO.." +
                "OOOO..");

        verify(listener).event(Events.figuresDropped(level, figure));
        reset(listener);

        hero.right();
        hero.right();
        hero.down();
        game.tick();

        assrtDr("..OO.." +
                "..OO.." +
                "......" +
                "......" +
                "......" +
                "......");

        verify(listener).event(Events.figuresDropped(level, figure));
        verify(listener).event(Events.linesRemoved(level, 2));
    }

    @Test
    public void shouldRemoveLine_whenO_and10x10() {
        int level = 1;
        int figure = Type.O.getColor().index();
        givenFl(".........." +
                ".........." +
                ".........." +
                ".........." +
                ".........." +
                ".........." +
                ".........." +
                ".........." +
                "OOOO..OOOO" +
                "OOOO..OOOO");

        when(queue.next()).thenReturn(Type.O.create());
        game.tick();

        assrtDr("....OO...." +
                "....OO...." +
                ".........." +
                ".........." +
                ".........." +
                ".........." +
                ".........." +
                ".........." +
                "OOOO..OOOO" +
                "OOOO..OOOO");

        hero.down();
        game.tick();

        assrtDr("....OO...." +
                "....OO...." +
                ".........." +
                ".........." +
                ".........." +
                ".........." +
                ".........." +
                ".........." +
                ".........." +
                "..........");

        verify(listener).event(Events.figuresDropped(level, figure));
        verify(listener).event(Events.linesRemoved(level, 2));
    }

    // тут начиналось переполнение long внутри glass
    @Test
    public void shouldRemoveLine_whenO_and11x11() {
        int level = 1;
        int figure = Type.O.getColor().index();
        givenFl("..........." +
                "..........." +
                "..........." +
                "..........." +
                "..........." +
                "..........." +
                "..........." +
                "..........." +
                "..........." +
                "OOOO..OOOOO" +
                "OOOO..OOOOO");

        when(queue.next()).thenReturn(Type.O.create());
        game.tick();

        assrtDr("....OO....." +
                "....OO....." +
                "..........." +
                "..........." +
                "..........." +
                "..........." +
                "..........." +
                "..........." +
                "..........." +
                "OOOO..OOOOO" +
                "OOOO..OOOOO");

        hero.down();
        game.tick();

        assrtDr("....OO....." +
                "....OO....." +
                "..........." +
                "..........." +
                "..........." +
                "..........." +
                "..........." +
                "..........." +
                "..........." +
                "..........." +
                "...........");

        verify(listener).event(Events.figuresDropped(level, figure));
        verify(listener).event(Events.linesRemoved(level, 2));
    }

    @Test
    public void shouldBugWithOverflow() {
        givenFl(".....I.III........" +
                ".....I.IJOOOO....." +
                "....IIIIJOOOO....." +
                "....IIIJJOOJOO...." +
                "....IIIIOOOJOO...." +
                "....IIIIOOJJOJ...." +
                ".I..IIIIIOOOOJ...." +
                ".I..IIIIIOOJJJ...." +
                ".II.IIIIIOJJOOO..." +
                ".II.IIIIIOOOOOO..." +
                ".IIOOIIIOOOOOJO..." +
                ".IIOOJIIOOIOOJO..." +
                ".IJIOJIJOJIOJJOJO." +
                ".IJIJJIJOJIJOOOJO." +
                "IJJIJIJJJJIJOOJJJ." +
                "IOIIJIOOOIJJIOOOJ." +
                "IJIJJIJOOIOJIOOJJ." +
                "IJIOOIJOOIOJIOOOO.");

        when(queue.next()).thenReturn(Type.O.create());
        game.tick();

        assrtDr(".....I.III........" +
                ".....I.IJOOOO....." +
                "....IIIIJOOOO....." +
                "....IIIJJOOJOO...." +
                "....IIIIOOOJOO...." +
                "....IIIIOOJJOJ...." +
                ".I..IIIIIOOOOJ...." +
                ".I..IIIIIOOJJJ...." +
                ".II.IIIIIOJJOOO..." +
                ".II.IIIIIOOOOOO..." +
                ".IIOOIIIOOOOOJO..." +
                ".IIOOJIIOOIOOJO..." +
                ".IJIOJIJOJIOJJOJO." +
                ".IJIJJIJOJIJOOOJO." +
                "IJJIJIJJJJIJOOJJJ." +
                "IOIIJIOOOIJJIOOOJ." +
                "IJIJJIJOOIOJIOOJJ." +
                "IJIOOIJOOIOJIOOOO.");

        hero.right();
        game.tick();

        assrtDr("........OO........" +
                "........OO........" +
                ".................." +
                ".................." +
                ".................." +
                ".................." +
                ".................." +
                ".................." +
                ".................." +
                ".................." +
                ".................." +
                ".................." +
                ".................." +
                ".................." +
                ".................." +
                ".................." +
                ".................." +
                "..................");
    }
}
