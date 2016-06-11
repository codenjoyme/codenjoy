package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.settings.Settings;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * User: oleksandr.baglai
 * Date: 3/23/13
 * Time: 3:53 PM
 */
public class GuiPlotColorDecoderTest {

    enum Elements {
        ONE('1'), TWO('2'), THREE('3'), FOUR('4');
        private char c;

        Elements(char c) {
            this.c = c;
        }

        @Override
        public String toString() {
            return String.valueOf(c);
        }
    }

    @Test
    public void shouldEncode() {
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Elements.values());

        assertEquals("ABCD", decoder.encode("1234"));
        assertEquals("DCBA", decoder.encode("4321"));
    }

    @Test
    public void shouldWorkWithAllSymbols() {
        assertEncode(new com.codenjoy.dojo.battlecity.services.GameRunner(), "ABCDEFGHIJKLMNOPQRATUVWXYZ012345");
        assertEncode(new com.codenjoy.dojo.bomberman.services.GameRunner(), "ABCDEFGHIJKLMNOPQR");
        assertEncode(new com.codenjoy.dojo.minesweeper.services.GameRunner(), "ABCDEFGHIJKLMNOP");
        assertEncode(new com.codenjoy.dojo.snake.services.GameRunner(), "ABCDEFGHIJKLMNOPQR");
        assertEncode(new com.codenjoy.dojo.loderunner.services.GameRunner(), "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456");
        // TODO сделать тут автоматическое добавление всех раннеров
    }

    private void assertEncode(GameType game, String expected) {
        Object[] plots = game.getPlots();
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(plots);

        String plotsString = "";
        for (Object o : plots) {
            plotsString += o;
        }

        assertEquals(expected, decoder.encode(plotsString));
    }

    @Test
    public void shouldEncodeJson() {
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Elements.values());

        assertEquals(fix("{'key2':'value2','layers':['ABCD','DCBA','DCAB','DABC'],'key1':'value1'}"),
                decoder.encode(fix("{'key1':'value1','layers':['1234','4321','4312','4123'],'key2':'value2'}")));

        assertEquals(fix("{'key2':'value2','layers':[],'key1':'value1'}"),
                decoder.encode(fix("{'key1':'value1','layers':[],'key2':'value2'}")));

        assertEquals(fix("{'layers':[]}"),
                decoder.encode(fix("{'layers':[]}")));

        assertEquals(fix("{'layers':['ABCD','DABC']}"),
                decoder.encode(fix("{'layers':['1234','4123']}")));
    }

    private String fix(String json) {
        return json.replace("'","\"");
    }

}
