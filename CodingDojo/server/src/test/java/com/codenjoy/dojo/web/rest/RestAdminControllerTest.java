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

import com.codenjoy.dojo.config.SemifinalGamesConfiguration;
import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.web.rest.pojo.PParameters;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

import static com.codenjoy.dojo.client.Utils.split;
import static com.codenjoy.dojo.utils.smart.SmartAssert.assertEquals;
import static java.util.stream.Collectors.toList;
import static org.mockito.Mockito.*;

@Import(SemifinalGamesConfiguration.class)
public class RestAdminControllerTest extends AbstractRestControllerTest {

    @Autowired
    private RestAdminController service;

    @Before
    public void setup() {
        with.login.asAdmin();
        super.setup();
    }

    @Test
    public void shouldStartStopGame_oneRoom() {
        // given
        assertEquals(false, service.isRoomActive("name"));
        assertEquals("false", get("/rest/admin/room/name/pause"));

        with.login.register("player", "ip", "name", "first");

        // TODO я думаю что сервис напрямую дергать не надо, т.к. его никто так вызывать не будет, только через rest
        assertEquals(true, service.isRoomActive("name"));
        assertEquals("true", get("/rest/admin/room/name/pause"));

        assertPlayersInActiveRooms("[player->name]");

        // when
        service.setRoomActive("name", false);

        // then
        assertEquals(false, service.isRoomActive("name"));
        assertEquals("false", get("/rest/admin/room/name/pause"));

        assertPlayersInActiveRooms("[]");

        // when
        assertEquals("", get("/rest/admin/room/name/pause/true"));

        // then
        assertEquals(true, service.isRoomActive("name"));
        assertEquals("true", get("/rest/admin/room/name/pause"));

        assertPlayersInActiveRooms("[player->name]");
    }

    public void assertPlayersInActiveRooms(String expected) {
        assertEquals(expected,
                deals.getAll(rooms::isRoomActive).stream()
                    .map(deal -> deal.getPlayer().getId() + "->" + deal.getRoom())
                    .collect(toList())
                    .toString());
    }

    @Test
    public void shouldStartStopGame_severalRooms() {
        // given
        assertEquals(false, service.isRoomActive("name1"));
        assertEquals("false", get("/rest/admin/room/name2/pause"));

        with.login.register("player1", "ip1", "name1", "first");
        with.login.register("player2", "ip2", "name1", "first");
        with.login.register("player3", "ip3", "name2", "second");

        assertEquals(true, service.isRoomActive("name1"));
        assertEquals("true", get("/rest/admin/room/name2/pause"));

        assertPlayersInActiveRooms("[player1->name1, player2->name1, player3->name2]");

        // when
        service.setRoomActive("name1", false);

        // then
        assertEquals(false, service.isRoomActive("name1"));
        assertEquals("true", get("/rest/admin/room/name2/pause"));

        assertPlayersInActiveRooms("[player3->name2]");

        // when
        assertEquals("", get("/rest/admin/room/name1/pause/true"));

        // then
        assertEquals(true, service.isRoomActive("name1"));
        assertEquals("true", get("/rest/admin/room/name2/pause"));

        assertPlayersInActiveRooms("[player1->name1, player2->name1, player3->name2]");
    }

    @Test
    public void shouldStartStopGame_validation() {
        // when then
        assertException("Room name is invalid: '$bad$'",
                () -> service.isRoomActive("$bad$"));

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/admin/room/$bad$/pause/true");

        // when then
        assertException("Room name is invalid: '$bad$'",
                () -> service.setRoomActive("$bad$", true));

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/admin/room/$bad$/pause");
    }

