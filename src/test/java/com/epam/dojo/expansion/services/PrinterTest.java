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


import com.epam.dojo.expansion.model.Cell;
import com.epam.dojo.expansion.model.Elements;
import com.epam.dojo.expansion.model.interfaces.ICell;
import com.epam.dojo.expansion.model.items.Hero;
import com.epam.dojo.expansion.model.items.HeroForces;
import com.epam.dojo.expansion.model.items.Start;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Oleksandr_Baglai on 2017-09-05.
 */
public class PrinterTest {

    @Test
    public void shouldCountNotMoreThan() {
        assertEquals("-=#", Printer.makeForceState(new Start(Elements.BASE1), null));
        assertEquals("000", Printer.makeForceState(forces(0), null));
        assertEquals("001", Printer.makeForceState(forces(1), null));
        assertEquals("ZZZ", Printer.makeForceState(forces(46655), null));
        assertEquals("000", Printer.makeForceState(forces(46656), null));
        assertEquals("001", Printer.makeForceState(forces(46657), null));
        assertEquals("GIF", Printer.makeForceState(forces(1234455), null));
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
            public ICell getCell() {
                return new Cell(0, 0);
            }
        };
    }

}