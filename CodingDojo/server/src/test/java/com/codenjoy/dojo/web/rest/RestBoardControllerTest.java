package com.codenjoy.dojo.web.rest;

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

import com.codenjoy.dojo.config.ThreeGamesConfiguration;
import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.utils.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Import;

import static com.codenjoy.dojo.services.mocks.ThirdGameSettings.Keys.PARAMETER5;
import static com.codenjoy.dojo.services.mocks.ThirdGameSettings.Keys.PARAMETER6;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_ENABLED;
import static com.codenjoy.dojo.utils.smart.SmartAssert.assertEquals;

@Import(ThreeGamesConfiguration.class)
public class RestBoardControllerTest extends AbstractRestControllerTest {

    @Before
    public void setup() {
        super.setup();

        with.login.register("somePlayer", "ip", "validRoom", "first");
        with.login.asUser("somePlayer", "somePlayer");
    }

    @Test
    public void healthCheck() {
        with.login.asNone();

        assertEquals("ok", get("/rest/health"));
    }

    @Test
    public void playerWantsToPlay() {
        String code = registration.getCodeById("somePlayer");
        String expected = "{\n" +
                "  'alphabet':'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿ',\n" +
                "  'context':'/codenjoy-contest',\n" +
                "  'gameType':{\n" +
                "    'boardSize':6,\n" +
                "    'clientUrl':'/codenjoy-contest/resources/user/first-servers.zip',\n" +
                "    'helpUrl':'/codenjoy-contest/resources/help/first.html',\n" +
                "    'info':'GameType[first]',\n" +
                "    'multiplayerType':{\n" +
                "      'disposable':true,\n" +
                "      'levels':false,\n" +
                "      'levelsCount':1,\n" +
                "      'multiplayer':false,\n" +
                "      'multiple':false,\n" +
                "      'quadro':false,\n" +
                "      'roomSize':1,\n" +
                "      'single':true,\n" +
                "      'singleplayer':true,\n" +
                "      'team':false,\n" +
                "      'tournament':false,\n" +
                "      'training':false,\n" +
                "      'triple':false,\n" +
                "      'type':'single'\n" +
                "    },\n" +
                "    'parameters':[\n" +
                "      {\n" +
                "        'def':'12',\n" +
                "        'multiline':false,\n" +
                "        'name':'Parameter 1',\n" +
                "        'options':[\n" +
                "          '12',\n" +
                "          '15'\n" +
                "        ],\n" +
                "        'type':'editbox',\n" +
                "        'value':'15',\n" +
                "        'valueType':'Integer'\n" +
                "      },\n" +
                "      {\n" +
                "        'def':'true',\n" +
                "        'multiline':false,\n" +
                "        'name':'Parameter 2',\n" +
                "        'options':[\n" +
                "          'true'\n" +
                "        ],\n" +
                "        'type':'checkbox',\n" +
                "        'value':'true',\n" +
                "        'valueType':'Boolean'\n" +
                "      }\n" +
                "    ],\n" +
                "    'room':'validRoom',\n" +
                "    'sprites':{\n" +
                "      'alphabet':'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿ',\n" +
                "      'names':[\n" +
                "        'none',\n" +
                "        'wall',\n" +
                "        'hero'\n" +
                "      ],\n" +
                "      'url':'/codenjoy-contest/resources/sprite/first/*.png',\n" +
                "      'values':[\n" +
                "        ' ',\n" +
                "        '☼',\n" +
                "        '☺'\n" +
                "      ]\n" +
                "    },\n" +
                "    'version':'version 1.11b',\n" +
                "    'wsUrl':'ws[s]://SERVER:PORT/codenjoy-contest/ws?user=PLAYER_ID&code=CODE'\n" +
                "  },\n" +
                "  'players':[\n" +
                "    {\n" +
                "      'gameType':'first',\n" +
                "      'id':'somePlayer',\n" +
                "      'readableName':'somePlayer-name',\n" +
                "      'score':'0'\n" +
                "    }\n" +
                "  ],\n" +
                "  'registered':true,\n" +
                "  'sprites':[\n" +
                "    'none',\n" +
                "    'wall',\n" +
                "    'hero'\n" +
                "  ],\n" +
                "  'spritesAlphabet':' ☼☺'\n" +
                "}";
        assertEquals(expected, JsonUtils.prettyPrint(get("/rest/player/somePlayer/" + code + "/wantsToPlay/first/validRoom")));

        String expected2 = "{\n" +
                "  'alphabet':'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿ',\n" +
                "  'context':'/codenjoy-contest',\n" +
                "  'gameType':{\n" +
                "    'boardSize':5,\n" +
                "    'clientUrl':'/codenjoy-contest/resources/user/second-servers.zip',\n" +
                "    'helpUrl':'/codenjoy-contest/resources/help/second.html',\n" +
                "    'info':'GameType[second]',\n" +
                "    'multiplayerType':{\n" +
                "      'disposable':false,\n" +
                "      'levels':true,\n" +
                "      'levelsCount':10,\n" +
                "      'multiplayer':true,\n" +
                "      'multiple':false,\n" +
                "      'quadro':false,\n" +
                "      'roomSize':1,\n" +
                "      'single':false,\n" +
                "      'singleplayer':false,\n" +
                "      'team':false,\n" +
                "      'tournament':false,\n" +
                "      'training':true,\n" +
                "      'triple':false,\n" +
                "      'type':'training'\n" +
                "    },\n" +
                "    'parameters':[\n" +
                "      {\n" +
                "        'def':'43',\n" +
                "        'multiline':false,\n" +
                "        'name':'Parameter 3',\n" +
                "        'options':[\n" +
                "          '43'\n" +
                "        ],\n" +
                "        'type':'editbox',\n" +
                "        'value':'43',\n" +
                "        'valueType':'Integer'\n" +
                "      },\n" +
                "      {\n" +
                "        'def':'false',\n" +
                "        'multiline':false,\n" +
                "        'name':'Parameter 4',\n" +
                "        'options':[\n" +
                "          'false',\n" +
                "          'true'\n" +
                "        ],\n" +
                "        'type':'checkbox',\n" +
                "        'value':'true',\n" +
                "        'valueType':'Boolean'\n" +
                "      }\n" +
                "    ],\n" +
                "    'room':'validRoom2',\n" +
                "    'sprites':{\n" +
                "      'alphabet':'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿ',\n" +
                "      'names':[\n" +
                "        'none',\n" +
                "        'red',\n" +
                "        'green',\n" +
                "        'blue'\n" +
                "      ],\n" +
                "      'url':'/codenjoy-contest/resources/sprite/second/*.png',\n" +
                "      'values':[\n" +
                "        ' ',\n" +
                "        'R',\n" +
                "        'G',\n" +
                "        'B'\n" +
                "      ]\n" +
                "    },\n" +
                "    'version':'version 12',\n" +
                "    'wsUrl':'ws[s]://SERVER:PORT/codenjoy-contest/ws?user=PLAYER_ID&code=CODE'\n" +
                "  },\n" +
                "  'players':[],\n" +
                "  'registered':true,\n" +
                "  'sprites':[\n" +
                "    'none',\n" +
                "    'red',\n" +
                "    'green',\n" +
                "    'blue'\n" +
                "  ],\n" +
                "  'spritesAlphabet':' RGB'\n" +
                "}";
        assertEquals(expected2, JsonUtils.prettyPrint(get("/rest/player/somePlayer/" + code + "/wantsToPlay/second/validRoom2")));
    }