    @Test
    public void shouldOpenCloseRoomRegistration_oneRoom() {
        // given
        // комнаты нет, но в нее можно зарегаться
        assertEquals(true, service.isRoomRegistrationOpened("name"));
        assertEquals("true", get("/rest/admin/room/name/registration/open"));

        with.login.register("player1", "ip1", "name", "first");
        assertPlayersInActiveRooms("[player1->name]");

        // а теперь она есть, т.к. зашел первый пользователь
        // и по умолчанию регистрация так же открыта
        assertEquals(true, service.isRoomRegistrationOpened("name"));
        assertEquals("true", get("/rest/admin/room/name/registration/open"));

        assertEquals(true, players.isRegistrationOpened("name"));

        // when
        service.setRoomRegistrationOpened("name", false);

        // then
        assertEquals(false, service.isRoomRegistrationOpened("name"));
        assertEquals("false", get("/rest/admin/room/name/registration/open"));

        assertEquals(false, players.isRegistrationOpened("name"));

        // when
        with.login.register("player2", "ip2", "name", "first"); // not created

        // then
        assertPlayersInActiveRooms("[player1->name]");

        // when
        assertEquals("", get("/rest/admin/room/name/registration/open/true"));

        // then
        assertEquals(true, service.isRoomRegistrationOpened("name"));
        assertEquals("true", get("/rest/admin/room/name/registration/open"));

        assertEquals(true, players.isRegistrationOpened("name"));

        // when
        with.login.register("player2", "ip2", "name", "first");

        // then
        assertPlayersInActiveRooms("[player1->name, player2->name]");
    }

    @Test
    public void shouldOpenCloseRoomRegistration_severalRooms() {
        // given
        // комнаты нет, но в нее можно зарегаться
        assertEquals(true, service.isRoomRegistrationOpened("name1"));
        assertEquals("true", get("/rest/admin/room/name2/registration/open"));

        with.login.register("player1", "ip1", "name1", "first");
        with.login.register("player2", "ip2", "name1", "first");
        with.login.register("player3", "ip3", "name2", "second");
        assertPlayersInActiveRooms("[player1->name1, player2->name1, player3->name2]");

        // а теперь она есть, т.к. зашел первый пользователь
        // и по умолчанию регистрация так же открыта
        assertEquals(true, service.isRoomRegistrationOpened("name1"));
        assertEquals("true", get("/rest/admin/room/name2/registration/open"));

        assertEquals(true, players.isRegistrationOpened("name1"));
        assertEquals(true, players.isRegistrationOpened("name2"));

        // when
        service.setRoomRegistrationOpened("name1", false);

        // then
        assertEquals(false, service.isRoomRegistrationOpened("name1"));
        assertEquals("true", get("/rest/admin/room/name2/registration/open"));

        assertEquals(false, players.isRegistrationOpened("name1"));
        assertEquals(true, players.isRegistrationOpened("name2"));

        // when
        with.login.register("player4", "ip4", "name1", "first"); // not created
        with.login.register("player5", "ip5", "name2", "second");

        // then
        assertPlayersInActiveRooms("[player1->name1, player2->name1, player3->name2, " +
                "player5->name2]");

        // when
        assertEquals("", get("/rest/admin/room/name1/registration/open/true"));

        // then
        assertEquals(true, service.isRoomRegistrationOpened("name1"));
        assertEquals("true", get("/rest/admin/room/name2/registration/open"));

        assertEquals(true, players.isRegistrationOpened("name1"));
        assertEquals(true, players.isRegistrationOpened("name2"));

        // when
        with.login.register("player6", "ip6", "name1", "first");
        with.login.register("player7", "ip7", "name2", "second");

        // then
        assertPlayersInActiveRooms("[player1->name1, player2->name1, player3->name2, " +
                "player5->name2, player6->name1, player7->name2]");
    }

    @Test
    public void shouldOpenCloseRoomRegistration_validation() {
        // when then
        assertException("Room name is invalid: '$bad$'",
                () -> service.isRoomRegistrationOpened("$bad$"));

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/admin/room/$bad$/registration/open/true");

        // when then
        assertException("Room name is invalid: '$bad$'",
                () -> service.setRoomRegistrationOpened("$bad$", true));

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/admin/room/$bad$/registration/open");
    }

    @Test
    public void shouldSetClearScores() {
        // given
        with.login.register("player1", "ip1", "room1", "first");
        with.login.register("player2", "ip2", "room1", "first");

        with.login.register("player3", "ip3", "room2", "first");

        with.login.register("player4", "ip4", "room3", "second");

        assertScores("{player1=0, player2=0, player3=0, player4=0}");

        // when
        service.setScores("room1", "player1", "10");
        service.setScores("room1", "player2", "20");
        assertEquals("", get("/rest/admin/room/room2/scores/player3/set/30"));
        assertEquals("", get("/rest/admin/room/room3/scores/player4/set/40"));

        // then
        assertScores("{player1=10, player2=20, player3=30, player4=40}");

        // when
        assertEquals("", get("/rest/admin/room/room1/scores/clear"));

        // then
        assertScores("{player1=0, player2=0, player3=30, player4=40}");

        // when
        service.cleanScores("room2");

        // then
        assertScores("{player1=0, player2=0, player3=0, player4=40}");
    }

