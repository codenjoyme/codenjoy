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


import com.codenjoy.dojo.tetris.model.*;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.*;
import static com.codenjoy.dojo.tetris.model.TestUtils.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TetrisGlassTest {

    public static final int CENTER_X = WIDTH / 2 - 1;
    public static final int TOP_Y = HEIGHT - 1;
    private TetrisGlass glass;
    private TetrisFigure point;
    private TetrisFigure glassWidthFigure;
    private TetrisFigure line9Width;
    @Mock
    private GlassEventListener glassEventListener;
    @Captor
    private ArgumentCaptor<Integer> removedLines;
    @Captor
    private ArgumentCaptor<Figure> droppedFigure;

    @Before
    public void setUp() throws Exception {
        glass = new TetrisGlass(WIDTH, HEIGHT, glassEventListener);
        point = new TetrisFigure();
        glassWidthFigure = new TetrisFigure(0, 0, StringUtils.repeat("#", WIDTH));
        line9Width = new TetrisFigure(0, 0, StringUtils.repeat("#", WIDTH - 1));
    }

    @Test
    public void shouldAcceptWhenEmpty() {
        assertTrue(glass.accept(new TetrisFigure(), 1, 1));
    }

    @Test
    public void shouldRejectWhenAcceptingOnDroppedPlace() {
        glass.drop(new TetrisFigure(), 0, 0);

        assertFalse(glass.accept(new TetrisFigure(), 0, 0));
    }

    @Test
    public void shouldPerformDropWhenDropRequested() {
        glass.drop(new TetrisFigure(), 0, HEIGHT);

        assertFalse(glass.accept(new TetrisFigure(), 0, 0));
    }

    @Test
    public void shouldRejectWhenFigurePartlyOutsideFromLeft() {
        assertFalse(glass.accept(new TetrisFigure(1, 0, "##"), 0, HEIGHT));
    }

    @Test
    public void shouldRejectWhenFigurePartlyOutsideFromRight() {
        assertFalse(glass.accept(new TetrisFigure(0, 0, "##"), WIDTH, HEIGHT));
    }

    @Test
    public void shouldRejectWhenFigurePartlyOutsideBottom() {
        assertFalse(glass.accept(new TetrisFigure(0, 0, "#", "#"), WIDTH, 0));
    }

    @Test
    public void shouldRejectWhenFigurePartlyOverDroppedRow() {
        TetrisFigure figure = new TetrisFigure(1, 0, "##");

        glass.drop(figure, WIDTH / 2, HEIGHT);

        assertFalse(glass.accept(figure, WIDTH / 2 + 1, 0));
        assertFalse(glass.accept(figure, WIDTH / 2, 0));
        assertFalse(glass.accept(figure, WIDTH / 2 - 1, 0));
    }

    @Test
    public void shouldAcceptWhenFigureAboveDropped() {
        glass.drop(new TetrisFigure(1, 0, "##"), WIDTH / 2, HEIGHT);

        assertTrue(glass.accept(new TetrisFigure(1, 0, "##"), WIDTH / 2, 1));
    }

    @Test
    public void shouldRejectWhenFigurePartlyOverDroppedColumn() {
        TetrisFigure figure = new TetrisFigure(0, 1, "#", "#");

        glass.drop(figure, WIDTH / 2, HEIGHT);

        assertFalse(glass.accept(figure, WIDTH / 2, 0));
        assertFalse(glass.accept(figure, WIDTH / 2, 1));
        assertTrue(glass.accept(figure, WIDTH / 2, 2));
    }

    @Test
    public void shouldRejectWhenFigureOverlapDroppedColumnByBottomSize() {
        TetrisFigure figure = new TetrisFigure(0, 0, "#", "#");

        glass.drop(figure, WIDTH / 2, HEIGHT);

        assertFalse(glass.accept(figure, WIDTH / 2, 2));
    }

    @Test
    public void shouldRejectWhenFigureIsNotSymmetric() {
        TetrisFigure figure = new TetrisFigure(1, 0, "##", " #");

        glass.drop(figure, WIDTH / 2, HEIGHT);

        assertFalse(glass.accept(point, WIDTH / 2 - 1, 1));
    }

    @Test
    public void shouldRejectWhenFigureIsNotSymmetricPositive() {
        TetrisFigure figure = new TetrisFigure(1, 0, "##", " #");

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
        glass.drop(new TetrisFigure(0, 0, "##"), -1, HEIGHT - 1);

        assertTrue(glass.accept(point, 0, 0));
    }

    @Test
    public void shouldIgnoreWhenOutOfBoundsLeft() {
        glass.drop(new TetrisFigure(1, 0, "##"), 0, HEIGHT - 1);

        assertTrue(glass.isEmpty());
    }

    @Test
    public void shouldIgnoreWhenOutOfBoundsRightPartially() {
        glass.drop(new TetrisFigure(0, 0, "##"), WIDTH - 1, HEIGHT - 1);

        assertTrue(glass.isEmpty());
    }

    @Test
    public void shouldIgnoreWhenOutOfBoundsBottom() {
        glass.drop(new TetrisFigure(0, 0, "#", "#"), 0, 0);

        assertTrue(glass.isEmpty());
    }

    @Test
    public void shouldRejectWhenPartiallyOutsideOnRight() {
        assertFalse(glass.accept(new TetrisFigure(0, 0, "##"), WIDTH - 1, 0));
    }

    @Test
    public void shouldReturnPlotCoordinateSimpleFigure() {
        glass.figureAt(point, 1, 1);

        Plot plot = glass.getCurrentFigurePlots().get(0);
        assertContainsPlot(1, 1, PlotColor.BLUE, plot);
    }

    @Test
    public void shouldReturnPlotCoordinateSimpleRedFigure() {
        glass.figureAt(createLine(Figure.Type.Z, "#"), 1, 1);

        Plot plot = glass.getCurrentFigurePlots().get(0);
        assertContainsPlot(1, 1, PlotColor.RED, plot);
    }

    @Test
    public void shouldReturnPlotCoordinateHorizontalFigure() {
        glass.figureAt(new TetrisFigure(1, 0, "###"), 1, 1);

        List<Plot> plots = glass.getCurrentFigurePlots();
        TestUtils.assertContainsPlot(1 - 1, 1, PlotColor.BLUE, plots);
        TestUtils.assertContainsPlot(1, 1, PlotColor.BLUE, plots);
        TestUtils.assertContainsPlot(1 + 1, 1, PlotColor.BLUE, plots);
    }

    @Test
    public void shouldReturnPlotCoordinateVerticalFigure() {
        glass.figureAt(new TetrisFigure(0, 1, "#", "#", "#"), 1, 3);

        List<Plot> plots = glass.getCurrentFigurePlots();
        TestUtils.assertContainsPlot(1, 3 + 1, PlotColor.BLUE, plots);
        TestUtils.assertContainsPlot(1, 3, PlotColor.BLUE, plots);
        TestUtils.assertContainsPlot(1, 3 - 1, PlotColor.BLUE, plots);
    }

    @Test
    public void shouldReturnPlotCoordinateAsymetricFigure() {
        glass.figureAt(new TetrisFigure(1, 0, " #"), 1, 0);

        List<Plot> plots = glass.getCurrentFigurePlots();
        assertEquals(1, plots.size());
        TestUtils.assertContainsPlot(1, 0, PlotColor.BLUE, plots);
    }

    @Test
    public void shouldReturnPlotCoordinateAsymetricFigure2() {
        glass.figureAt(new TetrisFigure(0, 0, " #"), 1, 0);

        List<Plot> plots = glass.getCurrentFigurePlots();
        assertEquals(1, plots.size());
        TestUtils.assertContainsPlot(1 + 1, 0, PlotColor.BLUE, plots);
    }

    @Test
    public void shouldReturnEmptyArrayWhenNoFigure() {
        assertTrue(glass.getCurrentFigurePlots().isEmpty());
    }

    @Test
    public void shouldReturnPlotOfDroppedFigure() {
        glass.drop(point, 0, HEIGHT);

        List<Plot> plots = glass.getDroppedPlots();
        TestUtils.assertContainsPlot(0, 0, PlotColor.BLUE, plots);
    }

    @Test
    public void shouldReturnPlotOfDroppedFigure2() {
        glass.drop(new TetrisFigure(1, 1, "##", "##"), 3, HEIGHT);

        List<Plot> plots = glass.getDroppedPlots();
        TestUtils.assertContainsPlot(3 - 1, 1, PlotColor.BLUE, plots);
        TestUtils.assertContainsPlot(3, 1, PlotColor.BLUE, plots);
        TestUtils.assertContainsPlot(3 - 1, 0, PlotColor.BLUE, plots);
        TestUtils.assertContainsPlot(3, 0, PlotColor.BLUE, plots);
    }


    @Test
    public void shouldEmptyWhenRequested() {
        glass.drop(point, 0, 0);

        glass.empty();

        assertTrue(glass.getDroppedPlots().isEmpty());
    }

    @Test
    public void shouldRemoveFilledLineAfterDrop() {
        glass.drop(glassWidthFigure, 0, 0);

        assertTrue(glass.isEmpty());
    }

    @Test
    public void shouldRemoveFilledLineWhenSeveralFilled() {
        TetrisFigure columnFigure = new TetrisFigure(0, 0, "#", "#");
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

        glass.drop(new TetrisFigure(0, 0, "#", "#"), WIDTH - 1, HEIGHT);

        assertTrue(glass.accept(glassWidthFigure, 0, 1));
    }

    @Test
    public void shouldNotifyWhenLineRemoved() {
        glass.drop(glassWidthFigure, 0, HEIGHT);

        verify(glassEventListener).linesRemoved(removedLines.capture());
        assertEquals(1, removedLines.getValue().intValue());
    }

    @Test
    public void shouldNotifyWhenSeveralLinesRemoved() {
        glass.drop(line9Width, 0, HEIGHT);
        glass.drop(line9Width, 0, HEIGHT);

        glass.drop(new TetrisFigure(0, 0, "#", "#"), WIDTH - 1, HEIGHT);

        verify(glassEventListener).linesRemoved(removedLines.capture());
        assertEquals(2, removedLines.getValue().intValue());
    }

    @Test
    public void shouldNotifyScoreBoardWhenDropped() {
        glass.drop(point, 0, HEIGHT);

        verify(glassEventListener).figureDropped(droppedFigure.capture());
        assertSame(point, droppedFigure.getValue());
    }

    @Test
    public void shouldTriggerListenerWhenOverflow() {
        glass.empty();

        verify(glassEventListener).glassOverflown();
    }

    @Test
    public void shouldTriggerAllListenersWhenOverflow() {
        TetrisGlass glass = new TetrisGlass(WIDTH, HEIGHT, glassEventListener, glassEventListener);

        glass.empty();

        verify(glassEventListener, times(2)).glassOverflown();
    }

    @Test
    public void shouldNotifyAllListenersScoreBoardWhenDropped() {
        TetrisGlass glass = new TetrisGlass(WIDTH, HEIGHT, glassEventListener, glassEventListener);

        glass.drop(point, 0, HEIGHT);

        verify(glassEventListener, times(2)).figureDropped(droppedFigure.capture());
    }

    @Test
    public void shouldNotifyAllListenersScoreBoardWhenLinesRemoved() {
        TetrisGlass glass = new TetrisGlass(WIDTH, HEIGHT, glassEventListener, glassEventListener);

        glass.drop(glassWidthFigure, 0, HEIGHT);

        verify(glassEventListener, times(2)).figureDropped(droppedFigure.capture());
    }

    @Test
    public void shouldQuietWhenDropOutOfBounds() {
        glass.drop(createVerticalFigure(HEIGHT), WIDTH / 2, HEIGHT);

        try {
            glass.drop(Figure.Type.I.createNewFigure(), WIDTH / 2, HEIGHT);
            verify(glassEventListener, times(1)).figureDropped(Matchers.<Figure>any());
        } catch (Exception e) {
            fail("Was exception: " + e);
        }
    }

    @Test
    public void shouldRemainWhenDropped_FoundInIntegrationTesting() {
        TetrisFigure twoRowsFigure = new TetrisFigure(0, 0, "#", "#");
        glass.drop(twoRowsFigure, WIDTH / 2, HEIGHT);

        glass.drop(twoRowsFigure, WIDTH / 2, HEIGHT);

        assertFalse(glass.accept(point, WIDTH / 2, 2));
        assertFalse(glass.accept(point, WIDTH / 2, 3));
    }

    @Test
    public void shouldBeOccupiedWhenLongFigureDroppedOnTopOfGlass() {
        glass.drop(createVerticalFigure(HEIGHT - 2), CENTER_X, TOP_Y);
        Figure figure = Figure.Type.J.createNewFigure();
        glass.drop(figure, CENTER_X, TOP_Y);

        assertFalse(glass.accept(figure, CENTER_X, TOP_Y));
    }

    @Test
    public void shouldStoreColorsWhenRedFigure() {
        glass.drop(createLine(Figure.Type.Z, "#"), CENTER_X, TOP_Y);

        assertEquals(PlotColor.RED, glass.getDroppedPlots().get(0).getColor());
    }

    @Test
    public void shouldNotAcceptWhenDroppedYellowFigure_int_overflow() {
        glass.drop(createLine(Figure.Type.O, "#"), 0, TOP_Y);

        assertFalse(glass.accept(point, 0, 0));
    }

    @Test
    public void shouldRemoveLinesWhenDroppedDifferentColors() {
        glass.drop(createLine(Figure.Type.I, "#########"), 1, TOP_Y);
        glass.drop(createLine(Figure.Type.J, "#"), 0, TOP_Y);

        verify(glassEventListener).linesRemoved(removedLines.capture());
        assertEquals(1, removedLines.getValue().intValue());
        assertTrue(glass.accept(point, 0, 0));
    }

    @Test
    public void shouldRemoveLinesInBetweenWhenDropped() {
        glass.drop(createLine("# ########"), 0, TOP_Y);
        glass.drop(createLine("##########"), 0, TOP_Y);

        verify(glassEventListener).linesRemoved(removedLines.capture());
        assertEquals(1, removedLines.getValue().intValue());
        assertFalse(glass.accept(point, 0, 0));
        assertTrue(glass.accept(point, 1, 0));
    }

    private TetrisFigure createLine(Figure.Type type, String ... lines) {
        return new TetrisFigure(0, 0, type, lines);
    }

    private TetrisFigure createLine(String ... lines) {
        return createLine(Figure.Type.I, lines);
    }

    private TetrisFigure createVerticalFigure(int height) {
        String[] verticalLine = new String[height];
        Arrays.fill(verticalLine, "#");
        return new TetrisFigure(0, 0, verticalLine);
    }

    private void drop(TetrisFigure lineFigure, int times) {
        for (int i = 0; i < times; i++) {
            glass.drop(lineFigure, 0, HEIGHT);
        }
    }


    private void assertContainsPlot(final int x, final int y, final PlotColor color, Plot... plots) {
        TestUtils.assertContainsPlot(x, y, color, Arrays.asList(plots));
    }
}
