package com.codenjoy.dojo.services.playerdata;

import com.codenjoy.dojo.services.Plot;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 4:00 AM
 */
public class PlayerDataTest {

    @Test
    public void shouldSavePlayerData(){
        LinkedList<Plot> plots = new LinkedList<Plot>();
        PlayerData data = new PlayerData(13, plots, 55, 78, 99, 3, "+100");

        assertSame(plots, data.getPlots());
        assertEquals(55, data.getScore());
        assertEquals(78, data.getMaxLength());
        assertEquals(3, data.getLevel());
        assertEquals(13, data.getBoardSize());
        assertEquals(99, data.getLength());
        assertEquals("+100", data.getInfo());
    }

    @Test
    public void shouldCollectData() {
        List<Plot> plots = new LinkedList<Plot>();
        plots.add(new Plot(0, 0, "APPLE"));
        plots.add(new Plot(1, 1, "BODY"));
        plots.add(new Plot(2, 2, "EMPTY"));
        plots.add(new Plot(3, 3, "HEAD"));
        plots.add(new Plot(4, 4, "STONE"));
        plots.add(new Plot(5, 5, "TAIL"));
        plots.add(new Plot(6, 6, "WALL"));

        PlayerData data = new PlayerData(15, plots, 10, 5, 7, 1, "info");

        assertEquals("PlayerData[" +
                "BoardSize:15, " +
                "Plots:[" +
                    "Plot{x=0, y=0, color=APPLE}, " +
                    "Plot{x=1, y=1, color=BODY}, " +
                    "Plot{x=2, y=2, color=EMPTY}, " +
                    "Plot{x=3, y=3, color=HEAD}, " +
                    "Plot{x=4, y=4, color=STONE}, " +
                    "Plot{x=5, y=5, color=TAIL}, " +
                    "Plot{x=6, y=6, color=WALL}], " +
                "Score:10, " +
                "MaxLength:5, " +
                "Length:7, " +
                "CurrentLevel:1, " +
                "Info:'info']", data.toString());
    }

    @Test
    public void shouldEmptyInfoIfNull(){
        LinkedList<Plot> plots = new LinkedList<Plot>();
        PlayerData data = new PlayerData(15, plots, 10, 9, 8, 1, null);

        assertEquals("", data.getInfo());
        assertTrue(data.toString(), data.toString().contains("Info:''"));
    }
}
