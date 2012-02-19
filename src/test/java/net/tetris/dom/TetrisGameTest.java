package net.tetris.dom;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * @author serhiy.zelenin
 */
@RunWith(MockitoJUnitRunner.class)
public class TetrisGameTest {
    public static final int CENTER_X = 10/2 - 1;
    public static final int TOP_Y = 20;
    public static final int GLASS_HEIGHT = 20;
    @Mock GameConsole console;
    @Mock FigureQueue queue;
    @Captor ArgumentCaptor<Integer> xCaptor;
    @Captor ArgumentCaptor<Integer> yCaptor;
    @Captor ArgumentCaptor<Figure> figureCaptor;

    private TetrisGame game;

    @Before
    public void setUp() throws Exception {
        game = new TetrisGame(console, queue);
    }

    @Test
    public void shouldBeInInitialPositionWhenGameStarts() {
        assertCoordinates(CENTER_X, TOP_Y);
    }

    @Test
    public void shouldBeMovedLeftWhenAsked() {
        game.moveLeft(2);
        game.nextStep();

        assertCoordinates(CENTER_X - 2, TOP_Y - 1);
    }

    @Test
    public void shouldChangePositionWhenOnlyNextStep(){
        game.moveLeft(2);

        assertCoordinates(CENTER_X, TOP_Y);
    }

    @Test
    public void shouldBeMovedRightWhenAsked() {
        game.moveRight(2);
        game.nextStep();

        assertCoordinates(CENTER_X + 2, TOP_Y - 1);
    }

    @Test
    public void shouldNotChangeCurrentFigureWhenNextStep(){
        game.nextStep();

        captureFigureAtValues();
        List<Figure> allFigures = figureCaptor.getAllValues();
        assertSame(allFigures.get(0), allFigures.get(1));
    } 

    @Test
    public void shouldNotMoveOutWhenLeftSide(){
        game.moveLeft(CENTER_X + 1);
        game.nextStep();

        assertCoordinates(0, TOP_Y - 1);
    }

    @Test
    public void shouldNotMoveOutWhenRightSide(){
        game.moveRight(CENTER_X + 2);
        game.nextStep();

        assertCoordinates(9, TOP_Y - 1);
    }

    @Test
    public void shouldTakeNextFigureWhenGameStarts() {
        IFigure figure = new IFigure();
        when(queue.next()).thenReturn(figure);
        new TetrisGame(console, queue);
        captureFigureAtValues();

        assertSame(figure, figureCaptor.getValue());
    } 


    @Test
    @Ignore
    public void shouldIncludeFigureSizeWhenMove() {
    }

    @Test
    @Ignore
    public void shouldRotateFigureWhenRotateCommand(){
    }


    private void assertCoordinates(int x, int y) {
        captureFigureAtValues();
        assertEquals(x, xCaptor.getValue().intValue());
        assertEquals(y, yCaptor.getValue().intValue());
    }

    private void captureFigureAtValues() {
        verify(console, atLeastOnce()).figureAt(figureCaptor.capture(), xCaptor.capture(), yCaptor.capture());
    }
}