    private void assertScores(String expected) {
        assertEquals(expected, dealsView.getScores().toString());
    }

    @Test
    public void shouldSetClearScores_validation() {
        // when then
        assertException("Room name is invalid: '$bad$'",
                () -> service.cleanScores("$bad$"));

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/admin/room/$bad$/scores/clear");

        // when then
        assertException("Room name is invalid: '$bad$'",
                () -> service.setScores("$bad$", "id", "12"));

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/admin/room/$bad$/scores/player3/set/30");

        // when then
        assertException("Parameter score is empty: ''",
                () -> service.setScores("room", "id", ""));

        assertError("java.lang.IllegalArgumentException: Parameter score is empty: 'null'",
                "/rest/admin/room/room/scores/player3/set/null");

        // when then
        assertException("Player id is invalid: '$badPlayer$'",
                () -> service.setScores("room", "$badPlayer$", "10"));

        assertError("java.lang.IllegalArgumentException: Player id is invalid: '$badPlayer$'",
                "/rest/admin/room/room/scores/$badPlayer$/set/30");
    }

    @Test
    public void shouldReloadAllPlayersInRoom() {
        // given
        Deal deal1 = with.login.register("player1", "ip1", "room1", "first");
        Deal deal2 = with.login.register("player2", "ip2", "room1", "first");

        Deal deal3 = with.login.register("player3", "ip3", "room2", "first");

        Deal deal4 = with.login.register("player4", "ip4", "room3", "second");

        Arrays.asList(deal1, deal2, deal3, deal4).stream()
                        .map(Deal::getField)
                        .forEach(Mockito::reset);

        // when
        assertEquals("", get("/rest/admin/room/room1/board/reload"));

        // then
        verifyNewGame(deal1, atLeastOnce());
        verifyNewGame(deal2, atLeastOnce());
        verifyNewGame(deal3, never());
        verifyNewGame(deal4, never());
    }

    @Test
    public void shouldReloadAllPlayersInRoom_validation() {
        assertException("Room name is invalid: '$bad$'",
                () -> service.reloadPlayers("$bad$"));

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/admin/room/$bad$/board/reload");
    }

    @Test
    public void shouldGameOverInRoom() {
        // given
        with.login.register("player1", "ip1", "room1", "first");
        with.login.register("player2", "ip2", "room1", "first");
        with.login.register("player3", "ip3", "room1", "first");

        with.login.register("player4", "ip4", "room2", "first");
        with.login.register("player5", "ip5", "room2", "first");

        with.login.register("player6", "ip6", "room3", "second");

        with.login.register("player7", "ip7", "room4", "second");

        // when
        assertEquals("", get("/rest/admin/room/room3/gameOver/player6"));

        // then
        assertRooms("[[player7], [player1, player2, player3], [player4, player5]]");

        // when
        assertEquals("", get("/rest/admin/room/room1/gameOverAll"));

        // then
        assertRooms("[[player7], [player4, player5]]");

        // when
        service.gameOver("room4", "player7");

        // then
        assertRooms("[[player4, player5]]");

        // when
        service.gameOverAll("room2");

        // then
        assertRooms("[]");
    }

    @Test
    public void shouldGameOverInRoom_severalPlayers() {
        // given
        with.login.register("player1", "ip1", "room1", "first");
        with.login.register("player2", "ip2", "room1", "first");
        with.login.register("player3", "ip3", "room1", "first");
        with.login.register("player4", "ip4", "room1", "first");
        with.login.register("player5", "ip5", "room1", "first");

        with.login.register("player6", "ip6", "room2", "first");
        with.login.register("player7", "ip7", "room2", "first");

        with.login.register("player8", "ip8", "room3", "second");

        // when
        assertEquals("", get("/rest/admin/room/room1/gameOver",
                unquote("{'ids':['player2','player4']}")));

        // then
        assertRooms("[[player8], [player1, player3, player5], [player6, player7]]");

        // when
        assertEquals("", get("/rest/admin/room/room1/gameOver",
                unquote("{'ids':['player1']}")));

        // then
        assertRooms("[[player8], [player3, player5], [player6, player7]]");

        // when
        assertEquals("", get("/rest/admin/room/room1/gameOver",
                unquote("{'ids':['player3','player5']}")));

        // then
        assertRooms("[[player8], [player6, player7]]");

        // when
        assertEquals("", get("/rest/admin/room/room2/gameOver",
                unquote("{'ids':['player6']}")));

        // then
        assertRooms("[[player8], [player7]]");

        // when
        assertEquals("", get("/rest/admin/room/room3/gameOver",
                unquote("{'ids':['player8']}")));

        // then
        assertRooms("[[player7]]");

        // when
        assertEquals("", get("/rest/admin/room/room3/gameOver",
                unquote("{'ids':['player7']}")));

        // then
        assertRooms("[]");
    }

