package com.codenjoy.dojo.services.playerdata;

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


import com.codenjoy.dojo.utils.JsonUtils;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerDataTest {

    @Test
    public void shouldSavePlayerData(){
        PlayerData data = new PlayerData(13, "board", "game", 55, "+100",
                new JSONObject("{'user@mail.com':12}"),
                new JSONObject("{'user@gmail.com':{'y':10,'x':5}}"));

        assertSame("board", data.getBoard());
        assertEquals(55, data.getScore());
        assertEquals(13, data.getBoardSize());
        assertEquals("+100", data.getInfo());
        assertEquals("{\"user@mail.com\":12}", JsonUtils.toStringSorted(data.getScores().toString()).toString());
        assertEquals("{\"user@gmail.com\":{\"x\":5,\"y\":10}}", JsonUtils.toStringSorted(data.getHeroesData().toString()).toString());
        assertEquals("game", data.getGameName());
    }

    @Test
    public void shouldCollectData() {
        PlayerData data = new PlayerData(15, "board", "game", 10, "info",
                new JSONObject("{'user@mail.com':12}"),
                new JSONObject("{'user@gmail.com':{'y':10,'x':5}}"));

        assertEquals("PlayerData[" +
                "BoardSize:15, " +
                "Board:'board', " +
                "GameName:'game', " +
                "Score:10, " +
                "Info:'info', " +
                "Scores:'{\"user@mail.com\":12}', " +
                "HeroesData:'{\"user@gmail.com\":{\"x\":5,\"y\":10}}']", data.toString());
    }

    @Test
    public void shouldEmptyInfoIfNull(){
        PlayerData data = new PlayerData(15, "board", "game", 10, null,
                new JSONObject("{'user@mail.com':12}"),
                new JSONObject("{'user@gmail.com':{'y':10,'x':5}}"));

        assertEquals("", data.getInfo());
        assertTrue(data.toString(), data.toString().contains("Info:''"));
    }
}
