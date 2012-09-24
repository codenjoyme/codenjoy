package net.tetris.services;

import org.junit.Test;

import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class PlayerDataTest {

    @Test
    public void shouldSavePlayerData(){
        LinkedList<Plot> plots = new LinkedList<Plot>();
        PlayerData data = new PlayerData(plots, 1, 2, "qwe", 3);

        assertSame(plots, data.getPlots());
        assertEquals(1, data.getScore());
        assertEquals(2, data.getLinesRemoved());
        assertEquals("qwe", data.getNextLevelIngoingCriteria());
        assertEquals(3, data.getLevel());
    }



}