    @Test
    public void changeLevel() {
        // given
        String game = "third";
        String ip = "ip";
        String room = "room";
        String player1 = "player1";
        String player2 = "player2";

        // will be SINGLE_LEVELS with 3 levels
        with.rooms.settings(room, game)
                .bool(ROUNDS_ENABLED, false)
                .bool(PARAMETER6, true) // SINGLE_LEVELS
                .integer(PARAMETER5, 3); // 3 levels

        Deal deal1 = with.login.register(player1, ip, room, game);
        deal1.getGame().getProgress().change(1, 2);
        String code1 = deal1.getPlayer().getCode();

        Deal deal2 = with.login.register(player2, ip, room, game);
        deal2.getGame().getProgress().change(1, 0);
        String code2 = deal2.getPlayer().getCode();

        // then
        assertEquals("{'current':1,'passed':2,'total':3,'valid':true}",
                quote(get(String.format("/rest/player/%s/%s/level", player1, code1))));

        assertEquals("{'current':1,'passed':0,'total':3,'valid':true}",
                quote(get(String.format("/rest/player/%s/%s/level", player2, code2))));

        // when
        // player1 last passed is enough to change level up to 2
        assertEquals("true",
                get(String.format("/rest/player/%s/%s/level/2", player1, code1)));

        // then
        assertEquals("{'current':2,'passed':2,'total':3,'valid':true}",
                quote(get(String.format("/rest/player/%s/%s/level", player1, code1))));

        assertEquals("{'current':1,'passed':0,'total':3,'valid':true}",
                quote(get(String.format("/rest/player/%s/%s/level", player2, code2))));

        // when
        // player2 last passed is not enough to change level up to 2
        assertEquals("false",
                get(String.format("/rest/player/%s/%s/level/2", player2, code2)));

        // then
        assertEquals("{'current':2,'passed':2,'total':3,'valid':true}",
                quote(get(String.format("/rest/player/%s/%s/level", player1, code1))));

        assertEquals("{'current':1,'passed':0,'total':3,'valid':true}",
                quote(get(String.format("/rest/player/%s/%s/level", player2, code2))));

        // when
        // player1 last passed is enough to change level up to 3
        assertEquals("true",
                get(String.format("/rest/player/%s/%s/level/3", player1, code1)));

        // then
        assertEquals("{'current':3,'passed':2,'total':3,'valid':true}",
                quote(get(String.format("/rest/player/%s/%s/level", player1, code1))));

        assertEquals("{'current':1,'passed':0,'total':3,'valid':true}",
                quote(get(String.format("/rest/player/%s/%s/level", player2, code2))));
    }
}