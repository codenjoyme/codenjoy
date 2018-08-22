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
import com.codenjoy.dojo.tetris.model.Levels;
import com.codenjoy.dojo.tetris.model.LevelsFactory;
import com.codenjoy.dojo.tetris.model.PlayerFigures;
import org.junit.Test;

import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class LevelsFactoryTest {

    private LevelsFactory factory = new LevelsFactory();

    @Test
    public void shouldReturnAllLevels(){
        Set<Class<? extends Levels>> levels = factory.getAllLevelsInPackage();

        assertThat(levels).contains(EasyLevels.class, HardLevels.class, AllFigureLevels.class);
    }

    @Test
    public void shouldLoadClass(){
        Levels loaded = factory.getGameLevels(mock(Dice.class),
                mock(PlayerFigures.class),
                "HardLevels");

        assertEquals(HardLevels.class, loaded.getClass());
    }

}
