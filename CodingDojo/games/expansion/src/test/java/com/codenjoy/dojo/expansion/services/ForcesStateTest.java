package com.codenjoy.dojo.expansion.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.expansion.model.Elements;
import com.codenjoy.dojo.expansion.model.levels.Cell;
import com.codenjoy.dojo.expansion.model.levels.CellImpl;
import com.codenjoy.dojo.expansion.model.levels.items.ForcesState;
import com.codenjoy.dojo.expansion.model.levels.items.Hero;
import com.codenjoy.dojo.expansion.model.levels.items.HeroForces;
import com.codenjoy.dojo.expansion.model.levels.items.Start;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Oleksandr_Baglai on 2017-09-05.
 */
public class ForcesStateTest {

    @Test
    public void shouldCountNotMoreThan() {
        Assert.assertEquals("-=#", new ForcesState(new Start(Elements.BASE1)).state(null, null));
        assertEquals("000", new ForcesState(forces(0)).state(null, null));
        assertEquals("001", new ForcesState(forces(1)).state(null, null));
        assertEquals("ZZZ", new ForcesState(forces(46655)).state(null, null));
        assertEquals("000", new ForcesState(forces(46656)).state(null, null));
        assertEquals("001", new ForcesState(forces(46657)).state(null, null));
        assertEquals("GIF", new ForcesState(forces(1234455)).state(null, null));
        assertEquals("046", new ForcesState(forces(150)).state(null, null));
        assertEquals("03W", new ForcesState(forces(140)).state(null, null));
    }

    @NotNull
    private HeroForces forces(final int count) {
        Hero hero = new Hero() {
            @Override
            public Start getBase() {
                return new Start(Elements.BASE1);
            }
        };
        return new HeroForces(hero, count) {
            @Override
            public Cell getCell() {
                return new CellImpl(0, 0);
            }
        };
    }

}