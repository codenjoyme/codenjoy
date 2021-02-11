package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.utils.JsonUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GuiPlotColorDecoderTest {

    enum Elements implements CharElements {
        ONE('1'),
        TWO('2'),
        THREE('3'),
        FOUR('4');

        private char ch;

        Elements(char ch) {
            this.ch = ch;
        }

        @Override
        public String toString() {
            return String.valueOf(ch);
        }

        @Override
        public char ch() {
            return ch;
        }
    }

    @Test
    public void shouldEncode() {
        // given
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Elements.values());

        // when then
        assertEquals("ABCD", decoder.encodeForBrowser("1234"));
        assertEquals("DCBA", decoder.encodeForBrowser("4321"));
    }

    @Test
    public void shouldToString() {
        // given when then
        assertEquals("[1234 -> ABCD]",
                new GuiPlotColorDecoder(Elements.values()).toString());

        assertEquals("[SHG -> ABC]",
                new GuiPlotColorDecoder(Elements1.values()).toString());

        assertEquals("[SH -> AB]",
                new GuiPlotColorDecoder(Elements2.values()).toString());
    }

    public enum Elements1 implements CharElements {
        STONE,
        HERO,
        GOLD;

        @Override
        public String toString() {
            return "" + ch();
        }

        @Override
        public char ch() {
            return super.toString().charAt(0);
        }
    }

    public enum Elements2 implements CharElements {
        SPACE,
        HERO;

        @Override
        public String toString() {
            return "" + ch();
        }

        @Override
        public char ch() {
            return super.toString().charAt(0);
        }
    }

    @Test
    public void shouldWorkWithAllSymbols() {
        // given
        GameType game1 = mock(GameType.class);
        when(game1.getPlots()).thenReturn(Elements1.values());

        GameType game2 = mock(GameType.class);
        when(game2.getPlots()).thenReturn(Elements2.values());

        // when then
        assertEncode(game1, "ABC");
        assertEncode(game2, "AB");
    }

    private void assertEncode(GameType game, String expected) {
        CharElements[] plots = game.getPlots();
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(plots);

        String plotsString = "";
        for (Object o : plots) {
            plotsString += o;
        }

        assertEquals(expected, decoder.encodeForBrowser(plotsString));
    }

    @Test
    public void shouldEncodeJsonWithLayers() {
        // given
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Elements.values());

        // when then
        assertEncode(decoder, "{'key1':'value1','key2':'value2','layers':['ABCD','DCBA','DCAB','DABC']}",
                "{'key1':'value1','layers':['1234','4321','4312','4123'],'key2':'value2'}");

        assertEncode(decoder, "{'key1':'value1','key2':'value2','layers':[]}",
                "{'key1':'value1','layers':[],'key2':'value2'}");

        assertEncode(decoder, "{'layers':[]}",
                "{'layers':[]}");

        assertEncode(decoder, "{'layers':['ABCD','DABC']}",
                "{'layers':['1234','4123']}");
    }

    private void assertEncode(GuiPlotColorDecoder decoder, String expected, String input) {
        assertEquals(fix(JsonUtils.toStringSorted(expected)),
                JsonUtils.toStringSorted(decoder.encodeForBrowser(new JSONObject(input)).toString()));
    }

    @Test
    public void shouldEncodeJsonWithoutLayers() {
        // given
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Elements.values());

        // when then
        assertEncode(decoder, "{'key1':'value1','key2':'value2','key3':['1234','4321','4312','4123']}",
                "{'key1':'value1','key3':['1234','4321','4312','4123'],'key2':'value2'}");

        assertEncode(decoder, "{'key1':'value1','key2':'value2','key3':[]}",
                "{'key1':'value1','key3':[],'key2':'value2'}");

        assertEncode(decoder, "{'key3':[]}",
                "{'key3':[]}");

        assertEncode(decoder, "{'key3':['1234','4123']}",
                "{'key3':['1234','4123']}");
    }

    @Test
    public void shouldEncodeUnexpectedObject() {
        // given
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Elements.values());

        try {
            // when
            decoder.encodeForBrowser(Boolean.TRUE);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            // then
            Assert.assertEquals("You can use only String or JSONObject as board", e.getMessage());
        }
    }

    @Test
    public void shouldNotEnumSymbol() {
        // given
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Elements.values());

        try {
            // when
            decoder.encodeForBrowser("12345");
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            // then
            Assert.assertEquals("Not enum symbol '5'", e.getMessage());
        }
    }

    @Test
    public void shouldRemoveNSymbolsFromLayers() {
        // given
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Elements.values());

        // when then
        assertEquals(fix("ABCD"),
                decoder.encodeForBrowser("12\n34").toString());

        assertEncode(decoder, "{'layers':['ABCD','DABC']}",
                "{'layers':['1234'\n,'4123']}");
    }

    private String fix(String json) {
        return json.replace("'","\"");
    }

    @Test
    public void shouldEncodeForClient_removeN() {
        // given
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Elements.values());

        // when then
        assertEquals("1234", decoder.encodeForClient("12\n34"));
        assertEquals("4321", decoder.encodeForClient("43\n21"));

        assertEquals(fix("{'layers':['1234','4123']}"),
                decoder.encodeForClient(new JSONObject("{'layers':['1234'\n,'4123']}")));
    }
}
