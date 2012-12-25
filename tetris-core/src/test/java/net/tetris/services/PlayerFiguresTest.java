package net.tetris.services;

import net.tetris.dom.Figure;
import net.tetris.services.randomizer.EquiprobableRandomizer;
import net.tetris.services.randomizer.Randomizer;
import net.tetris.services.randomizer.RandomizerFetcher;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static net.tetris.dom.Figure.Type.*;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerFiguresTest {

    private Figure.Type[] allTypes;

    @Before
    public void setUp() throws Exception {
        allTypes = new Figure.Type[]{I, O, J, L, S, Z, T};
    }

    @Test
    public void shouldCheckRandomizerForNextFigure(){
        Randomizer randomizer = mock(Randomizer.class);
        RandomizerFetcher randomizerFetcher = mock(RandomizerFetcher.class);
        when(randomizerFetcher.get()).thenReturn(randomizer);
        PlayerFigures playerFigures = new PlayerFigures(4);
        playerFigures.setRandomizerFetcher(randomizerFetcher);

        when(randomizer.getNextNumber(anyInt())).thenReturn(3);
        playerFigures.openFigures(allTypes);

        Figure next = playerFigures.next();
        assertEquals(allTypes[3], next.getType());
    }

    @Test
    public void shouldReturnFutureFigures(){
        Randomizer randomizer = new EquiprobableRandomizer();
        RandomizerFetcher randomizerFetcher = mock(RandomizerFetcher.class);
        when(randomizerFetcher.get()).thenReturn(randomizer);
        PlayerFigures playerFigures = new PlayerFigures(4);
        playerFigures.setRandomizerFetcher(randomizerFetcher);
        playerFigures.openFigures(allTypes);

        List<Figure.Type> figures = playerFigures.getFutureFigures();

        assertEquals(4, figures.size());
        assertTypesEqual(playerFigures, figures, 0);
        assertTypesEqual(playerFigures, figures, 1);
        assertTypesEqual(playerFigures, figures, 2);
        assertTypesEqual(playerFigures, figures, 3);
    }

    @Test
    public void shouldOpenNewFituresAfterFutureIsEmpty(){
        Randomizer randomizer = new EquiprobableRandomizer();
        RandomizerFetcher randomizerFetcher = mock(RandomizerFetcher.class);
        when(randomizerFetcher.get()).thenReturn(randomizer);
        PlayerFigures playerFigures = new PlayerFigures(1);
        playerFigures.setRandomizerFetcher(randomizerFetcher);
        playerFigures.openFigures(I);

        playerFigures.openFigures(J);

        assertEquals(I, playerFigures.getFutureFigures().get(0));
        assertEquals(I, playerFigures.next().getType());
        assertEquals(J, playerFigures.getFutureFigures().get(0));
        assertEquals(J, playerFigures.next().getType());
    }

    private void assertTypesEqual(PlayerFigures playerFigures, List<Figure.Type> futureFigures, int index) {
        assertEquals(playerFigures.next().getType(), futureFigures.get(index));
    }


}
