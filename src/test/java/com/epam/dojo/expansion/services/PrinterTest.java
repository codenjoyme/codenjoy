package com.epam.dojo.expansion.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 EPAM
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


import com.epam.dojo.expansion.model.levels.CellImpl;
import com.epam.dojo.expansion.model.Elements;
import com.epam.dojo.expansion.model.levels.Cell;
import com.epam.dojo.expansion.model.levels.items.Hero;
import com.epam.dojo.expansion.model.levels.items.HeroForces;
import com.epam.dojo.expansion.model.levels.items.Start;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Oleksandr_Baglai on 2017-09-05.
 */
public class PrinterTest {

    @Test
    public void shouldCountNotMoreThan() {
        assertEquals("-=#", Printer.makeForceState(new Start(Elements.BASE1)));
        assertEquals("000", Printer.makeForceState(forces(0)));
        assertEquals("001", Printer.makeForceState(forces(1)));
        assertEquals("ZZZ", Printer.makeForceState(forces(46655)));
        assertEquals("000", Printer.makeForceState(forces(46656)));
        assertEquals("001", Printer.makeForceState(forces(46657)));
        assertEquals("GIF", Printer.makeForceState(forces(1234455)));
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