package net.tetris.services;

import com.codenjoy.dojo.tetris.model.Plot;
import org.junit.Test;

import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PlayerDataTest {

    @Test
    public void shouldSavePlayerData(){
        LinkedList<Plot> plots = new LinkedList<Plot>();
        PlayerData data = new PlayerData(plots, 1, 2, "qwe", 3, "+100");

        assertSame(plots, data.getPlots());
        assertEquals(1, data.getScore());
        assertEquals(2, data.getLinesRemoved());
        assertEquals("qwe", data.getNextLevelIngoingCriteria());
        assertEquals(3, data.getLevel());
        assertEquals("+100", data.getInfo());
    }

    @Test
    public void shouldPrintablePlayerData(){
        LinkedList<Plot> plots = new LinkedList<Plot>();
        PlayerData data = new PlayerData(plots, 1, 2, "qwe", 3, "+100");

        assertEquals("PlayerData[Plots:[], " +
                "Score:1, " +
                "LinesRemoved:2, " +
                "NextLevelIngoingCriteria:'qwe', " +
                "CurrentLevel:3, " +
                "Info:'+100']",
                data.toString());
    }

    @Test
    public void shouldEmptyInfoIfNull(){
        LinkedList<Plot> plots = new LinkedList<Plot>();
        PlayerData data = new PlayerData(plots, 1, 2, "qwe", 3, null);

        assertEquals("", data.getInfo());
        assertTrue(data.toString(), data.toString().contains("Info:''"));
    }
}
