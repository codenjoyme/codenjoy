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


import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.hero.HeroDataImpl;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.*;

public class PlayerDataTest {

    @Test
    public void shouldSavePlayerData(){
        PlayerData data = new PlayerData(13, "board", "game", 55, "+100",
                new LinkedHashMap<String, Object>(){{ put("user@mail.com", 12); }},
                new LinkedHashMap<String, HeroData>(){{ put("user@mail.com", new HeroDataImpl(1, pt(10, 5), true)); }},
                new LinkedHashMap<String, String>(){{ put("user@mail.com", "User Muser"); }},
                new LinkedList<>(){{ addAll(Arrays.asList("player1", "player2")); }},
                1);

        assertSame("board", data.getBoard());
        assertEquals(55, data.getScore());
        assertEquals(13, data.getBoardSize());
        assertEquals("+100", data.getInfo());
        assertEquals("{user@mail.com=12}", data.getScores().toString());
        assertEquals("[player1, player2]", data.getGroup().toString());
        assertEquals("{user@mail.com=User Muser}", data.getReadableNames().toString());
        assertEquals("{user@mail.com=HeroDataImpl(level=1, coordinate=[10,5], isMultiplayer=true, additionalData=null)}", data.getCoordinates().toString());
        assertEquals("game", data.getGame());
        assertEquals(Integer.valueOf(1), data.getLastChatMessage());
    }

    @Test
    public void shouldSavePlayerData_nullChatId(){
        PlayerData data = new PlayerData(13, "board", "game", 55, "+100",
                new LinkedHashMap<String, Object>(){{ put("user@mail.com", 12); }},
                new LinkedHashMap<String, HeroData>(){{ put("user@mail.com", new HeroDataImpl(1, pt(10, 5), true)); }},
                new LinkedHashMap<String, String>(){{ put("user@mail.com", "User Muser"); }},
                new LinkedList<>(){{ addAll(Arrays.asList("player1", "player2")); }},
                null);

        assertEquals(null, data.getLastChatMessage());
    }

    @Test
    public void shouldCollectData() {
        PlayerData data = new PlayerData(15, "board", "game", 10, "info",
                new LinkedHashMap<String, Object>(){{ put("user@mail.com", 12); }},
                new LinkedHashMap<String, HeroData>(){{ put("user@mail.com", new HeroDataImpl(1, pt(10, 5), true)); }},
                new LinkedHashMap<String, String>(){{ put("user@mail.com", "User Muser"); }},
                new LinkedList<>(){{ addAll(Arrays.asList("player1", "player2")); }},
                2);

        assertEquals("PlayerData[BoardSize:15, " +
                "Board:'board', " +
                "Game:'game', " +
                "Score:10, " +
                "Info:'info', " +
                "Scores:'{user@mail.com=12}', " +
                "Coordinates:'{user@mail.com=HeroDataImpl(level=1, coordinate=[10,5], isMultiplayer=true, additionalData=null)}', " +
                "ReadableNames:'{user@mail.com=User Muser}', " +
                "Group:[player1, player2], " +
                "LastChatMessage:2]",
                data.toString());
    }

    @Test
    public void shouldEmptyInfoIfNull(){
        PlayerData data = new PlayerData(15, "board", "game", 10, null,
                new LinkedHashMap<String, Object>(){{ put("user@mail.com", 12); }},
                new LinkedHashMap<String, HeroData>(){{ put("user@mail.com", new HeroDataImpl(1, pt(10, 5), true)); }},
                new LinkedHashMap<String, String>(){{ put("user@mail.com", "User Muser"); }},
                new LinkedList<>(){{ addAll(Arrays.asList("player1", "player2")); }},
                1);

        assertEquals("", data.getInfo());
        assertTrue(data.toString(), data.toString().contains("Info:''"));
    }
}
