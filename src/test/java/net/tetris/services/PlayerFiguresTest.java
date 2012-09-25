package net.tetris.services;

import net.tetris.dom.Figure;
import net.tetris.services.randomizer.Randomizer;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static net.tetris.dom.Figure.Type.*;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerFiguresTest {

    @Test
    public void shouldCheckRandomizerForNextFigure(){
        Randomizer randomizer = mock(Randomizer.class);
        PlayerFigures playerFigures = new PlayerFigures(randomizer);

        Figure.Type[] types = {I, O, J, L, S, Z, T};
        when(randomizer.getNextNumber(anyInt())).thenReturn(3);
        playerFigures.openFigures(types);

        Figure next = playerFigures.next();
        assertEquals(types[3], next.getType());
    }



}