    private void assertRooms(String expected) {
        assertEquals(expected, dealsView.getGroupsByRooms().toString());
    }

    @Test
    public void shouldGameOverInRoom_validation() {
        // given
        with.login.register("validPlayer", "ip", "validRoom", "first");

        // when then
        assertException("Room name is invalid: '$bad$'",
                () -> service.gameOverAll("$bad$"));

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/admin/room/$bad$/gameOverAll");

        // when then
        assertException("Room name is invalid: '$bad$'",
                () -> service.gameOver("$bad$", "validPlayer"));

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/admin/room/$bad$/gameOver/validPlayer");

        // when then
        assertException("Player id is invalid: '$bad$'",
                () -> service.gameOver("validRoom", "$bad$"));

        assertError("java.lang.IllegalArgumentException: Player id is invalid: '$bad$'",
                "/rest/admin/room/validRoom/gameOver/$bad$");

        // when then
        assertException("Player 'validPlayer' is not in room 'otherRoom'",
                () -> service.gameOver("otherRoom", "validPlayer"));

        assertError("java.lang.IllegalArgumentException: Player 'validPlayer' is not in room 'otherRoom'",
                "/rest/admin/room/otherRoom/gameOver/validPlayer");
    }

    private void verifyNewGame(Deal deal, VerificationMode mode) {
        verify(deal.getField(), mode).newGame(deal.getGame().getPlayer());
    }

    @Test
    public void shouldSaveAllAndLoad() {
        // given
        with.login.register("player1", "ip1", "room1", "first");
        with.login.register("player2", "ip2", "room1", "first");
        with.login.register("player3", "ip3", "room2", "first");
        with.login.register("player4", "ip4", "room3", "second");

        service.setScores("room1", "player1", "10");
        service.setScores("room1", "player2", "20");
        service.setScores("room2", "player3", "30");
        service.setScores("room3", "player4", "40");

        assertScores("{player1=10, player2=20, player3=30, player4=40}");

        // when
        service.saveAll("room1");
        assertEquals("", get("/rest/admin/room/room2/saveAll"));

        assertEquals("", get("/rest/admin/room/room1/scores/clear"));
        assertEquals("", get("/rest/admin/room/room2/scores/clear"));
        assertEquals("", get("/rest/admin/room/room3/scores/clear"));

        // then
        assertScores("{player1=0, player2=0, player3=0, player4=0}");

        // when
        service.load("room1", "player1");    // saved, cleaned - last value is 0
        // service.load("room1", "player2"); // saved, cleaned - last value is 0 = do not load
        assertEquals("", get("/rest/admin/room/room2/load/player3")); // saved, cleaned - last value is 0
        assertEquals("", get("/rest/admin/room/room3/load/player4")); // not saved, cleaned - last value is 0

        // then
        assertScores("{player1=0, player2=0, player3=0, player4=0}");

        // when
        service.setScores("room1", "player1", "11");
        service.setScores("room1", "player2", "22");
        service.setScores("room2", "player3", "33");
        service.setScores("room3", "player4", "44");

        service.saveAll("room1");
        assertEquals("", get("/rest/admin/room/room2/saveAll"));

        // then
        assertScores("{player1=11, player2=22, player3=33, player4=44}");

        service.gameOverAll("room1");
        service.gameOverAll("room2");
        service.gameOverAll("room3");

        // then
        assertScores("{}");

        service.load("room1", "player1");    // saved, cleaned, saved - last value is 11
        // service.load("room1", "player2"); // saved, cleaned, saved - last value is 22, do not load
        service.load("room2", "player3");    // saved, cleaned, saved - last value is 33
        service.load("room3", "player4");    // not saved, cleaned    - last value is 0

        // then
        assertScores("{player1=11, player3=33, player4=0}");
    }

