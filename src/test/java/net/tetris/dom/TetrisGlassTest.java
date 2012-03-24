package net.tetris.dom;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

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
}
