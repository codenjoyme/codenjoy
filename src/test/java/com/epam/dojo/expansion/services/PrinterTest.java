package com.epam.dojo.expansion.services;

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