    @Test
    public void shouldSaveAllAndLoad_validation() {
        // when then
        assertException("Room name is invalid: '$bad$'",
                () -> service.saveAll("$bad$"));

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/admin/room/$bad$/saveAll");

        // when then
        assertException("Room name is invalid: '$bad$'",
                () -> service.load("$bad$", "validPlayer"));

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/admin/room/$bad$/load/validPlayer");

        // when then
        assertException("Player id is invalid: '$bad$'",
                () -> service.load("validRoom", "$bad$"));

        assertError("java.lang.IllegalArgumentException: Player id is invalid: '$bad$'",
                "/rest/admin/room/validRoom/load/$bad$");
    }

    @Test
    public void shouldGetGameSettings_theseSettingsCannotBeChangedByChangingRoomSettings() {
        // given
        // custom room settings
        PParameters settings1 = service.getSettings("name1", "first");
        assertEqualsSettingsParameters(settings1,
                "[[Parameter 1:Integer = multiline[false] def[12] val[15]], \n" +
                "[Parameter 2:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Enabled:Boolean = def[false] val[false]], \n" +
                "[[Semifinal] Timeout:Integer = multiline[false] def[900] val[900]], \n" +
                "[[Semifinal] Percentage:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Limit:Integer = multiline[false] def[50] val[50]], \n" +
                "[[Semifinal] Reset board:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Shuffle board:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Clear scores:Boolean = def[false] val[false]]]");

        // when then
        // get game settings
        JSONObject settings2 = new JSONObject(get("/rest/admin/game/first/settings"));
        assertEqualsSettingsJson(settings2,
                "{'parameters':[{'def':'12','multiline':false,'name':'Parameter 1','options':['12','15'],'type':'editbox','value':'15','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'Parameter 2','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Enabled','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'},\n" +
                "{'def':'900','multiline':false,'name':'[Semifinal] Timeout','options':['900'],'type':'editbox','value':'900','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Percentage','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'50','multiline':false,'name':'[Semifinal] Limit','options':['50'],'type':'editbox','value':'50','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Reset board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Shuffle board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Clear scores','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'}]}");

        // when
        // change room settings
        JSONObject updated = new JSONObject(settings2.toString());
        updated.getJSONArray("parameters").getJSONObject(0).put("value", 55);
        updated.getJSONArray("parameters").getJSONObject(1).put("value", true);
        assertEquals("", post(200, "/rest/admin/room/name1/settings/first",
                updated.toString()));

        // then
        // room settings changed
        assertEqualsSettingsJson(new JSONObject(get("/rest/admin/room/name1/settings/first")),
                "{'parameters':[{'def':'12','multiline':false,'name':'Parameter 1','options':['12','55'],'type':'editbox','value':'55','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'Parameter 2','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Enabled','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'},\n" +
                "{'def':'900','multiline':false,'name':'[Semifinal] Timeout','options':['900'],'type':'editbox','value':'900','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Percentage','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'50','multiline':false,'name':'[Semifinal] Limit','options':['50'],'type':'editbox','value':'50','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Reset board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Shuffle board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Clear scores','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'}]}");

        // then
        // game settings are the same
        assertEqualsSettingsJson(
                new JSONObject(get("/rest/admin/game/first/settings")),
                splitSettings(settings2));
    }

    private void assertEqualsSettingsJson(JSONObject settings, String expected) {
        assertEquals(expected, splitSettings(settings));
    }

    private String splitSettings(JSONObject settings) {
        return split(quote(settings.toString()), "},\n{'def'");
    }

    private void assertEqualsSettingsParameters(PParameters settings, String expected) {
        assertEquals(expected, split(settings.build(), "]], \n["));
    }

