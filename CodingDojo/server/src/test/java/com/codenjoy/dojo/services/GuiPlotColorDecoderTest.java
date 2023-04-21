package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.client.Utils;
import com.codenjoy.dojo.services.printer.CharElement;
import com.codenjoy.dojo.utils.JsonUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GuiPlotColorDecoderTest {

    enum Element implements CharElement {
        ONE('1'),
        TWO('2'),
        THREE('3'),
        FOUR('4');

        private char ch;

        Element(char ch) {
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
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Element.values());

        // when then
        assertEquals("ABCD", decoder.encodeForBrowser("1234"));
        assertEquals("DCBA", decoder.encodeForBrowser("4321"));
    }

    @Test
    public void shouldToString() {
        // given when then
        assertEquals("[1234 -> ABCD]",
                new GuiPlotColorDecoder(Element.values()).toString());

        assertEquals("[SHG -> ABC]",
                new GuiPlotColorDecoder(Element1.values()).toString());

        assertEquals("[SH -> AB]",
                new GuiPlotColorDecoder(Element2.values()).toString());
    }

    public enum Element1 implements CharElement {
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

    public enum Element2 implements CharElement {
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
        when(game1.getPlots()).thenReturn(Element1.values());

        GameType game2 = mock(GameType.class);
        when(game2.getPlots()).thenReturn(Element2.values());

        // when then
        assertEncode(game1, "ABC");
        assertEncode(game2, "AB");
    }

    private void assertEncode(GameType game, String expected) {
        CharElement[] plots = game.getPlots();
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
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Element.values());

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
                JsonUtils.toStringSorted(decoder.encodeForBrowser(new JSONObject(input))));
    }

    @Test
    public void shouldEncodeJsonWithoutLayersAndBoard() {
        // given
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Element.values());

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
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Element.values());

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
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Element.values());

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
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Element.values());

        // when then
        assertEquals(fix("ABCD"),
                decoder.encodeForBrowser("12\n34"));

        assertEncode(decoder, "{'layers':['ABCD','DABC']}",
                "{'layers':['1234'\n,'4123']}");
    }

    private String fix(String json) {
        return json.replace("'","\"");
    }

    @Test
    public void shouldEncodeForClient_removeN() {
        // given
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Element3.values());

        // when then
        assertEquals("1234", decoder.encodeForClient("12\n34"));
        assertEquals("4321", decoder.encodeForClient("43\n21"));

        assertEquals(fix("{'layers':['1234','4123']}"),
                decoder.encodeForClient(new JSONObject("{'layers':['1234'\n,'4123']}")));
    }

    public enum Element3 implements CharElement {

        /// void

        NONE(' '),

        /// bricks

        BRICK('#'),
        PIT_FILL_1('1'),
        PIT_FILL_2('2'),
        PIT_FILL_3('3'),
        PIT_FILL_4('4'),
        STONE('☼'),
        CRACK_PIT('*'),

        /// clues

        CLUE_KNIFE('$'),
        CLUE_GLOVE('&'),
        CLUE_RING('@'),

        /// your hero

        HERO_DIE('Ѡ'),
        HERO_LADDER('Y'),
        HERO_LEFT('◄'),
        HERO_RIGHT('►'),
        HERO_FALL(']'),
        HERO_PIPE('{'),
        HERO_PIT('⍃'),

        /// your hero in shadow mode

        HERO_MASK_DIE('x'),
        HERO_MASK_LADDER('⍬'),
        HERO_MASK_LEFT('⊲'),
        HERO_MASK_RIGHT('⊳'),
        HERO_MASK_FALL('⊅'),
        HERO_MASK_PIPE('⋜'),
        HERO_MASK_PIT('ᐊ'),

        /// other heroes

        OTHER_HERO_DIE('Z'),
        OTHER_HERO_LADDER('U'),
        OTHER_HERO_LEFT(')'),
        OTHER_HERO_RIGHT('('),
        OTHER_HERO_FALL('⊐'),
        OTHER_HERO_PIPE('Э'),
        OTHER_HERO_PIT('ᗉ'),

        /// other heroes in shadow mode

        OTHER_HERO_MASK_DIE('⋈'),
        OTHER_HERO_MASK_LEFT('⋊'),
        OTHER_HERO_MASK_RIGHT('⋉'),
        OTHER_HERO_MASK_LADDER('⋕'),
        OTHER_HERO_MASK_FALL('⋣'),
        OTHER_HERO_MASK_PIPE('⊣'),
        OTHER_HERO_MASK_PIT('ᗏ'),

        /// enemy heroes

        ENEMY_HERO_DIE('Ž'),
        ENEMY_HERO_LADDER('Ǔ'),
        ENEMY_HERO_LEFT('❫'),
        ENEMY_HERO_RIGHT('❪'),
        ENEMY_HERO_FALL('⋥'),
        ENEMY_HERO_PIPE('Ǯ'),
        ENEMY_HERO_PIT('⇇'),

        /// enemy heroes in shadow mode

        ENEMY_HERO_MASK_DIE('⧓'),
        ENEMY_HERO_MASK_LEFT('⧒'),
        ENEMY_HERO_MASK_RIGHT('⧑'),
        ENEMY_HERO_MASK_LADDER('≠'),
        ENEMY_HERO_MASK_FALL('⌫'),
        ENEMY_HERO_MASK_PIPE('❵'),
        ENEMY_HERO_MASK_PIT('⬱'),

        /// robbers (dummy AI-bots)

        ROBBER_LADDER('Q'),
        ROBBER_LEFT('«'),
        ROBBER_RIGHT('»'),
        ROBBER_FALL('‹'),
        ROBBER_PIPE('<'),
        ROBBER_PIT('⍇'),

        /// doors and keys

        OPENED_DOOR_GOLD('⍙'),
        OPENED_DOOR_SILVER('⍚'),
        OPENED_DOOR_BRONZE('⍜'),
        CLOSED_DOOR_GOLD('⍍'),
        CLOSED_DOOR_SILVER('⌺'),
        CLOSED_DOOR_BRONZE('⌼'),
        KEY_GOLD('✦'),
        KEY_SILVER('✼'),
        KEY_BRONZE('⍟'),

        /// other stuff

        BULLET('•'),
        LADDER('H'),
        PIPE('~'),
        BACKWAY('⊛'),
        MASK_POTION('S');

        final char ch;

        @Override
        public char ch() {
            return ch;
        }

        public char getChar() {
            return ch;
        }

        Element3(char ch) {
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
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Element3.values());
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
                "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG\n" +
                "GAAAAAAAAAAAAAAAAAAAAAAAAAAAAAÈÈÈÈÈÈÈÈÈAAAAAAAAAAAÈÈÈÈÈÈÈG\n" +
                "GBBÇBBBBBBBBBBBBBBBBBBBBBBBBÇBÇAAAAAAAÇBBBBBBBBBBÇAAAAAAAG\n" +
                "GAAÇAAAAAAAAAAAAAAAAAAAAAAAAÇBBBBBBÇAAÇAAAAAAAAAAÇBGGGGGBG\n" +
                "GÇGGBGGÇAAAAÇBBBBBBBBBÇAAAAAÇBAAAAAÇBBBBBÇBBBBBÇBBAAÈÈÈÈÈG\n" +
                "GÇAAAAAÇAAAAÇAAAAAAAAAÇBBBBBÇBAAAAAÇAÈAAAÇAAAAAÇAAÈÈAAAAAG\n" +
                "GÇBGBGBÇAAAAÇAAAAAAAAAÇAAÈÈÈABBBBBÇBAAAAAÇAAAAAÇAAAAÈÈAAAG\n" +
                "GÇAAÈAAÇÈÈÈÈÇÈÈÈÈÈÈAAAÇAAAAAAAAAAAÇAAAÇBBBBBBÇBBAAAAAAÈÈAG\n" +
                "GÇAAAAAÇAAAAÇAAAAAÇBBBGGGGGGÇGAAAAÇÈÈÈÇAAAAAAÇAAAAAAAAAABG\n" +
                "GÇAAAAAÇAAAAÇBBBBBÇAAAAAAAAAÇAAAAAÇAAAAAAÇBBBBBBBBBÇAAAAAG\n" +
                "GGBBBGBBGBBGÇAAAAAAAAAÇBBBÇBBAAAAÇBBAAAAAÇBAAAAAAABBAAAAAG\n" +
                "GGBBBGÈÈÈÈAAÇAAAAAAAAAÇAAAÇBBBBBBÇBBBBBBBBBAÇBBBÇABBBBBÇBG\n" +
                "GGAAAGAAAAAAÇAAAÈÈÈÈÈÈÇAAAÇAAAAAAÇAAAAAAAAAAÇBABÇAAAAAAÇAG\n" +
                "GBBBBBBBBÇBBBGGGGAAAAAÇAABBBBBBBBBBBBAAABBBBBBABBBBBBBBBBG\n" +
                "GAAAAAAAAÇAAAAAAAAAAAAÇAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAG\n" +
                "GÇBBBBBBBBBBBBBBBBBBBBBBBBBBÇBBBBBBBBÈÈÈBBBBÇBBBBBBBBBBBBG\n" +
                "GÇAAAAAAAAAAAAAAAAAÈÈÈAAAAAAÇAAAAAAAAAAAAAAAÇAAAAAAAAAAAAG\n" +
                "GBBBBBBBÇBBBBBBBAAAAAAAAAAAAÇBBBÈÈÈÈAAAAAABBBBBBBBBBBBÇAAG\n" +
                "GAAAAAAAÇÈÈÈÈÈÈÈÈÈÈAAAAAAAAAÇAAAAAAAAAAAAAAAAAAAAAAAAAÇAAG\n" +
                "GAAAAAAAÇAAAABBÇAAABBBBBBBÇBBBBBBBBBBÈÈÈÈÈÈÈÇBBBBBBBBAÇAAG\n" +
                "GAAAAAAAÇAAAABBÇAAAAAAAAAAÇAAAAAAAAAAAAAAAAAÇAAAAAAAAAÇAAG\n" +
                "GBBÇBBBBBAAAABBBBBBBBÇBBBBBBBÈÈÈÈAAÈÈÈBBBBBBBBBÈÈÈÈÈAAÇAAG\n" +
                "GAAÇAAAAAAAAAAAAAAAAAÇAAAAAAAAAAAAAAAAAAAAAAAAAAAAÈÈÈÈÇAAG\n" +
                "GBBBBBBBBBÇBBBBBBBBBBÇAAAAAAAABGGGGGGBAAAGGGGGGGAAAAAAÇAAG\n" +
                "GAAAAAAAAAÇAAAAAAAAAAÇAAAAAAAAÈAAAAAAÈAAAAAAAAAAAAAAAAÇAAG\n" +
                "GGGAAAAAAAÇÈÈÈÈÈÈÈÈÈÈÇAAAAAAAAABBBBBBAAABBBBBBBBBBBAAAÇAAG\n" +
                "GAAAAÇBBBBBBAAAAAAAAABBBBBBBÇAAAAAAAAAAAÈÈÈÈÈÈÈÈÈÈÈÈÈÈÇAAG\n" +
                "GÇGAAÇAANAAAAAAAAAAAAAAAAAAAÇAAÇBBBBÇAAAAAAAAAAAAAAAAAÇAAG\n" +
                "GÇGGBGGGGGGGGGGGGBBBGGGGGGGGÇGGGGGGGGBGGGGGGGGGGGGGGGGGGBG\n" +
                "GÇAAAAAAAAAAAAÈÈÇÈÈÈÈGGGGGGGÇGGGGGGGAAAAAAAÇAAAÈÈÈÈÈÈÈÈÈÇG\n" +
                "GÇÈÈÈÈAABBBBBBAAÇAAAAAAAAAÇGÇGÇAAAAAAAABBBBÇAAGAAAAAAAAAÇG\n" +
                "GÇAAAAAAAAAAAAAABBÇBBBBBBBÇGÇGÇBBBBBBÇAAAAABBBGGGGGGGGAÈÇG\n" +
                "GÇBBBBBBBBBAAAAAAAÇAAAAÈÈÈÇGÇGÇÈÈÈAAAÇÈÈÈÈÈABBAAAAAAAAÈAÇG\n" +
                "GÇAAAAAAAABBBÇBBBBÇBBÇAAAAAGÇGAAAAAAAÇAAAAABBBGGGGGGAÈAAÇG\n" +
                "GÇAAAAAAAAAAAÇAAAAAABBBBBBBGÇGBBBBBAAÇBBBBBAAAÈÈÈÈÈÈÈAÈAÇG\n" +
                "GÈÈÈÈÈÈÈÈÈÈÈÈÇAAAAAAAÇÈÈÈÈÈGÇGÈÈÈÈÈAAÇAAAAAAAAAAAAAÈAÈAAÇG\n" +
                "GAAAAAÇAAAAAAAAAAAAAAÇAAAAAGÇGAAAAABBBBBBBBBBÇAAAAAAAAAAÇG\n" +
                "GABBBABBBBBBBBBBBBBÇAÇBBBBBGÇGAAAAAAAAAAAAAAAÇABBBBBBBBAÇG\n" +
                "GÇAAAAAAAAAAAAAAAAAÇAAAAAAAGÇGBBBBBBBAAAAAAAAÇAAAAAAAAAAÇG\n" +
                "GÇBBBBBAAAAAAAAAÇBBÇBBBBAAAAAAAAAAAAAAAABBBÇBBBBBBBBBAAAÇG\n" +
                "GÇAAAAAAÇBBBBBBBBBAÇAAABBBBBBBBBBBBAAAAAAAAÇAAAAAAAAAAAAÇG\n" +
                "GÇBBAAAAÇAAAAAAAAAAÇÈÈÈÈÈÈAAAAAAAAAAAAAAAAAÇABBBBBBBÇBBAÇG\n" +
                "GÈÈÈÈBBBBBÇBAAAÈÈÈÈÇAAAAAAAAABBBBBBBBÇAAAAAÇAAAAAAAAÇAAAÇG\n" +
                "GAAAAAAAAAÇAAAAAAAAÇAAAAAAÈÈÈÈÈÈÈÈAAAÇAAAAAÇAAAAAAAAÇAAAÇG\n" +
                "GAAABBBBBBBBÇAAAABBBBBBÇBBAAAAAAAABBBBBBBBBBBBBBAAAAÇAAAÇG\n" +
                "GAAAAAAAAAAAÇAAAAAAAAAAÇAAAAAAAAÈÈÈÈÈAAAAAAAAAAABBÇBBBBBÇG\n" +
                "GÇAAAABBBBBBBBBBBÇAAAAAÇBBBBBÇAAAAAAAAAÇBBÇAAAAAAAÇAAAAAÇG\n" +
                "GÇBBBAAAAAAAAAAAAÇAAAAAÇAAAAABBBBBBBBBBBAABBÇBBBAAÇAAAAAÇG\n" +
                "GÇAABBBBBBAABBÇBBBBBBAAÇAAAAAAAAAAAAAAAAAAAAÇAAABBÇBBBAAÇG\n" +
                "GÇAAAAAAAAAAAAÇAÈÈÈÈÈBBÇBBBÇAAAAABBBBBBBBBÇBBAAAAAAAAAAAÇG\n" +
                "GAAAAÇBBBBBBBBÇBAAAAAAAÇAAABBBBBBAAAAAAAAAÇAAAAAAAAAAAAAÇG\n" +
                "GABBBÇAAAAAAAAÇAAAAAAAAAÈÈÈÈÈÇAAAAAABBÇBBBÇBBBBÇBBBAAAAAÇG\n" +
                "GAAAAÇBBBBBBBBÇBBBBBBBBBAAAAAÇAAAAAAAAÇAAAAAAAAÇAAAAAAAAÇG\n" +
                "GÇAAAÇAAAAAAAAAAAAAAAAAAAAAAAÇAAAAAAAAÇAAAAAAAAÇAAAAAAAAÇG\n" +
                "GÇAABBBBÇBBBBBBAAAAAAAAABBBBBÇBBBBBBBBÇBBAAAAAAÇBBBBBAAAÇG\n" +
                "GÇAAAAAAÇAAAAAAÇBBBBBBBÇAAAAAAAAAAAAAAAAAAAAAAAÇAAAAAAAAÇG\n" +
                "GBBBBBBBBBBBBBBÇAAAAAAAÇBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBG\n" +
                "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG\n";

        // when then
        int times = 100000;
        for (int i = 0; i < times; i++) {
            assertEquals(expected,
                    Utils.injectN(decoder.encodeForBrowser(map).toString()));
        }
    }

    @Test
    public void shouldBuildBoards_whenSameBoard() {
        // given
        Game game = mock(Game.class);
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Element.values());
        when(game.getBoardAsString(true)).thenReturn("1111222233334444");
        when(game.getBoardAsString(false)).thenReturn("1111222211112222");
        when(game.sameBoard()).thenReturn(true);

        // when
        ImmutablePair<Object, Object> boards = decoder.buildBoards(game, "player");

        // then
        assertEquals("(AAAABBBBCCCCDDDD,1111222233334444)", boards.toString());
    }

    @Test
    public void shouldBuildBoards_whenNotSameBoard() {
        // given
        Game game = mock(Game.class);
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(Element.values());
        when(game.getBoardAsString(true)).thenReturn("1111222233334444");
        when(game.getBoardAsString(false)).thenReturn("1111222211112222");
        when(game.sameBoard()).thenReturn(false);

        // when
        ImmutablePair<Object, Object> boards = decoder.buildBoards(game, "player");

        // then
        assertEquals("(AAAABBBBCCCCDDDD,1111222211112222)", boards.toString());
    }
}
