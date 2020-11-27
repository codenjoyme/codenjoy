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
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.function.Supplier;

import static com.codenjoy.dojo.tetris.model.Type.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FiguresTest {

    private Type[] allTypes;
    private Dice dice;

    @Before
    public void setUp() throws Exception {
        allTypes = new Type[]{I, O, J, L, S, Z, T};
        dice = mock(Dice.class);
    }

    @Test
    public void shouldCheckRandomizerForNextFigure(){
        Randomizer randomizer = mock(Randomizer.class);
        Supplier<Randomizer> randomizerFetcher = mock(Supplier.class);
        when(randomizerFetcher.get()).thenReturn(randomizer);
        Figures figures = new Figures(4);
        figures.setRandomizer(randomizerFetcher);

        when(randomizer.getNextNumber(anyInt())).thenReturn(3);
        figures.open(allTypes);

        Figure next = figures.next();
        assertEquals(allTypes[3], next.type());
    }

    @Test
    public void shouldReturnFutureFigures(){
        Figures figures = new Figures(4);
        figures.setRandomizer(getFetcher(new EquiprobableRandomizer(dice)));
        figures.open(allTypes);

        List<Type> types = figures.future();

        assertEquals(4, types.size());
        assertTypesEqual(figures, types, 0);
        assertTypesEqual(figures, types, 1);
        assertTypesEqual(figures, types, 2);
        assertTypesEqual(figures, types, 3);
    }

    private Supplier<Randomizer> getFetcher(Randomizer random) {
        Supplier<Randomizer> randomizer = mock(Supplier.class);
        when(randomizer.get()).thenReturn(random);
        return randomizer;
    }

    @Test
    public void shouldOpenNewFituresAfterFutureIsEmpty(){
        Figures figures = new Figures(1);
        figures.setRandomizer(getFetcher(new EquiprobableRandomizer(dice)));
        figures.open(I);

        figures.open(J);

        assertEquals(I, figures.future().get(0));
        assertEquals(I, figures.next().type());
        assertEquals(J, figures.future().get(0));
        assertEquals(J, figures.next().type());
    }

    private void assertTypesEqual(Figures figures, List<Type> futureFigures, int index) {
        assertEquals(figures.next().type(), futureFigures.get(index));
    }


}