    @Test
    public void shouldGetSetRoomSettings() {
        PParameters settings1 = service.getSettings("name1", "first");
        assertEqualsSettingsParameters(settings1,
                "[[Parameter 1:Integer = multiline[false] def[12] val[15]], \n" +
                "[Parameter 2:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Enabled:Boolean = def[false] val[false]], \n" +
                "[[Semifinal] Timeout:Integer = multiline[false] def[900] val[900]], \n" +
                "[[Semifinal] Percentage:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Limit:Integer = multiline[false] def[50] val[50]], \n" +
                "[[Semifinal] Reset board:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Shuffle board:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Clear scores:Boolean = def[false] val[false]]]");

        JSONObject settings2 = new JSONObject(get("/rest/admin/room/name2/settings/second"));
        assertEqualsSettingsJson(settings2,
                "{'parameters':[{'def':'43','multiline':false,'name':'Parameter 3','options':['43'],'type':'editbox','value':'43','valueType':'Integer'},\n" +
                "{'def':'false','multiline':false,'name':'Parameter 4','options':['false','true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Enabled','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'},\n" +
                "{'def':'900','multiline':false,'name':'[Semifinal] Timeout','options':['900'],'type':'editbox','value':'900','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Percentage','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'50','multiline':false,'name':'[Semifinal] Limit','options':['50'],'type':'editbox','value':'50','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Reset board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Shuffle board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Clear scores','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'}]}");

        // when
        settings1.getParameters().get(0).setValue("30");
        settings1.getParameters().get(1).setValue("false");

        settings2.getJSONArray("parameters").getJSONObject(0).put("value", 55);
        settings2.getJSONArray("parameters").getJSONObject(1).put("value", true);

        service.setSettings("name1", "first", settings1);
        assertEquals("", post(200, "/rest/admin/room/name2/settings/second",
                settings2.toString()));

        // then
        settings1 = service.getSettings("name1", "first");
        assertEqualsSettingsParameters(settings1,
                "[[Parameter 1:Integer = multiline[false] def[12] val[30]], \n" +
                "[Parameter 2:Boolean = def[true] val[false]], \n" +
                "[[Semifinal] Enabled:Boolean = def[false] val[false]], \n" +
                "[[Semifinal] Timeout:Integer = multiline[false] def[900] val[900]], \n" +
                "[[Semifinal] Percentage:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Limit:Integer = multiline[false] def[50] val[50]], \n" +
                "[[Semifinal] Reset board:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Shuffle board:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Clear scores:Boolean = def[false] val[false]]]");

        settings2 = new JSONObject(get("/rest/admin/room/name2/settings/second"));
        assertEqualsSettingsJson(settings2,
                "{'parameters':[{'def':'43','multiline':false,'name':'Parameter 3','options':['43','55'],'type':'editbox','value':'55','valueType':'Integer'},\n" +
                "{'def':'false','multiline':false,'name':'Parameter 4','options':['false','true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Enabled','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'},\n" +
                "{'def':'900','multiline':false,'name':'[Semifinal] Timeout','options':['900'],'type':'editbox','value':'900','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Percentage','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'50','multiline':false,'name':'[Semifinal] Limit','options':['50'],'type':'editbox','value':'50','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Reset board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Shuffle board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Clear scores','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'}]}");
    }

