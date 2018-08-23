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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.tetris.model.levels.random.EquiprobableRandomizer;
import com.codenjoy.dojo.tetris.model.levels.random.Randomizer;
import com.codenjoy.dojo.tetris.model.levels.random.RandomizerFetcher;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.codenjoy.dojo.tetris.model.Figure.Type.*;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerFiguresTest {

    private Figure.Type[] allTypes;
    private Dice dice;

    @Before
    public void setUp() throws Exception {
        allTypes = new Figure.Type[]{I, O, J, L, S, Z, T};
        dice = mock(Dice.class);
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
        Randomizer randomizer = new EquiprobableRandomizer(dice);
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
        Randomizer randomizer = new EquiprobableRandomizer(dice);
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
