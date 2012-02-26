package net.tetris.dom;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TetrisGlassTest {

    public static final int HEIGHT = 20;
    private TetrisGlass glass;
    private int width;

    @Before
    public void setUp() throws Exception {
        width = 10;
        glass = new TetrisGlass(width, HEIGHT);
    }

    @Test
    public void shouldAcceptWhenEmpty(){
        assertTrue(glass.accept(new DummyFigure(), 1, 1));
    }

    @Test
    public void shouldRejectWhenAcceptingOnDroppedPlace() {
        glass.drop(new DummyFigure(), 0, 0);

        assertFalse(glass.accept(new DummyFigure(), 0, 0));
    }

    @Test
    public void shouldPerformDropWhenDropRequested(){
        glass.drop(new DummyFigure(), 0, HEIGHT);

        assertFalse(glass.accept(new DummyFigure(), 0, 0));
    }

    private static class DummyFigure implements Figure {
        public int getLeft() {
            return 0;
        }

        public int getRight() {
            return 0;
        }

        public int getTop() {
            return 0;
        }

        public int getBottom() {
            return 0;
        }
    }
}
