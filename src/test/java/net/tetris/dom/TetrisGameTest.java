package net.tetris.dom;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * @author serhiy.zelenin
 */
@RunWith(MockitoJUnitRunner.class)
public class TetrisGameTest {
    @Mock GameConsole console;
    @Captor ArgumentCaptor<Integer> xCaptor;
    @Captor ArgumentCaptor<Integer> yCaptor;
    @Captor ArgumentCaptor<Figure> figureCaptor;
    private TetrisGame game;

    @Before
    public void setUp() throws Exception {
        game = new TetrisGame(console);
    }

    @Test
    public void shouldBeInInitialPositionWhenGameStarts() {
        verify(console).figureAt(figureCaptor.capture(), xCaptor.capture(), yCaptor.capture());

        assertEquals(5, xCaptor.getValue().intValue());
        assertEquals(20, yCaptor.getValue().intValue());
    }

    @Test
    public void should(){

    }
}
