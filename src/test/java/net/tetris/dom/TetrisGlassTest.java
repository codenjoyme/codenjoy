package net.tetris.dom;

import net.tetris.services.Plot;
import net.tetris.services.PlotColor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.*;

public class TetrisGlassTest {

    public static final int HEIGHT = 20;
    private static int WIDTH = 10;
    private TetrisGlass glass;
    private TetrisFigure point;

    @Before
    public void setUp() throws Exception {
        glass = new TetrisGlass(WIDTH, HEIGHT);
        point = new TetrisFigure();
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

        Plot plot = glass.getPlots().get(0);
        assertContainsPlot(1, 1, PlotColor.CYAN, plot);
    }

    @Test
    public void shouldReturnPlotCoordinateHorizontalFigure() {
        glass.figureAt(new TetrisFigure(1, 0, "###"), 1, 1);

        List<Plot> plots = glass.getPlots();
        assertContainsPlot(1 - 1, 1, PlotColor.CYAN, plots);
        assertContainsPlot(1, 1, PlotColor.CYAN, plots);
        assertContainsPlot(1 + 1, 1, PlotColor.CYAN, plots);
    }

    @Test
    public void shouldReturnPlotCoordinateVerticalFigure() {
        glass.figureAt(new TetrisFigure(0, 1, "#", "#", "#"), 1, 3);

        List<Plot> plots = glass.getPlots();
        assertContainsPlot(1, 3 + 1, PlotColor.CYAN, plots);
        assertContainsPlot(1, 3, PlotColor.CYAN, plots);
        assertContainsPlot(1, 3 - 1, PlotColor.CYAN, plots);
    }

    @Test
    public void shouldReturnPlotCoordinateAsymetricFigure() {
        glass.figureAt(new TetrisFigure(1, 0, " #"), 1, 0);

        List<Plot> plots = glass.getPlots();
        assertEquals(1, plots.size());
        assertContainsPlot(1, 0, PlotColor.CYAN, plots);
    }

    @Test
    public void shouldReturnPlotCoordinateAsymetricFigure2() {
        glass.figureAt(new TetrisFigure(0, 0, " #"), 1, 0);

        List<Plot> plots = glass.getPlots();
        assertEquals(1, plots.size());
        assertContainsPlot(1 + 1, 0, PlotColor.CYAN, plots);
    }

    @Test
    public void shouldReturnPlotOfDroppedFigure() {
        glass.drop(point, 0, HEIGHT);

        List<Plot> plots = glass.getPlots();
        assertContainsPlot(0, 0, PlotColor.CYAN, plots);
    }

    @Test
    public void shouldReturnPlotOfDroppedFigure2() {
        glass.drop(new TetrisFigure(1, 1, "##", "##"), 3, HEIGHT);

        List<Plot> plots = glass.getPlots();
        assertContainsPlot(3-1, 1, PlotColor.CYAN, plots);
        assertContainsPlot(3, 1, PlotColor.CYAN, plots);
        assertContainsPlot(3-1, 0, PlotColor.CYAN, plots);
        assertContainsPlot(3, 0, PlotColor.CYAN, plots);
    }

    @Test
    public void shouldReturnPlotsOfCurrentAndDroppedFigures() {
        glass.drop(point, 3, HEIGHT);
        glass.figureAt(point, 1, 2);

        List<Plot> plots = glass.getPlots();
        assertEquals(2, plots.size());
        assertContainsPlot(3, 0, PlotColor.CYAN, plots);
        assertContainsPlot(1, 2, PlotColor.CYAN, plots);
    }


    private void assertContainsPlot(final int x, final int y, final PlotColor color, List<Plot> plots) {
        Object foundPlot = CollectionUtils.find(plots, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Plot plot = (Plot) object;
                return plot.getColor() == color && plot.getX() == x && plot.getY() == y;
            }
        });
        assertNotNull("Plot with coordinates (" + x + "," + y + ") color: " + color + " not found", foundPlot);
    }

    private void assertContainsPlot(final int x, final int y, final PlotColor color, Plot... plots) {
        assertContainsPlot(x, y, color, Arrays.asList(plots));
    }
}
