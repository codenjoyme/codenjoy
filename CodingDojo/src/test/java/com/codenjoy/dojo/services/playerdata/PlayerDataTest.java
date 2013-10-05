package com.codenjoy.dojo.services.playerdata;

import org.junit.Test;

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
        PlayerData data = new PlayerData(13, "board", 55, 78, 99, 3, "+100", "chat");

        assertSame("board", data.getBoard());
        assertEquals(55, data.getScore());
        assertEquals(78, data.getMaxLength());
        assertEquals(3, data.getLevel());
        assertEquals(13, data.getBoardSize());
        assertEquals(99, data.getLength());
        assertEquals("+100", data.getInfo());
    }

    @Test
    public void shouldCollectData() {
        PlayerData data = new PlayerData(15, "board", 10, 5, 7, 1, "info", "chat");

        assertEquals("PlayerData[" +
                "BoardSize:15, " +
                "Board:'board', " +
                "Score:10, " +
                "MaxLength:5, " +
                "Length:7, " +
                "CurrentLevel:1, " +
                "Info:'info', " +
                "ChatLog:'chat']", data.toString());
    }

    @Test
    public void shouldEmptyInfoIfNull(){
        PlayerData data = new PlayerData(15, "board", 10, 9, 8, 1, null, "chat");

        assertEquals("", data.getInfo());
        assertTrue(data.toString(), data.toString().contains("Info:''"));
    }
}
