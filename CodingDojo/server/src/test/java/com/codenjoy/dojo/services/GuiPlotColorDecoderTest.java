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
    public void shouldEncodeJsonWithoutLayersAndBoard() {
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
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Elements3.values());

        // when then
        assertEquals("1234", decoder.encodeForClient("12\n34"));
        assertEquals("4321", decoder.encodeForClient("43\n21"));

        assertEquals(fix("{'layers':['1234','4123']}"),
                decoder.encodeForClient(new JSONObject("{'layers':['1234'\n,'4123']}")));
    }

    public enum Elements3 implements CharElements {

        NONE(' '),

        BRICK('#'),

        PIT_FILL_1('1'),
        PIT_FILL_2('2'),
        PIT_FILL_3('3'),
        PIT_FILL_4('4'),

        UNDESTROYABLE_WALL('☼'),

        DRILL_PIT('*'),

        YELLOW_GOLD('$'),
        GREEN_GOLD('&'),
        RED_GOLD('@'),

        HERO_DIE('Ѡ'),
        HERO_DRILL_LEFT('Я'),
        HERO_DRILL_RIGHT('R'),
        HERO_LADDER('Y'),
        HERO_LEFT('◄'),
        HERO_RIGHT('►'),
        HERO_FALL_LEFT(']'),
        HERO_FALL_RIGHT('['),
        HERO_PIPE_LEFT('{'),
        HERO_PIPE_RIGHT('}'),

        HERO_SHADOW_DIE('x'),
        HERO_SHADOW_DRILL_LEFT('⊰'),
        HERO_SHADOW_DRILL_RIGHT('⊱'),
        HERO_SHADOW_LADDER('⍬'),
        HERO_SHADOW_LEFT('⊲'),
        HERO_SHADOW_RIGHT('⊳'),
        HERO_SHADOW_FALL_LEFT('⊅'),
        HERO_SHADOW_FALL_RIGHT('⊄'),
        HERO_SHADOW_PIPE_LEFT('⋜'),
        HERO_SHADOW_PIPE_RIGHT('⋝'),

        OTHER_HERO_DIE('Z'),
        OTHER_HERO_DRILL_LEFT('⌋'),
        OTHER_HERO_DRILL_RIGHT('⌊'),
        OTHER_HERO_LADDER('U'),
        OTHER_HERO_LEFT(')'),
        OTHER_HERO_RIGHT('('),
        OTHER_HERO_FALL_LEFT('⊐'),
        OTHER_HERO_FALL_RIGHT('⊏'),
        OTHER_HERO_PIPE_LEFT('Э'),
        OTHER_HERO_PIPE_RIGHT('Є'),

        OTHER_HERO_SHADOW_DIE('⋈'),
        OTHER_HERO_SHADOW_DRILL_LEFT('⋰'),
        OTHER_HERO_SHADOW_DRILL_RIGHT('⋱'),
        OTHER_HERO_SHADOW_LEFT('⋊'),
        OTHER_HERO_SHADOW_RIGHT('⋉'),
        OTHER_HERO_SHADOW_LADDER('⋕'),
        OTHER_HERO_SHADOW_FALL_LEFT('⋣'),
        OTHER_HERO_SHADOW_FALL_RIGHT('⋢'),
        OTHER_HERO_SHADOW_PIPE_LEFT('⊣'),
        OTHER_HERO_SHADOW_PIPE_RIGHT('⊢'),

        ENEMY_LADDER('Q'),
        ENEMY_LEFT('«'),
        ENEMY_RIGHT('»'),
        ENEMY_PIPE_LEFT('<'),
        ENEMY_PIPE_RIGHT('>'),
        ENEMY_PIT('X'),

        LADDER('H'),
        PIPE('~'),

        PORTAL('⊛'),

        SHADOW_PILL('S');

        final char ch;

        @Override
        public char ch() {
            return ch;
        }

        public char getChar() {
            return ch;
        }

        Elements3(char ch) {
            this.ch = ch;
        }

        @Override
        public String toString() {
            return String.valueOf(ch);
        }

    }

    @Test
    public void performanceTest() {
        // given
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Elements3.values());
        String map =
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼                             ~~~~~~~~~           ~~~~~~~☼" +
                "☼##H########################H#H       H##########H       ☼" +
                "☼  H                        H######H  H          H#☼☼☼☼☼#☼" +
                "☼H☼☼#☼☼H    H#########H     H#     H#####H#####H##  ~~~~~☼" +
                "☼H     H    H         H#####H#     H ~   H     H  ~~     ☼" +
                "☼H#☼#☼#H    H         H  ~~~ #####H#     H     H    ~~   ☼" +
                "☼H  ~  H~~~~H~~~~~~   H           H   H######H##      ~~ ☼" +
                "☼H     H    H     H###☼☼☼☼☼☼H☼    H~~~H      H          #☼" +
                "☼H     H    H#####H         H     H      H#########H     ☼" +
                "☼☼###☼##☼##☼H         H###H##    H##     H#       ##     ☼" +
                "☼☼###☼~~~~  H         H   H######H######### H###H #####H#☼" +
                "☼☼   ☼      H   ~~~~~~H   H      H          H# #H      H ☼" +
                "☼########H###☼☼☼☼     H  ############   ###### ##########☼" +
                "☼        H            H                                  ☼" +
                "☼H##########################H########~~~####H############☼" +
                "☼H                 ~~~      H               H            ☼" +
                "☼#######H#######            H###~~~~      ############H  ☼" +
                "☼       H~~~~~~~~~~         H                         H  ☼" +
                "☼       H    ##H   #######H##########~~~~~~~H######## H  ☼" +
                "☼       H    ##H          H                 H         H  ☼" +
                "☼##H#####    ########H#######~~~~  ~~~#########~~~~~  H  ☼" +
                "☼  H                 H                            ~~~~H  ☼" +
                "☼#########H##########H        #☼☼☼☼☼☼#   ☼☼☼☼☼☼☼      H  ☼" +
                "☼         H          H        ~      ~                H  ☼" +
                "☼☼☼       H~~~~~~~~~~H         ######   ###########   H  ☼" +
                "☼    H######         #######H           ~~~~~~~~~~~~~~H  ☼" +
                "☼H☼  H  ◄                   H  H####H                 H  ☼" +
                "☼H☼☼#☼☼☼☼☼☼☼☼☼☼☼☼###☼☼☼☼☼☼☼☼H☼☼☼☼☼☼☼☼#☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼#☼" +
                "☼H            ~~H~~~~☼☼☼☼☼☼☼H☼☼☼☼☼☼☼       H   ~~~~~~~~~H☼" +
                "☼H~~~~  ######  H         H☼H☼H        ####H  ☼         H☼" +
                "☼H              ##H#######H☼H☼H######H     ###☼☼☼☼☼☼☼☼ ~H☼" +
                "☼H#########       H    ~~~H☼H☼H~~~   H~~~~~ ##        ~ H☼" +
                "☼H        ###H####H##H     ☼H☼       H     ###☼☼☼☼☼☼ ~  H☼" +
                "☼H           H      #######☼H☼#####  H#####   ~~~~~~~ ~ H☼" +
                "☼~~~~~~~~~~~~H       H~~~~~☼H☼~~~~~  H             ~ ~  H☼" +
                "☼     H              H     ☼H☼     ##########H          H☼" +
                "☼ ### #############H H#####☼H☼               H ######## H☼" +
                "☼H                 H       ☼H☼#######        H          H☼" +
                "☼H#####         H##H####                ###H#########   H☼" +
                "☼H      H######### H   ############        H            H☼" +
                "☼H##    H          H~~~~~~                 H #######H## H☼" +
                "☼~~~~#####H#   ~~~~H         ########H     H        H   H☼" +
                "☼         H        H      ~~~~~~~~   H     H        H   H☼" +
                "☼   ########H    ######H##        ##############    H   H☼" +
                "☼           H          H        ~~~~~           ##H#####H☼" +
                "☼H    ###########H     H#####H         H##H       H     H☼" +
                "☼H###            H     H     ###########  ##H###  H     H☼" +
                "☼H  ######  ##H######  H                    H   ##H###  H☼" +
                "☼H            H ~~~~~##H###H     #########H##           H☼" +
                "☼    H########H#       H   ######         H             H☼" +
                "☼ ###H        H         ~~~~~H      ##H###H####H###     H☼" +
                "☼    H########H#########     H        H        H        H☼" +
                "☼H   H                       H        H        H        H☼" +
                "☼H  ####H######         #####H########H##      H#####   H☼" +
                "☼H      H      H#######H                       H        H☼" +
                "☼##############H       H#################################☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼";

        String expected =
                "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG" +
                "GAAAAAAAAAAAAAAAAAAAAAAAAAAAAA666666666AAAAAAAAAAA6666666G" +
                "GBB5BBBBBBBBBBBBBBBBBBBBBBBB5B5AAAAAAA5BBBBBBBBBB5AAAAAAAG" +
                "GAA5AAAAAAAAAAAAAAAAAAAAAAAA5BBBBBB5AA5AAAAAAAAAA5BGGGGGBG" +
                "G5GGBGG5AAAA5BBBBBBBBB5AAAAA5BAAAAA5BBBBB5BBBBB5BBAA66666G" +
                "G5AAAAA5AAAA5AAAAAAAAA5BBBBB5BAAAAA5A6AAA5AAAAA5AA66AAAAAG" +
                "G5BGBGB5AAAA5AAAAAAAAA5AA666ABBBBB5BAAAAA5AAAAA5AAAA66AAAG" +
                "G5AA6AA566665666666AAA5AAAAAAAAAAA5AAA5BBBBBB5BBAAAAAA66AG" +
                "G5AAAAA5AAAA5AAAAA5BBBGGGGGG5GAAAA56665AAAAAA5AAAAAAAAAABG" +
                "G5AAAAA5AAAA5BBBBB5AAAAAAAAA5AAAAA5AAAAAA5BBBBBBBBB5AAAAAG" +
                "GGBBBGBBGBBG5AAAAAAAAA5BBB5BBAAAA5BBAAAAA5BAAAAAAABBAAAAAG" +
                "GGBBBG6666AA5AAAAAAAAA5AAA5BBBBBB5BBBBBBBBBA5BBB5ABBBBB5BG" +
                "GGAAAGAAAAAA5AAA6666665AAA5AAAAAA5AAAAAAAAAA5BAB5AAAAAA5AG" +
                "GBBBBBBBB5BBBGGGGAAAAA5AABBBBBBBBBBBBAAABBBBBBABBBBBBBBBBG" +
                "GAAAAAAAA5AAAAAAAAAAAA5AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAG" +
                "G5BBBBBBBBBBBBBBBBBBBBBBBBBB5BBBBBBBB666BBBB5BBBBBBBBBBBBG" +
                "G5AAAAAAAAAAAAAAAAA666AAAAAA5AAAAAAAAAAAAAAA5AAAAAAAAAAAAG" +
                "GBBBBBBB5BBBBBBBAAAAAAAAAAAA5BBB6666AAAAAABBBBBBBBBBBB5AAG" +
                "GAAAAAAA56666666666AAAAAAAAA5AAAAAAAAAAAAAAAAAAAAAAAAA5AAG" +
                "GAAAAAAA5AAAABB5AAABBBBBBB5BBBBBBBBBB66666665BBBBBBBBA5AAG" +
                "GAAAAAAA5AAAABB5AAAAAAAAAA5AAAAAAAAAAAAAAAAA5AAAAAAAAA5AAG" +
                "GBB5BBBBBAAAABBBBBBBB5BBBBBBB6666AA666BBBBBBBBB66666AA5AAG" +
                "GAA5AAAAAAAAAAAAAAAAA5AAAAAAAAAAAAAAAAAAAAAAAAAAAA66665AAG" +
                "GBBBBBBBBB5BBBBBBBBBB5AAAAAAAABGGGGGGBAAAGGGGGGGAAAAAA5AAG" +
                "GAAAAAAAAA5AAAAAAAAAA5AAAAAAAA6AAAAAA6AAAAAAAAAAAAAAAA5AAG" +
                "GGGAAAAAAA566666666665AAAAAAAAABBBBBBAAABBBBBBBBBBBAAA5AAG" +
                "GAAAA5BBBBBBAAAAAAAAABBBBBBB5AAAAAAAAAAA666666666666665AAG" +
                "G5GAA5AAPAAAAAAAAAAAAAAAAAAA5AA5BBBB5AAAAAAAAAAAAAAAAA5AAG" +
                "G5GGBGGGGGGGGGGGGBBBGGGGGGGG5GGGGGGGGBGGGGGGGGGGGGGGGGGGBG" +
                "G5AAAAAAAAAAAA6656666GGGGGGG5GGGGGGGAAAAAAA5AAA6666666665G" +
                "G56666AABBBBBBAA5AAAAAAAAA5G5G5AAAAAAAABBBB5AAGAAAAAAAAA5G" +
                "G5AAAAAAAAAAAAAABB5BBBBBBB5G5G5BBBBBB5AAAAABBBGGGGGGGGA65G" +
                "G5BBBBBBBBBAAAAAAA5AAAA6665G5G5666AAA566666ABBAAAAAAAA6A5G" +
                "G5AAAAAAAABBB5BBBB5BB5AAAAAG5GAAAAAAA5AAAAABBBGGGGGGA6AA5G" +
                "G5AAAAAAAAAAA5AAAAAABBBBBBBG5GBBBBBAA5BBBBBAAA6666666A6A5G" +
                "G6666666666665AAAAAAA566666G5G66666AA5AAAAAAAAAAAAA6A6AA5G" +
                "GAAAAA5AAAAAAAAAAAAAA5AAAAAG5GAAAAABBBBBBBBBB5AAAAAAAAAA5G" +
                "GABBBABBBBBBBBBBBBB5A5BBBBBG5GAAAAAAAAAAAAAAA5ABBBBBBBBA5G" +
                "G5AAAAAAAAAAAAAAAAA5AAAAAAAG5GBBBBBBBAAAAAAAA5AAAAAAAAAA5G" +
                "G5BBBBBAAAAAAAAA5BB5BBBBAAAAAAAAAAAAAAAABBB5BBBBBBBBBAAA5G" +
                "G5AAAAAA5BBBBBBBBBA5AAABBBBBBBBBBBBAAAAAAAA5AAAAAAAAAAAA5G" +
                "G5BBAAAA5AAAAAAAAAA5666666AAAAAAAAAAAAAAAAA5ABBBBBBB5BBA5G" +
                "G6666BBBBB5BAAA66665AAAAAAAAABBBBBBBB5AAAAA5AAAAAAAA5AAA5G" +
                "GAAAAAAAAA5AAAAAAAA5AAAAAA66666666AAA5AAAAA5AAAAAAAA5AAA5G" +
                "GAAABBBBBBBB5AAAABBBBBB5BBAAAAAAAABBBBBBBBBBBBBBAAAA5AAA5G" +
                "GAAAAAAAAAAA5AAAAAAAAAA5AAAAAAAA66666AAAAAAAAAAABB5BBBBB5G" +
                "G5AAAABBBBBBBBBBB5AAAAA5BBBBB5AAAAAAAAA5BB5AAAAAAA5AAAAA5G" +
                "G5BBBAAAAAAAAAAAA5AAAAA5AAAAABBBBBBBBBBBAABB5BBBAA5AAAAA5G" +
                "G5AABBBBBBAABB5BBBBBBAA5AAAAAAAAAAAAAAAAAAAA5AAABB5BBBAA5G" +
                "G5AAAAAAAAAAAA5A66666BB5BBB5AAAAABBBBBBBBB5BBAAAAAAAAAAA5G" +
                "GAAAA5BBBBBBBB5BAAAAAAA5AAABBBBBBAAAAAAAAA5AAAAAAAAAAAAA5G" +
                "GABBB5AAAAAAAA5AAAAAAAAA666665AAAAAABB5BBB5BBBB5BBBAAAAA5G" +
                "GAAAA5BBBBBBBB5BBBBBBBBBAAAAA5AAAAAAAA5AAAAAAAA5AAAAAAAA5G" +
                "G5AAA5AAAAAAAAAAAAAAAAAAAAAAA5AAAAAAAA5AAAAAAAA5AAAAAAAA5G" +
                "G5AABBBB5BBBBBBAAAAAAAAABBBBB5BBBBBBBB5BBAAAAAA5BBBBBAAA5G" +
                "G5AAAAAA5AAAAAA5BBBBBBB5AAAAAAAAAAAAAAAAAAAAAAA5AAAAAAAA5G" +
                "GBBBBBBBBBBBBBB5AAAAAAA5BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBG" +
                "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG";

        // when then
        int times = 100000;
        for (int i = 0; i < times; i++) {
            assertEquals(expected, decoder.encodeForBrowser(map));
        }

    }
}
