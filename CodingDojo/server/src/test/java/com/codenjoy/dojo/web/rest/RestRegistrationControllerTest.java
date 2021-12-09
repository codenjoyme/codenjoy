package com.codenjoy.dojo.web.rest;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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
import org.junit.Test;
import org.springframework.context.annotation.Import;

import static com.codenjoy.dojo.client.Utils.split;
import static com.codenjoy.dojo.utils.smart.SmartAssert.assertEquals;

@Import(ThreeGamesConfiguration.class)
public class RestRegistrationControllerTest extends AbstractRestControllerTest {

    @Test
    public void shouldGetRoomPlayers_success() {
        // given
        with.login.register("player1", "ip1", "room1", "first");
        with.login.register("player2", "ip2", "room1", "first");
        with.login.register("player3", "ip3", "room1", "first");
        with.login.register("player4", "ip4", "room2", "first");
        with.login.register("player5", "ip5", "room2", "first");
        with.login.register("player6", "ip6", "room3", "second");

        with.login.asUser("player1", "player1");

        // when then
        assertEquals("[{'gameType':'first','id':'player1','readableName':'player1-name','score':'0'},\n" +
                        "{'gameType':'first','id':'player2','readableName':'player2-name','score':'0'},\n" +
                        "{'gameType':'first','id':'player3','readableName':'player3-name','score':'0'}]",
                fix(get("/rest/room/room1/players")));

        // when then
        assertEquals("[{'gameType':'first','id':'player4','readableName':'player4-name','score':'0'},\n" +
                        "{'gameType':'first','id':'player5','readableName':'player5-name','score':'0'}]",
                fix(get("/rest/room/room2/players")));

        // when then
        assertEquals("[{'gameType':'second','id':'player6','readableName':'player6-name','score':'0'}]",
                fix(get("/rest/room/room3/players")));
    }

    @Test
    public void shouldGetRoomPlayers_failure() {
        // when then
        assertGetError("java.lang.IllegalArgumentException: Room name is invalid: 'bad$room'",
                "/rest/room/bad$room/players");

        // when then
        assertGetError("java.lang.IllegalArgumentException: Room name is invalid: 'null'",
                "/rest/room/null/players");

        // when then
        assertGetError(404, "Not a json value: ''",
                "/rest/room//players");

        // when then
        assertEquals("[]",
                fix(get("/rest/room/notExistsGame/players")));
    }

    @Test
    public void shouldGetGamePlayers_success() {
        // given
        with.login.register("player1", "ip1", "room1", "first");
        with.login.register("player2", "ip2", "room1", "first");
        with.login.register("player3", "ip3", "room1", "first");
        with.login.register("player4", "ip4", "room2", "first");
        with.login.register("player5", "ip5", "room2", "first");
        with.login.register("player6", "ip6", "room3", "second");

        with.login.asUser("player1", "player1");

        // when then
        assertEquals("[{'gameType':'first','id':'player1','readableName':'player1-name','score':'0'},\n" +
                        "{'gameType':'first','id':'player2','readableName':'player2-name','score':'0'},\n" +
                        "{'gameType':'first','id':'player3','readableName':'player3-name','score':'0'},\n" +
                        "{'gameType':'first','id':'player4','readableName':'player4-name','score':'0'},\n" +
                        "{'gameType':'first','id':'player5','readableName':'player5-name','score':'0'}]",
                fix(get("/rest/game/first/players")));

        // when then
        assertEquals("[{'gameType':'second','id':'player6','readableName':'player6-name','score':'0'}]",
                fix(get("/rest/game/second/players")));
    }

    @Test
    public void shouldGetGamePlayers_failure() {
        // when then
        assertGetError("java.lang.IllegalArgumentException: Game name is invalid: 'bad$game'",
                "/rest/game/bad$game/players");

        // when then
        assertGetError("java.lang.IllegalArgumentException: Game name is invalid: 'null'",
                "/rest/game/null/players");

        // when then
        assertGetError(404, "Not a json value: ''",
                "/rest/game//players");

        // when then
        assertEquals("[]",
                fix(get("/rest/game/notExistsGame/players")));
    }

    private String fix(String string) {
        return split(quote(string),
                "'},\n{'");
    }

    @Test
    public void shouldIsPlayerExists_success() {
        // given
        with.login.register("player1", "ip1", "room1", "first");
        with.login.register("player2", "ip2", "room2", "first");
        with.login.register("player3", "ip3", "room2", "first");
        with.login.register("player4", "ip4", "room4", "second");

        with.login.gameOver("player3");
        with.login.asUser("player1", "player1");

        // when then
        assertEquals("true", get("/rest/player/player1/exists"));           // logged in
        assertEquals("true", get("/rest/player/player2/exists"));           // other room
        assertEquals("false", get("/rest/player/player3/exists"));          // game over
        assertEquals("true", get("/rest/player/player4/exists"));           // other game
        assertEquals("false", get("/rest/player/notExistsPlayer/exists"));  // not exists
    }

    @Test
    public void shouldIsPlayerExists_failure() {
        // when then
        assertGetError("java.lang.IllegalArgumentException: Player id is invalid: 'bad$player'",
                "/rest/player/bad$player/exists");

        // when then
        assertGetError("java.lang.IllegalArgumentException: Player id is invalid: 'null'",
                "/rest/player/null/exists");

        // when then
        assertGetError(404, "Not a json value: ''",
                "/rest/player//exists");
    }

    @Test
    public void shouldCheckUserLogin_success() {
        // given
        Deal deal1 = with.login.register("player1", "ip1", "room1", "first");
        Deal deal2 = with.login.register("player2", "ip2", "room2", "first");
        Deal deal3 = with.login.register("player3", "ip3", "room2", "first");
        Deal deal4 = with.login.register("player4", "ip4", "room4", "second");

        String code1 = deal1.getPlayer().getCode();
        String code2 = deal2.getPlayer().getCode();
        String code3 = deal3.getPlayer().getCode();
        String code4 = deal4.getPlayer().getCode();
        String badCode = "000000000";

        with.login.gameOver("player3");
        with.login.asUser("player1", "player1");

        // when then
        assertEquals("true",  get("/rest/player/player1/check/" + code1));   // logged in
        assertEquals("false", get("/rest/player/player1/check/" + badCode)); //
        assertEquals("true",  get("/rest/player/player2/check/" + code2));   // other room
        assertEquals("false", get("/rest/player/player2/check/" + badCode)); //
        assertEquals("true",  get("/rest/player/player3/check/" + code3));   // game over
        assertEquals("false", get("/rest/player/player3/check/" + badCode)); //
        assertEquals("true",  get("/rest/player/player4/check/" + code4));   // other game
        assertEquals("false", get("/rest/player/player4/check/" + badCode)); //
        assertEquals("false", get("/rest/player/notExistsPlayer/check/" + badCode)); // not exists
    }

    @Test
    public void shouldCheckUserLogin_failure() {
        // when then
        assertGetError("java.lang.IllegalArgumentException: Player id is invalid: 'bad$player'",
                "/rest/player/bad$player/check/0000000");

        // when then
        assertGetError("java.lang.IllegalArgumentException: Player id is invalid: 'null'",
                "/rest/player/null/check/0000000");

        // when then
        assertGetError("java.lang.IllegalArgumentException: Player code is invalid: 'bad$Code'",
                "/rest/player/player/check/bad$Code");

        // when then
        assertGetError("java.lang.IllegalArgumentException: Player code is invalid: 'null'",
                "/rest/player/player/check/null");

        // when then
        assertGetError(404, "Not a json value: ''",
                "/rest/player//check/");
    }
}
