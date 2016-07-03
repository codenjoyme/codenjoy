package com.codenjoy.dojo.services.playerdata;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PlayerDataTest {

    @Test
    public void shouldSavePlayerData(){
        PlayerData data = new PlayerData(13, "board", "game", 55, 78, 99, 3, "+100", "scores", "coordinates");

        assertSame("board", data.getBoard());
        assertEquals(55, data.getScore());
        assertEquals(78, data.getMaxLength());
        assertEquals(3, data.getLevel());
        assertEquals(13, data.getBoardSize());
        assertEquals(99, data.getLength());
        assertEquals("+100", data.getInfo());
        assertEquals("scores", data.getScores());
        assertEquals("coordinates", data.getCoordinates());
        assertEquals("game", data.getGameName());
    }

    @Test
    public void shouldCollectData() {
        PlayerData data = new PlayerData(15, "board", "game", 10, 5, 7, 1, "info", "scores", "coordinates");

        assertEquals("PlayerData[" +
                "BoardSize:15, " +
                "Board:'board', " +
                "GameName:'game', " +
                "Score:10, " +
                "MaxLength:5, " +
                "Length:7, " +
                "CurrentLevel:1, " +
                "Info:'info', " +
                "Scores:'scores', " +
                "Coordinates:'coordinates']", data.toString());
    }

    @Test
    public void shouldEmptyInfoIfNull(){
        PlayerData data = new PlayerData(15, "board", "game", 10, 9, 8, 1, null, "scores", "coordinates");

        assertEquals("", data.getInfo());
        assertTrue(data.toString(), data.toString().contains("Info:''"));
    }
}
