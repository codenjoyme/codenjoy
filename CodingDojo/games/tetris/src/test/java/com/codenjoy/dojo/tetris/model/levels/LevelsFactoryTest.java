package com.codenjoy.dojo.tetris.model.levels;

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
import com.codenjoy.dojo.tetris.model.Figures;
import com.codenjoy.dojo.tetris.model.Levels;
import com.codenjoy.dojo.tetris.model.levels.level.HardLevels;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class LevelsFactoryTest {

    private LevelsFactory factory = new LevelsFactory();

    @Test
    public void shouldReturnAllLevels(){
        List<String> list = factory.allLevels();
        assertEquals("[AllFigureLevels, EasyLevels, HardLevels, " +
                "ProbabilityLevels, ProbabilityWithoutOverflownLevels]", list.toString());
    }

    @Test
    public void shouldLoadClass(){
        Levels loaded = factory.createLevels("HardLevels", mock(Dice.class),
                mock(Figures.class)
        );

        assertEquals(HardLevels.class, loaded.getClass());
    }

}