    // тест такой же как и прошлый, только мы апдейтим не весь сеттинг каким он пришел,
    // а только name/value этого должно быть достаточно
    @Test
    public void shouldSetRoomSettings_onlyKeyValue() {
        PParameters settings1 = service.getSettings("name1", "first");
        assertEqualsSettingsParameters(settings1,
                "[[Parameter 1:Integer = multiline[false] def[12] val[15]], \n" +
                "[Parameter 2:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Enabled:Boolean = def[false] val[false]], \n" +
                "[[Semifinal] Timeout:Integer = multiline[false] def[900] val[900]], \n" +
                "[[Semifinal] Percentage:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Limit:Integer = multiline[false] def[50] val[50]], \n" +
                "[[Semifinal] Reset board:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Shuffle board:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Clear scores:Boolean = def[false] val[false]]]");

        JSONObject settings2 = new JSONObject(get("/rest/admin/room/name2/settings/second"));
        assertEqualsSettingsJson(settings2,
                "{'parameters':[{'def':'43','multiline':false,'name':'Parameter 3','options':['43'],'type':'editbox','value':'43','valueType':'Integer'},\n" +
                "{'def':'false','multiline':false,'name':'Parameter 4','options':['false','true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Enabled','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'},\n" +
                "{'def':'900','multiline':false,'name':'[Semifinal] Timeout','options':['900'],'type':'editbox','value':'900','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Percentage','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'50','multiline':false,'name':'[Semifinal] Limit','options':['50'],'type':'editbox','value':'50','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Reset board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Shuffle board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Clear scores','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'}]}");

        // when
        // обновляем сеттинги передавая только ключик и значение
        assertEquals("", post(200, "/rest/admin/room/name1/settings/first",
                unquote("{'parameters':[" +
                        "{'name':'Parameter 1','value':30}," +
                        "{'name':'Parameter 2','value':false}]}")));

        assertEquals("", post(200, "/rest/admin/room/name2/settings/second",
                unquote("{'parameters':[" +
                        "{'name':'Parameter 3','value':55}," +
                        "{'name':'Parameter 4','value':true}]}")));

        // then
        settings1 = service.getSettings("name1", "first");
        assertEqualsSettingsParameters(settings1,
                "[[Parameter 1:Integer = multiline[false] def[12] val[30]], \n" +
                "[Parameter 2:Boolean = def[true] val[false]], \n" +
                "[[Semifinal] Enabled:Boolean = def[false] val[false]], \n" +
                "[[Semifinal] Timeout:Integer = multiline[false] def[900] val[900]], \n" +
                "[[Semifinal] Percentage:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Limit:Integer = multiline[false] def[50] val[50]], \n" +
                "[[Semifinal] Reset board:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Shuffle board:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Clear scores:Boolean = def[false] val[false]]]");

        settings2 = new JSONObject(get("/rest/admin/room/name2/settings/second"));
        assertEqualsSettingsJson(settings2,
                "{'parameters':[{'def':'43','multiline':false,'name':'Parameter 3','options':['43','55'],'type':'editbox','value':'55','valueType':'Integer'},\n" +
                "{'def':'false','multiline':false,'name':'Parameter 4','options':['false','true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Enabled','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'},\n" +
                "{'def':'900','multiline':false,'name':'[Semifinal] Timeout','options':['900'],'type':'editbox','value':'900','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Percentage','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'50','multiline':false,'name':'[Semifinal] Limit','options':['50'],'type':'editbox','value':'50','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Reset board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Shuffle board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Clear scores','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'}]}");
    }

