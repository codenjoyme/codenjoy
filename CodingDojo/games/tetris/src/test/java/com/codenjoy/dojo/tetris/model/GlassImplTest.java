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


import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.tetris.services.Events;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.tetris.model.TestUtils.HEIGHT;
import static com.codenjoy.dojo.tetris.model.TestUtils.WIDTH;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GlassImplTest {

    public static final int CENTER_X = WIDTH / 2 - 1;
    public static final int TOP_Y = HEIGHT - 1;
    private Glass glass;
    private Figure point;
    private Figure glassWidthFigure;
    private Figure line9Width;
    @Mock
    private EventListener listener;
    @Captor
    private ArgumentCaptor<Integer> removedLines;
    @Captor
    private ArgumentCaptor<Figure> droppedFigure;

    @Before
    public void setUp() throws Exception {
        glass = new GlassImpl(WIDTH, HEIGHT, () -> 1);
        glass.setListener(listener);
        point = new FigureImpl();
        glassWidthFigure = new FigureImpl(0, 0, StringUtils.repeat("#", WIDTH));
        line9Width = new FigureImpl(0, 0, StringUtils.repeat("#", WIDTH - 1));
    }

    @Test
    public void shouldAcceptWhenEmpty() {
        assertTrue(glass.accept(new FigureImpl(), 1, 1));
    }

    @Test
    public void shouldRejectWhenAcceptingOnDroppedPlace() {
        glass.drop(new FigureImpl(), 0, 0);

        assertFalse(glass.accept(new FigureImpl(), 0, 0));
    }

    @Test
    public void shouldPerformDropWhenDropRequested() {
        glass.drop(new FigureImpl(), 0, HEIGHT);

        assertFalse(glass.accept(new FigureImpl(), 0, 0));
    }

    @Test
    public void shouldRejectWhenFigurePartlyOutsideFromLeft() {
        assertFalse(glass.accept(new FigureImpl(1, 0, "##"), 0, HEIGHT));
    }

    @Test
    public void shouldRejectWhenFigurePartlyOutsideFromRight() {
        assertFalse(glass.accept(new FigureImpl(0, 0, "##"), WIDTH, HEIGHT));
    }

    @Test
    public void shouldRejectWhenFigurePartlyOutsideBottom() {
        assertFalse(glass.accept(new FigureImpl(0, 0, "#", "#"), WIDTH, 0));
    }

    @Test
    public void shouldRejectWhenFigurePartlyOverDroppedRow() {
        Figure figure = new FigureImpl(1, 0, "##");

        glass.drop(figure, WIDTH / 2, HEIGHT);

        assertFalse(glass.accept(figure, WIDTH / 2 + 1, 0));
        assertFalse(glass.accept(figure, WIDTH / 2, 0));
        assertFalse(glass.accept(figure, WIDTH / 2 - 1, 0));
    }

    @Test
    public void shouldAcceptWhenFigureAboveDropped() {
        glass.drop(new FigureImpl(1, 0, "##"), WIDTH / 2, HEIGHT);

        assertTrue(glass.accept(new FigureImpl(1, 0, "##"), WIDTH / 2, 1));
    }

    @Test
    public void shouldRejectWhenFigurePartlyOverDroppedColumn() {
        Figure figure = new FigureImpl(0, 1, "#", "#");

        glass.drop(figure, WIDTH / 2, HEIGHT);

        assertFalse(glass.accept(figure, WIDTH / 2, 0));
        assertFalse(glass.accept(figure, WIDTH / 2, 1));
        assertTrue(glass.accept(figure, WIDTH / 2, 2));
    }

    @Test
    public void shouldRejectWhenFigureOverlapDroppedColumnByBottomSize() {
        Figure figure = new FigureImpl(0, 0, "#", "#");

        glass.drop(figure, WIDTH / 2, HEIGHT);

        assertFalse(glass.accept(figure, WIDTH / 2, 2));
    }

    @Test
    public void shouldRejectWhenFigureIsNotSymmetric() {
        Figure figure = new FigureImpl(1, 0, "##", " #");

        glass.drop(figure, WIDTH / 2, HEIGHT);

        assertFalse(glass.accept(point, WIDTH / 2 - 1, 1));
    }

    @Test
    public void shouldRejectWhenFigureIsNotSymmetricPositive() {
        Figure figure = new FigureImpl(1, 0, "##", " #");

        glass.drop(figure, WIDTH / 2, HEIGHT);

        assertTrue(glass.accept(figure, WIDTH / 2 + 1, 2));
    }

    @Test
    public void shouldRejectWhenFigureDroppedToFilledGlass() {
        glass.drop(point, 0, HEIGHT);

        glass.drop(point, 0, HEIGHT);

        assertFalse(glass.accept(point, 0, 1));
    }


    @Test
    public void shouldIgnoreWhenDropOutside() {
        glass.drop(new FigureImpl(0, 0, "##"), -1, HEIGHT - 1);

        assertTrue(glass.accept(point, 0, 0));
    }

    @Test
    public void shouldIgnoreWhenOutOfBoundsLeft() {
        glass.drop(new FigureImpl(1, 0, "##"), 0, HEIGHT - 1);

        assertTrue(glass.isEmpty());
    }

    @Test
    public void shouldIgnoreWhenOutOfBoundsRightPartially() {
        glass.drop(new FigureImpl(0, 0, "##"), WIDTH - 1, HEIGHT - 1);

        assertTrue(glass.isEmpty());
    }

    @Test
    public void shouldIgnoreWhenOutOfBoundsBottom() {
        glass.drop(new FigureImpl(0, 0, "#", "#"), 0, 0);

        assertTrue(glass.isEmpty());
    }

    @Test
    public void shouldRejectWhenPartiallyOutsideOnRight() {
        assertFalse(glass.accept(new FigureImpl(0, 0, "##"), WIDTH - 1, 0));
    }

    @Test
    public void shouldReturnPlotCoordinateSimpleFigure() {
        glass.figureAt(point, 1, 1);

        Plot plot = glass.currentFigure().get(0);
        assertContainsPlot(1, 1, Elements.BLUE, plot);
    }

    @Test
    public void shouldReturnPlotCoordinateSimpleRedFigure() {
        glass.figureAt(createLine(Type.Z, "#"), 1, 1);

        Plot plot = glass.currentFigure().get(0);
        assertContainsPlot(1, 1, Elements.RED, plot);
    }

    @Test
    public void shouldReturnPlotCoordinateHorizontalFigure() {
        glass.figureAt(new FigureImpl(1, 0, "###"), 1, 1);

        List<Plot> plots = glass.currentFigure();
        TestUtils.assertContainsPlot(1 - 1, 1, Elements.BLUE, plots);
        TestUtils.assertContainsPlot(1, 1, Elements.BLUE, plots);
        TestUtils.assertContainsPlot(1 + 1, 1, Elements.BLUE, plots);
    }

    @Test
    public void shouldReturnPlotCoordinateVerticalFigure() {
        glass.figureAt(new FigureImpl(0, 1, "#", "#", "#"), 1, 3);

        List<Plot> plots = glass.currentFigure();
        TestUtils.assertContainsPlot(1, 3 + 1, Elements.BLUE, plots);
        TestUtils.assertContainsPlot(1, 3, Elements.BLUE, plots);
        TestUtils.assertContainsPlot(1, 3 - 1, Elements.BLUE, plots);
    }

    @Test
    public void shouldReturnPlotCoordinateAsymetricFigure() {
        glass.figureAt(new FigureImpl(1, 0, " #"), 1, 0);

        List<Plot> plots = glass.currentFigure();
        assertEquals(1, plots.size());
        TestUtils.assertContainsPlot(1, 0, Elements.BLUE, plots);
    }

    @Test
    public void shouldReturnPlotCoordinateAsymetricFigure2() {
        glass.figureAt(new FigureImpl(0, 0, " #"), 1, 0);

        List<Plot> plots = glass.currentFigure();
        assertEquals(1, plots.size());
        TestUtils.assertContainsPlot(1 + 1, 0, Elements.BLUE, plots);
    }

    @Test
    public void shouldReturnEmptyArrayWhenNoFigure() {
        assertTrue(glass.currentFigure().isEmpty());
    }

    @Test
    public void shouldReturnPlotOfDroppedFigure() {
        glass.drop(point, 0, HEIGHT);

        List<Plot> plots = glass.dropped();
        TestUtils.assertContainsPlot(0, 0, Elements.BLUE, plots);
    }

    @Test
    public void shouldReturnPlotOfDroppedFigure2() {
        glass.drop(new FigureImpl(1, 1, "##", "##"), 3, HEIGHT);

        List<Plot> plots = glass.dropped();
        TestUtils.assertContainsPlot(3 - 1, 1, Elements.BLUE, plots);
        TestUtils.assertContainsPlot(3, 1, Elements.BLUE, plots);
        TestUtils.assertContainsPlot(3 - 1, 0, Elements.BLUE, plots);
        TestUtils.assertContainsPlot(3, 0, Elements.BLUE, plots);
    }


    @Test
    public void shouldEmptyWhenRequested() {
        glass.drop(point, 0, 0);

        glass.empty();

        assertTrue(glass.dropped().isEmpty());
    }

    @Test
    public void shouldRemoveFilledLineAfterDrop() {
        glass.drop(glassWidthFigure, 0, 0);

        assertTrue(glass.isEmpty());
    }

    @Test
    public void shouldRemoveFilledLineWhenSeveralFilled() {
        Figure columnFigure = new FigureImpl(0, 0, "#", "#");
        glass.drop(line9Width, 0, HEIGHT);
        glass.drop(line9Width, 0, HEIGHT);

        glass.drop(columnFigure, WIDTH - 1, HEIGHT);

        assertTrue(glass.isEmpty());
    }

    @Test
    public void shouldRemoveFilledWhenGarbageOnTop() {
        drop(line9Width, HEIGHT);

        glass.drop(point, WIDTH - 1, HEIGHT);

        assertTrue(glass.accept(glassWidthFigure, 0, HEIGHT - 1));
    }

    @Test
    public void shouldRemoveFilledLineWhenInMiddleOfGlass() {
        glass.drop(point, 0, HEIGHT);
        glass.drop(line9Width, 0, HEIGHT);

        glass.drop(new FigureImpl(0, 0, "#", "#"), WIDTH - 1, HEIGHT);

        assertTrue(glass.accept(glassWidthFigure, 0, 1));
    }

    @Test
    public void shouldNotifyWhenLineRemoved() {
        glass.drop(glassWidthFigure, 0, HEIGHT);

        verify(listener).event(Events.linesRemoved(1, 1));
    }

    @Test
    public void shouldNotifyWhenSeveralLinesRemoved() {
        glass.drop(line9Width, 0, HEIGHT);
        glass.drop(line9Width, 0, HEIGHT);

        glass.drop(new FigureImpl(0, 0, "#", "#"), WIDTH - 1, HEIGHT);

        verify(listener).event(Events.linesRemoved(1, 2));
    }

    @Test
    public void shouldNotifyScoreBoardWhenDropped() {
        glass.drop(point, 0, HEIGHT);

        verify(listener).event(Events.figuresDropped(1, point.type().getColor().index()));
    }

    @Test
    public void shouldTriggerListenerWhenOverflow() {
        glass.empty();

        verify(listener).event(Events.glassOverflown(1));
    }

    @Test
    public void shouldTriggerAllListenersWhenOverflow() {
        glass.empty();

        verify(listener).event(Events.glassOverflown(1));
    }

    @Test
    public void shouldNotifyAllListenersScoreBoardWhenDropped() {
        glass.drop(point, 0, HEIGHT);

        verify(listener).event(Events.figuresDropped(1, point.type().getColor().index()));
    }

    @Test
    public void shouldNotifyAllListenersScoreBoardWhenLinesRemoved() {
        glass.drop(glassWidthFigure, 0, HEIGHT);

        verify(listener).event(Events.figuresDropped(1, glassWidthFigure.type().getColor().index()));
    }

    @Test
    public void shouldQuietWhenDropOutOfBounds() {
        glass.drop(createVerticalFigure(HEIGHT), WIDTH / 2, HEIGHT);

        try {
            glass.drop(Type.I.create(), WIDTH / 2, HEIGHT);
            verify(listener, times(1)).event(Events.figuresDropped(1, Type.I.getColor().index()));
        } catch (Exception e) {
            fail("Was exception: " + e);
        }
    }

    @Test
    public void shouldRemainWhenDropped_FoundInIntegrationTesting() {
        Figure twoRowsFigure = new FigureImpl(0, 0, "#", "#");
        glass.drop(twoRowsFigure, WIDTH / 2, HEIGHT);

        glass.drop(twoRowsFigure, WIDTH / 2, HEIGHT);

        assertFalse(glass.accept(point, WIDTH / 2, 2));
        assertFalse(glass.accept(point, WIDTH / 2, 3));
    }

    @Test
    public void shouldBeOccupiedWhenLongFigureDroppedOnTopOfGlass() {
        glass.drop(createVerticalFigure(HEIGHT - 2), CENTER_X, TOP_Y);
        Figure figure = Type.J.create();
        glass.drop(figure, CENTER_X, TOP_Y);

        assertFalse(glass.accept(figure, CENTER_X, TOP_Y));
    }

    @Test
    public void shouldStoreColorsWhenRedFigure() {
        glass.drop(createLine(Type.Z, "#"), CENTER_X, TOP_Y);

        assertEquals(Elements.RED, glass.dropped().get(0).getColor());
    }

    @Test
    public void shouldNotAcceptWhenDroppedYellowFigure_int_overflow() {
        glass.drop(createLine(Type.O, "#"), 0, TOP_Y);

        assertFalse(glass.accept(point, 0, 0));
    }

    @Test
    public void shouldRemoveLinesWhenDroppedDifferentColors() {
        glass.drop(createLine(Type.I, "#########"), 1, TOP_Y);
        glass.drop(createLine(Type.J, "#"), 0, TOP_Y);

        verify(listener).event(Events.linesRemoved(1, 1));
        assertTrue(glass.accept(point, 0, 0));
    }

    @Test
    public void shouldRemoveLinesInBetweenWhenDropped() {
        glass.drop(createLine("# ########"), 0, TOP_Y);
        glass.drop(createLine("##########"), 0, TOP_Y);

        verify(listener).event(Events.linesRemoved(1, 1));
        assertFalse(glass.accept(point, 0, 0));
        assertTrue(glass.accept(point, 1, 0));
    }

    private Figure createLine(Type type, String ... lines) {
        return new FigureImpl(0, 0, type, lines);
    }

    private Figure createLine(String ... lines) {
        return createLine(Type.I, lines);
    }

    private Figure createVerticalFigure(int height) {
        String[] verticalLine = new String[height];
        Arrays.fill(verticalLine, "#");
        return new FigureImpl(0, 0, verticalLine);
    }

    private void drop(Figure lineFigure, int times) {
        for (int i = 0; i < times; i++) {
            glass.drop(lineFigure, 0, HEIGHT);
        }
    }

    private void assertContainsPlot(final int x, final int y, final Elements color, Plot... plots) {
        TestUtils.assertContainsPlot(x, y, color, Arrays.asList(plots));
    }
}
