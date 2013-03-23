package com.codenjoy.dojo.services;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 3/23/13
 * Time: 3:53 PM
 */
public class GuiPlotColorDecoderTest {

    enum PlotColor {
        ONE('1'), TWO('2'), THREE('3'), FOUR('4');
        private char c;

        PlotColor(char c) {
            this.c = c;
        }

        @Override
        public String toString() {
            return String.valueOf(c);
        }
    }

    @Test
    public void shouldWork() {
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(PlotColor.values());

        assertEquals("ABCD", decoder.encode("1234"));
        assertEquals("DCBA", decoder.encode("4321"));
    }
}