    // тест такой же как и прошлый, только мы проверяем группу настроек semifinal
    @Test
    public void shouldSetRoomSettings_onlyKeyValue_semifinal() {
        // given
        PParameters settings1 = service.getSettings("name1", "first");
        assertEqualsSettingsParameters(settings1,
                "[[Parameter 1:Integer = multiline[false] def[12] val[15]], \n" +
                "[Parameter 2:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Enabled:Boolean = def[false] val[false]], \n" +
                "[[Semifinal] Timeout:Integer = multiline[false] def[900] val[900]], \n" +
                "[[Semifinal] Percentage:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Limit:Integer = multiline[false] def[50] val[50]], \n" +
                "[[Semifinal] Reset board:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Shuffle board:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Clear scores:Boolean = def[false] val[false]]]");

        JSONObject settings2 = new JSONObject(get("/rest/admin/room/name2/settings/second"));
        assertEqualsSettingsJson(settings2,
                "{'parameters':[{'def':'43','multiline':false,'name':'Parameter 3','options':['43'],'type':'editbox','value':'43','valueType':'Integer'},\n" +
                "{'def':'false','multiline':false,'name':'Parameter 4','options':['false','true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Enabled','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'},\n" +
                "{'def':'900','multiline':false,'name':'[Semifinal] Timeout','options':['900'],'type':'editbox','value':'900','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Percentage','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'50','multiline':false,'name':'[Semifinal] Limit','options':['50'],'type':'editbox','value':'50','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Reset board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Shuffle board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Clear scores','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'}]}");

        // when
        // обновляем сеттинги первой комнаты передавая только ключик и значение
        assertEquals("", post(200, "/rest/admin/room/name1/settings/first",
                unquote("{'parameters':[" +
                        "{'name':'[Semifinal] Enabled','value':true}," +
                        "{'name':'[Semifinal] Timeout','value':500}," +
                        "{'name':'[Semifinal] Percentage','value':false}," +
                        "{'name':'[Semifinal] Limit','value':70}," +
                        "{'name':'[Semifinal] Reset board','value':false}," +
                        "{'name':'[Semifinal] Shuffle board','value':false}," +
                        "{'name':'[Semifinal] Clear scores','value':false}" +
                        "]}")));

        // then
        // смотрим что поменялось
        settings1 = service.getSettings("name1", "first");
        assertEqualsSettingsParameters(settings1,
                "[[Parameter 1:Integer = multiline[false] def[12] val[15]], \n" +
                "[Parameter 2:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Enabled:Boolean = def[false] val[true]], \n" +
                "[[Semifinal] Timeout:Integer = multiline[false] def[900] val[500]], \n" +
                "[[Semifinal] Percentage:Boolean = def[true] val[false]], \n" +
                "[[Semifinal] Limit:Integer = multiline[false] def[50] val[70]], \n" +
                "[[Semifinal] Reset board:Boolean = def[true] val[false]], \n" +
                "[[Semifinal] Shuffle board:Boolean = def[true] val[false]], \n" +
                "[[Semifinal] Clear scores:Boolean = def[false] val[false]]]");

        // тут не поменялось ничего, т.к. комната другая
        settings2 = new JSONObject(get("/rest/admin/room/name2/settings/second"));
        assertEqualsSettingsJson(settings2,
                "{'parameters':[{'def':'43','multiline':false,'name':'Parameter 3','options':['43'],'type':'editbox','value':'43','valueType':'Integer'},\n" +
                "{'def':'false','multiline':false,'name':'Parameter 4','options':['false','true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Enabled','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'},\n" +
                "{'def':'900','multiline':false,'name':'[Semifinal] Timeout','options':['900'],'type':'editbox','value':'900','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Percentage','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'50','multiline':false,'name':'[Semifinal] Limit','options':['50'],'type':'editbox','value':'50','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Reset board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Shuffle board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Clear scores','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'}]}");

        // when
        // для другой комнаты пробуем передавать строковые представления
        assertEquals("", post(200, "/rest/admin/room/name2/settings/second",
                unquote("{'parameters':[" +
                        "{'name':'[Semifinal] Enabled','value':'false'}," +
                        "{'name':'[Semifinal] Timeout','value':'300'}," +
                        "{'name':'[Semifinal] Percentage','value':'true'}," +
                        "{'name':'[Semifinal] Limit','value':'60'}," +
                        "{'name':'[Semifinal] Reset board','value':'true'}," +
                        "{'name':'[Semifinal] Shuffle board','value':'true'}," +
                        "{'name':'[Semifinal] Clear scores','value':'false'}" +
                        "]}")));

        // then
        // тут ничего не поменялось
        settings1 = service.getSettings("name1", "first");
        assertEqualsSettingsParameters(settings1,
                "[[Parameter 1:Integer = multiline[false] def[12] val[15]], \n" +
                "[Parameter 2:Boolean = def[true] val[true]], \n" +
                "[[Semifinal] Enabled:Boolean = def[false] val[true]], \n" +
                "[[Semifinal] Timeout:Integer = multiline[false] def[900] val[500]], \n" +
                "[[Semifinal] Percentage:Boolean = def[true] val[false]], \n" +
                "[[Semifinal] Limit:Integer = multiline[false] def[50] val[70]], \n" +
                "[[Semifinal] Reset board:Boolean = def[true] val[false]], \n" +
                "[[Semifinal] Shuffle board:Boolean = def[true] val[false]], \n" +
                "[[Semifinal] Clear scores:Boolean = def[false] val[false]]]");

        // а тут поменялось
        settings2 = new JSONObject(get("/rest/admin/room/name2/settings/second"));
        assertEqualsSettingsJson(settings2,
                "{'parameters':[{'def':'43','multiline':false,'name':'Parameter 3','options':['43'],'type':'editbox','value':'43','valueType':'Integer'},\n" +
                "{'def':'false','multiline':false,'name':'Parameter 4','options':['false','true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Enabled','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'},\n" +
                "{'def':'900','multiline':false,'name':'[Semifinal] Timeout','options':['900','300'],'type':'editbox','value':'300','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Percentage','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'50','multiline':false,'name':'[Semifinal] Limit','options':['50','60'],'type':'editbox','value':'60','valueType':'Integer'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Reset board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'true','multiline':false,'name':'[Semifinal] Shuffle board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'},\n" +
                "{'def':'false','multiline':false,'name':'[Semifinal] Clear scores','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'}]}");
    }
}