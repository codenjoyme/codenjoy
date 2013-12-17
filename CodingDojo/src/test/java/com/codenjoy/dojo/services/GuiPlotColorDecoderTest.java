package com.codenjoy.dojo.services;

import com.codenjoy.dojo.battlecity.services.BattlecityGame;
import com.codenjoy.dojo.bomberman.services.BombermanGame;
import com.codenjoy.dojo.loderunner.services.LoderunnerGame;
import com.codenjoy.dojo.minesweeper.services.MinesweeperGame;
import com.codenjoy.dojo.snake.services.SnakeGame;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

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
        assertEncode(new BattlecityGame(), "ABCDEFGHIJKLMNOPQRATUVWXYZ012345");
        assertEncode(new BombermanGame(), "ABCDEFGHIJKLMNOPQR");
        assertEncode(new MinesweeperGame(), "ABCDEFGHIJKLMNOP");
        assertEncode(new SnakeGame(), "ABCDEFGHIJKLMNOPQR");
        assertEncode(new LoderunnerGame(), "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
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

}
