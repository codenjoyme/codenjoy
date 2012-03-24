package net.tetris.dom;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TetrisGlassTest {

    public static final int HEIGHT = 20;
    private static int WIDTH = 10;
    private TetrisGlass glass;

    @Before
    public void setUp() throws Exception {
        glass = new TetrisGlass(WIDTH, HEIGHT);
    }

    @Test
    public void shouldAcceptWhenEmpty(){
        assertTrue(glass.accept(new TetrisFigure(), 1, 1));
    }

    @Test
    public void shouldRejectWhenAcceptingOnDroppedPlace() {
        glass.drop(new TetrisFigure(), 0, 0);

        assertFalse(glass.accept(new TetrisFigure(), 0, 0));
    }

    @Test
    public void shouldPerformDropWhenDropRequested(){
        glass.drop(new TetrisFigure(), 0, HEIGHT);

        assertFalse(glass.accept(new TetrisFigure(), 0, 0));
    }

    @Test
    public void shouldRejectWhenFigurePartlyOutsideFromLeft(){
        assertFalse(glass.accept(new TetrisFigure(1, 0, "##"), 0, HEIGHT));
    }

    @Test
    public void shouldRejectWhenFigurePartlyOutsideFromRight(){
        assertFalse(glass.accept(new TetrisFigure(0, 0, "##"), WIDTH, HEIGHT));
    }

    @Test
    public void shouldRejectWhenFigurePartlyOutsideBottom(){
        assertFalse(glass.accept(new TetrisFigure(0, 0, "#", "#"), WIDTH, 0));
    }

    @Test
    public void shouldRejectWhenFigurePartlyOverDropped(){
        glass.drop(new TetrisFigure(1, 0, "##"), WIDTH/2, HEIGHT);
        
        assertFalse(glass.accept(new TetrisFigure(1, 0, "##"), WIDTH/2 + 1, 0));
    } 

}
