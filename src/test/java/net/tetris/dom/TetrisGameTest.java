package net.tetris.dom;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;

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
    public static final int HEIGHT = 20;
    @Mock GameConsole console;
    @Mock FigureQueue queue;
    @Mock ScoreBoard scoreBoard;
    @Mock Glass glass;
    @Captor ArgumentCaptor<Integer> xCaptor;
    @Captor ArgumentCaptor<Integer> yCaptor;
    @Captor ArgumentCaptor<Figure> figureCaptor;

    //field value will be initialized in GameSetupRule
    private TetrisGame game;

    @Rule public GameSetupRule gameSetup = new GameSetupRule();
    

    @Test
    @GivenFiguresInQueue({@FigureProperties})
    public void shouldBeInInitialPositionWhenGameStarts() {
        assertCoordinates(CENTER_X, TOP_Y);
    }

    @Test
    @GivenFiguresInQueue({@FigureProperties})
    public void shouldBeMovedLeftWhenAsked() {
        game.moveLeft(2);
        game.nextStep();

        assertCoordinates(CENTER_X - 2, TOP_Y - 1);
    }

    @Test
    @GivenFiguresInQueue({@FigureProperties})
    public void shouldChangePositionWhenOnlyNextStep(){
        game.moveLeft(2);

        assertCoordinates(CENTER_X, TOP_Y);
    }

    @Test
    @GivenFiguresInQueue({@FigureProperties})
    public void shouldBeMovedRightWhenAsked() {
        game.moveRight(2);
        game.nextStep();

        assertCoordinates(CENTER_X + 2, TOP_Y - 1);
    }

    @Test
    @GivenFiguresInQueue({@FigureProperties})
    public void shouldNotChangeCurrentFigureWhenNextStep(){
        game.nextStep();

        captureFigureAtValues();
        List<Figure> allFigures = figureCaptor.getAllValues();
        assertSame(allFigures.get(0), allFigures.get(1));
    } 

    @Test
    @GivenFiguresInQueue({@FigureProperties})
    public void shouldNotMoveOutWhenLeftSide(){
        game.moveLeft(CENTER_X + 1);
        game.nextStep();

        assertCoordinates(0, TOP_Y - 1);
    }

    @Test
    @GivenFiguresInQueue({@FigureProperties})
    public void shouldNotMoveOutWhenRightSide(){
        game.moveRight(CENTER_X + 2);
        game.nextStep();

        assertCoordinates(9, TOP_Y - 1);
    }

    @Test
    @GivenFiguresInQueue({@FigureProperties(left = 1)})
    public void shouldIncludeFigureSizeWhenMoveLeft() {
        game.moveLeft(CENTER_X + 1);
        game.nextStep();

        assertCoordinates(1, HEIGHT - 1);
    }

    @Test
    @GivenFiguresInQueue({@FigureProperties(right = 1)})
    public void shouldIncludeFigureSizeWhenMoveRight() {
        game.moveRight(CENTER_X + 1);
        game.nextStep();

        assertCoordinates(9 - 1, HEIGHT - 1);
    }

    @Test
    @GivenFiguresInQueue({@FigureProperties(top = 1)})
    public void shouldAdjustFigurePositionWhenFigureHasTopCells(){
        assertCoordinates(CENTER_X, HEIGHT - 1);
    }

    @Test
    @GivenFiguresInQueue({@FigureProperties(bottom = 1), @FigureProperties(bottom = 2)})
    public void shouldTakeNextFigureWhenCurrentIsDropped() {
        game.drop();
        game.nextStep();
        game.nextStep();

        assertCoordinates(CENTER_X, HEIGHT);
        assertEquals(2, figureCaptor.getValue().getBottom());
    }
    
    @Test
    @GivenFiguresInQueue({@FigureProperties(bottom = HEIGHT), @FigureProperties(bottom = 1)})
    public void shouldGameOverWhenGlassOverflown() {
        glassToRejectFigure();
        game.drop();
        game.nextStep();

        assertCoordinates(CENTER_X, HEIGHT);
        verify(scoreBoard).glassOverflown();
        verify(glass).drop(Matchers.<Figure>anyObject(), eq(CENTER_X), eq(HEIGHT));
        verify(glass).empty();
    }

    private OngoingStubbing<Boolean> glassToRejectFigure() {
        return when(glass.accept(Matchers.<Figure>anyObject(), anyInt(), anyInt())).thenReturn(false);
    }

    @Test
    @GivenFiguresInQueue({@FigureProperties})
    public void shouldBeAtSameCoordinatesWhenDropRequested(){
        game.drop();
        game.moveLeft(1);
        game.nextStep();
        
        assertCoordinates(CENTER_X, HEIGHT);
    } 

    @Test
    @GivenFiguresInQueue({@FigureProperties})
    public void shouldMoveLeftWhenAcceptOnly(){
        glassToRejectFigure();
        game.moveLeft(1);
        game.nextStep();

        assertCoordinates(CENTER_X, HEIGHT - 1);
    }

    @Test
    @GivenFiguresInQueue({@FigureProperties})
    public void shouldMoveRightWhenAcceptOnly(){
        glassToRejectFigure();
        game.moveRight(1);
        game.nextStep();

        assertCoordinates(CENTER_X, HEIGHT - 1);
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
