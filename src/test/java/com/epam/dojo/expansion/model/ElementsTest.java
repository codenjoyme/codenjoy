package com.epam.dojo.expansion.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Oleksandr_Baglai on 2017-09-01.
 */
public class ElementsTest {

    @Test
    public void testGetForce() {
        assertEquals(Elements.FORCE1, Elements.getForce(0));
        assertEquals(Elements.FORCE2, Elements.getForce(1));
        assertEquals(Elements.FORCE3, Elements.getForce(2));
        assertEquals(Elements.FORCE4, Elements.getForce(3));

        assertEquals(Elements.FORCE1, Elements.valueOf('♥'));
        assertEquals(Elements.FORCE2, Elements.valueOf('♦'));
        assertEquals(Elements.FORCE3, Elements.valueOf('♣'));
        assertEquals(Elements.FORCE4, Elements.valueOf('♠'));
    }

    @Test
    public void testGetAllElements() {
        Elements.valueOf('♥'); // because of lazy
        assertEquals("{.=., └=└, -=-, ┐=┐, ┌=┌, $=$, O=O,  = , " +
                "│=│, ─=─, G=G, ╝=╝, F=F, E=E, ╚=╚, B=B, ╗=╗, " +
                "╔=╔, ║=║, ═=═, ♦=♦, ♥=♥, 4=4, 3=3, ♣=♣, 2=2, " +
                "1=1, ┘=┘, ♠=♠}", Elements.elementsMap.toString());
    }
}
