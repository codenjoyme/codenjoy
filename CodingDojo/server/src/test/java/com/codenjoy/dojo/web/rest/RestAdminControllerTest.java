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

import com.codenjoy.dojo.CodenjoyContestApplication;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.mocks.FirstSemifinalGameType;
import com.codenjoy.dojo.services.mocks.SecondSemifinalGameType;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.semifinal.SemifinalSettings;
import com.codenjoy.dojo.services.semifinal.SemifinalSettingsImpl;
import com.codenjoy.dojo.web.rest.pojo.PParameters;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CodenjoyContestApplication.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
@Import(RestAdminControllerTest.ContextConfiguration.class)
@ContextConfiguration(initializers = AbstractRestControllerTest.PropertyOverrideContextInitializer.class)
@WebAppConfiguration
public class RestAdminControllerTest extends AbstractRestControllerTest {

    private static Map<String, SemifinalSettings> saved;

    @TestConfiguration
    public static class ContextConfiguration {
        @Bean("gameService")
        public GameServiceImpl gameService() {
            return new GameServiceImpl(){
                @Override
                public Collection<? extends Class<? extends GameType>> findInPackage(String packageName) {
                    return Arrays.asList(FirstSemifinalGameType.class, SecondSemifinalGameType.class);
                }
            };

        }
    }

    @Autowired
    private RestAdminController service;

    @Autowired
    private PlayerGamesView playerGamesView;

    @Autowired
    private SaveService saveService;

    @Autowired
    private RoomService roomService;

    @Before
    public void setUp() {
        super.setUp();

        registration.removeAll();

        asAdmin();

        playerService.removeAll();
        saveService.removeAllSaves();
        roomService.removeAll(); // тут чистятся все сеттинги
    }

    @Test
    public void shouldStartStopGame_oneRoom() {
        // given
        assertEquals(false, service.isRoomActive("name"));
        assertEquals("false", get("/rest/admin/room/name/pause"));

        register("player", "ip", "name", "first");

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
                playerGames.active().stream()
                    .map(pg -> pg.getPlayer().getId() + "->" + pg.getRoom())
                    .collect(toList())
                    .toString());
    }

    @Test
    public void shouldStartStopGame_severalRooms() {
        // given
        assertEquals(false, service.isRoomActive("name1"));
        assertEquals("false", get("/rest/admin/room/name2/pause"));

        register("player1", "ip1", "name1", "first");
        register("player2", "ip2", "name1", "first");
        register("player3", "ip3", "name2", "second");

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

        register("player1", "ip1", "name", "first");
        assertPlayersInActiveRooms("[player1->name]");

        // а теперь она есть, т.к. зашел первый пользователь
        // и по умолчанию регистрация так же открыта
        assertEquals(true, service.isRoomRegistrationOpened("name"));
        assertEquals("true", get("/rest/admin/room/name/registration/open"));

        assertEquals(true, playerService.isRegistrationOpened("name"));

        // when
        service.setRoomRegistrationOpened("name", false);

        // then
        assertEquals(false, service.isRoomRegistrationOpened("name"));
        assertEquals("false", get("/rest/admin/room/name/registration/open"));

        assertEquals(false, playerService.isRegistrationOpened("name"));

        // when
        register("player2", "ip2", "name", "first"); // not created

        // then
        assertPlayersInActiveRooms("[player1->name]");

        // when
        assertEquals("", get("/rest/admin/room/name/registration/open/true"));

        // then
        assertEquals(true, service.isRoomRegistrationOpened("name"));
        assertEquals("true", get("/rest/admin/room/name/registration/open"));

        assertEquals(true, playerService.isRegistrationOpened("name"));

        // when
        register("player2", "ip2", "name", "first");

        // then
        assertPlayersInActiveRooms("[player1->name, player2->name]");
    }

    @Test
    public void shouldOpenCloseRoomRegistration_severalRooms() {
        // given
        // комнаты нет, но в нее можно зарегаться
        assertEquals(true, service.isRoomRegistrationOpened("name1"));
        assertEquals("true", get("/rest/admin/room/name2/registration/open"));

        register("player1", "ip1", "name1", "first");
        register("player2", "ip2", "name1", "first");
        register("player3", "ip3", "name2", "second");
        assertPlayersInActiveRooms("[player1->name1, player2->name1, player3->name2]");

        // а теперь она есть, т.к. зашел первый пользователь
        // и по умолчанию регистрация так же открыта
        assertEquals(true, service.isRoomRegistrationOpened("name1"));
        assertEquals("true", get("/rest/admin/room/name2/registration/open"));

        assertEquals(true, playerService.isRegistrationOpened("name1"));
        assertEquals(true, playerService.isRegistrationOpened("name2"));

        // when
        service.setRoomRegistrationOpened("name1", false);

        // then
        assertEquals(false, service.isRoomRegistrationOpened("name1"));
        assertEquals("true", get("/rest/admin/room/name2/registration/open"));

        assertEquals(false, playerService.isRegistrationOpened("name1"));
        assertEquals(true, playerService.isRegistrationOpened("name2"));

        // when
        register("player4", "ip4", "name1", "first"); // not created
        register("player5", "ip5", "name2", "second");

        // then
        assertPlayersInActiveRooms("[player1->name1, player2->name1, player3->name2, " +
                "player5->name2]");

        // when
        assertEquals("", get("/rest/admin/room/name1/registration/open/true"));

        // then
        assertEquals(true, service.isRoomRegistrationOpened("name1"));
        assertEquals("true", get("/rest/admin/room/name2/registration/open"));

        assertEquals(true, playerService.isRegistrationOpened("name1"));
        assertEquals(true, playerService.isRegistrationOpened("name2"));

        // when
        register("player6", "ip6", "name1", "first");
        register("player7", "ip7", "name2", "second");

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
        register("player1", "ip1", "room1", "first");
        register("player2", "ip2", "room1", "first");

        register("player3", "ip3", "room2", "first");

        register("player4", "ip4", "room3", "second");

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
        assertEquals(expected, playerGamesView.getScores().toString());
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
        PlayerGame playerGame1 = register("player1", "ip1", "room1", "first");
        PlayerGame playerGame2 = register("player2", "ip2", "room1", "first");

        PlayerGame playerGame3 = register("player3", "ip3", "room2", "first");

        PlayerGame playerGame4 = register("player4", "ip4", "room3", "second");

        // when
        assertEquals("", get("/rest/admin/room/room1/player/reload"));

        // then
        verifyNewGame(playerGame1, atLeastOnce());
        verifyNewGame(playerGame2, atLeastOnce());
        verifyNewGame(playerGame3, never());
        verifyNewGame(playerGame4, never());
    }

    @Test
    public void shouldReloadAllPlayersInRoom_validation() {
        assertException("Room name is invalid: '$bad$'",
                () -> service.reloadPlayers("$bad$"));

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/admin/room/$bad$/player/reload");
    }

    @Test
    public void shouldGameOverInRoom() {
        // given
        register("player1", "ip1", "room1", "first");
        register("player2", "ip2", "room1", "first");
        register("player3", "ip3", "room1", "first");

        register("player4", "ip4", "room2", "first");
        register("player5", "ip5", "room2", "first");

        register("player6", "ip6", "room3", "second");

        register("player7", "ip7", "room4", "second");

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

    private void assertRooms(String expected) {
        assertEquals(expected, playerGamesView.getGroupsByRooms().toString());
    }

    @Test
    public void shouldGameOverInRoom_validation() {
        // given
        register("validPlayer", "ip", "validRoom", "first");

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

    private void verifyNewGame(PlayerGame playerGame, VerificationMode mode) {
        verify(playerGame.getField(), mode).newGame(playerGame.getGame().getPlayer());
    }

    @Test
    public void shouldSaveAllAndLoad() {
        // given
        register("player1", "ip1", "room1", "first");
        register("player2", "ip2", "room1", "first");

        register("player3", "ip3", "room2", "first");

        register("player4", "ip4", "room3", "second");

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
        service.load("room1", "player1"); // save exists
        // service.load("room1", "player2"); // do not load
        assertEquals("", get("/rest/admin/room/room2/load/player3")); // save exists
        assertEquals("", get("/rest/admin/room/room3/load/player4")); // save not exists

        // then
        assertScores("{player1=10, player2=0, player3=30, player4=0}");

        // when
        service.gameOverAll("room1");
        service.gameOverAll("room2");
        service.gameOverAll("room3");

        // then
        assertScores("{}");

        service.load("room1", "player1"); // save exists
        // service.load("room1", "player2"); // do not load
        service.load("room2", "player3"); // save exists
        service.load("room3", "player4"); // save not exists

        // then
        assertScores("{player1=10, player3=30}");
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
    public void shouldGetSetSettings() {
        PParameters settings1 = service.getSettings("name1", "first");
        assertEquals("[[Parameter 1:Integer = multiline[false] def[12] val[15]], " +
                        "[Parameter 2:Boolean = def[true] val[true]], " +
                        "[[Semifinal] Enabled:Boolean = def[false] val[false]], " +
                        "[[Semifinal] Timeout:Integer = multiline[false] def[900] val[900]], " +
                        "[[Semifinal] Percentage:Boolean = def[true] val[true]], " +
                        "[[Semifinal] Limit:Integer = multiline[false] def[50] val[50]], " +
                        "[[Semifinal] Reset board:Boolean = def[true] val[true]], " +
                        "[[Semifinal] Shuffle board:Boolean = def[true] val[true]]]",
                settings1.build().toString());

        JSONObject settings2 = new JSONObject(get("/rest/admin/room/name2/settings/second"));
        assertEquals("{'parameters':[" +
                        "{'def':'43','multiline':false,'name':'Parameter 3','options':['43'],'type':'editbox','value':'43','valueType':'Integer'}," +
                        "{'def':'false','multiline':false,'name':'Parameter 4','options':['false','true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'false','multiline':false,'name':'[Semifinal] Enabled','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'}," +
                        "{'def':'900','multiline':false,'name':'[Semifinal] Timeout','options':['900'],'type':'editbox','value':'900','valueType':'Integer'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Percentage','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'50','multiline':false,'name':'[Semifinal] Limit','options':['50'],'type':'editbox','value':'50','valueType':'Integer'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Reset board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Shuffle board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}]}",
                quote(settings2.toString()));

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
        assertEquals("[[Parameter 1:Integer = multiline[false] def[12] val[30]], " +
                        "[Parameter 2:Boolean = def[true] val[false]], " +
                        "[[Semifinal] Enabled:Boolean = def[false] val[false]], " +
                        "[[Semifinal] Timeout:Integer = multiline[false] def[900] val[900]], " +
                        "[[Semifinal] Percentage:Boolean = def[true] val[true]], " +
                        "[[Semifinal] Limit:Integer = multiline[false] def[50] val[50]], " +
                        "[[Semifinal] Reset board:Boolean = def[true] val[true]], " +
                        "[[Semifinal] Shuffle board:Boolean = def[true] val[true]]]",
                settings1.build().toString());

        settings2 = new JSONObject(get("/rest/admin/room/name2/settings/second"));
        assertEquals("{'parameters':[" +
                        "{'def':'43','multiline':false,'name':'Parameter 3','options':['43','55'],'type':'editbox','value':'55','valueType':'Integer'}," +
                        "{'def':'false','multiline':false,'name':'Parameter 4','options':['false','true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'false','multiline':false,'name':'[Semifinal] Enabled','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'}," +
                        "{'def':'900','multiline':false,'name':'[Semifinal] Timeout','options':['900'],'type':'editbox','value':'900','valueType':'Integer'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Percentage','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'50','multiline':false,'name':'[Semifinal] Limit','options':['50'],'type':'editbox','value':'50','valueType':'Integer'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Reset board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Shuffle board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}]}",
                quote(settings2.toString()));
    }

    // тест такой же как и прошлый, только мы апдейтим не весь сеттинг каким он пришел,
    // а только name/value этого должно быть достаточно
    @Test
    public void shouldSetSettings_onlyKeyValue() {
        PParameters settings1 = service.getSettings("name1", "first");
        assertEquals("[[Parameter 1:Integer = multiline[false] def[12] val[15]], " +
                        "[Parameter 2:Boolean = def[true] val[true]], " +
                        "[[Semifinal] Enabled:Boolean = def[false] val[false]], " +
                        "[[Semifinal] Timeout:Integer = multiline[false] def[900] val[900]], " +
                        "[[Semifinal] Percentage:Boolean = def[true] val[true]], " +
                        "[[Semifinal] Limit:Integer = multiline[false] def[50] val[50]], " +
                        "[[Semifinal] Reset board:Boolean = def[true] val[true]], " +
                        "[[Semifinal] Shuffle board:Boolean = def[true] val[true]]]",
                settings1.build().toString());

        JSONObject settings2 = new JSONObject(get("/rest/admin/room/name2/settings/second"));
        assertEquals("{'parameters':[" +
                        "{'def':'43','multiline':false,'name':'Parameter 3','options':['43'],'type':'editbox','value':'43','valueType':'Integer'}," +
                        "{'def':'false','multiline':false,'name':'Parameter 4','options':['false','true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'false','multiline':false,'name':'[Semifinal] Enabled','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'}," +
                        "{'def':'900','multiline':false,'name':'[Semifinal] Timeout','options':['900'],'type':'editbox','value':'900','valueType':'Integer'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Percentage','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'50','multiline':false,'name':'[Semifinal] Limit','options':['50'],'type':'editbox','value':'50','valueType':'Integer'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Reset board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Shuffle board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}]}",
                quote(settings2.toString()));

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
        assertEquals("[[Parameter 1:Integer = multiline[false] def[12] val[30]], " +
                        "[Parameter 2:Boolean = def[true] val[false]], " +
                        "[[Semifinal] Enabled:Boolean = def[false] val[false]], " +
                        "[[Semifinal] Timeout:Integer = multiline[false] def[900] val[900]], " +
                        "[[Semifinal] Percentage:Boolean = def[true] val[true]], " +
                        "[[Semifinal] Limit:Integer = multiline[false] def[50] val[50]], " +
                        "[[Semifinal] Reset board:Boolean = def[true] val[true]], " +
                        "[[Semifinal] Shuffle board:Boolean = def[true] val[true]]]",
                settings1.build().toString());

        settings2 = new JSONObject(get("/rest/admin/room/name2/settings/second"));
        assertEquals("{'parameters':[" +
                        "{'def':'43','multiline':false,'name':'Parameter 3','options':['43','55'],'type':'editbox','value':'55','valueType':'Integer'}," +
                        "{'def':'false','multiline':false,'name':'Parameter 4','options':['false','true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'false','multiline':false,'name':'[Semifinal] Enabled','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'}," +
                        "{'def':'900','multiline':false,'name':'[Semifinal] Timeout','options':['900'],'type':'editbox','value':'900','valueType':'Integer'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Percentage','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'50','multiline':false,'name':'[Semifinal] Limit','options':['50'],'type':'editbox','value':'50','valueType':'Integer'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Reset board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Shuffle board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}]}",
                quote(settings2.toString()));
    }

    // тест такой же как и прошлый, только мы проверяем группу настроек semifinal
    @Test
    public void shouldSetSettings_onlyKeyValue_semifinal() {
        // given
        PParameters settings1 = service.getSettings("name1", "first");
        assertEquals("[[Parameter 1:Integer = multiline[false] def[12] val[15]], " +
                        "[Parameter 2:Boolean = def[true] val[true]], " +
                        "[[Semifinal] Enabled:Boolean = def[false] val[false]], " +
                        "[[Semifinal] Timeout:Integer = multiline[false] def[900] val[900]], " +
                        "[[Semifinal] Percentage:Boolean = def[true] val[true]], " +
                        "[[Semifinal] Limit:Integer = multiline[false] def[50] val[50]], " +
                        "[[Semifinal] Reset board:Boolean = def[true] val[true]], " +
                        "[[Semifinal] Shuffle board:Boolean = def[true] val[true]]]",
                settings1.build().toString());

        JSONObject settings2 = new JSONObject(get("/rest/admin/room/name2/settings/second"));
        assertEquals("{'parameters':[" +
                        "{'def':'43','multiline':false,'name':'Parameter 3','options':['43'],'type':'editbox','value':'43','valueType':'Integer'}," +
                        "{'def':'false','multiline':false,'name':'Parameter 4','options':['false','true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'false','multiline':false,'name':'[Semifinal] Enabled','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'}," +
                        "{'def':'900','multiline':false,'name':'[Semifinal] Timeout','options':['900'],'type':'editbox','value':'900','valueType':'Integer'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Percentage','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'50','multiline':false,'name':'[Semifinal] Limit','options':['50'],'type':'editbox','value':'50','valueType':'Integer'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Reset board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Shuffle board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}]}",
                quote(settings2.toString()));

        // when
        // обновляем сеттинги первой комнаты передавая только ключик и значение
        assertEquals("", post(200, "/rest/admin/room/name1/settings/first",
                unquote("{'parameters':[" +
                        "{'name':'[Semifinal] Enabled','value':true}," +
                        "{'name':'[Semifinal] Timeout','value':500}," +
                        "{'name':'[Semifinal] Percentage','value':false}," +
                        "{'name':'[Semifinal] Limit','value':70}," +
                        "{'name':'[Semifinal] Reset board','value':false}," +
                        "{'name':'[Semifinal] Shuffle board','value':false}" +
                        "]}")));

        // then
        // смотрим что поменялось
        settings1 = service.getSettings("name1", "first");
        assertEquals("[[Parameter 1:Integer = multiline[false] def[12] val[15]], " +
                        "[Parameter 2:Boolean = def[true] val[true]], " +
                        "[[Semifinal] Enabled:Boolean = def[false] val[true]], " +
                        "[[Semifinal] Timeout:Integer = multiline[false] def[900] val[500]], " +
                        "[[Semifinal] Percentage:Boolean = def[true] val[false]], " +
                        "[[Semifinal] Limit:Integer = multiline[false] def[50] val[70]], " +
                        "[[Semifinal] Reset board:Boolean = def[true] val[false]], " +
                        "[[Semifinal] Shuffle board:Boolean = def[true] val[false]]]",
                settings1.build().toString());

        // тут не поменялось ничего, т.к. комната другая
        settings2 = new JSONObject(get("/rest/admin/room/name2/settings/second"));
        assertEquals("{'parameters':[" +
        "{'def':'43','multiline':false,'name':'Parameter 3','options':['43'],'type':'editbox','value':'43','valueType':'Integer'}," +
                "{'def':'false','multiline':false,'name':'Parameter 4','options':['false','true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                "{'def':'false','multiline':false,'name':'[Semifinal] Enabled','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'}," +
                "{'def':'900','multiline':false,'name':'[Semifinal] Timeout','options':['900'],'type':'editbox','value':'900','valueType':'Integer'}," +
                "{'def':'true','multiline':false,'name':'[Semifinal] Percentage','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                "{'def':'50','multiline':false,'name':'[Semifinal] Limit','options':['50'],'type':'editbox','value':'50','valueType':'Integer'}," +
                "{'def':'true','multiline':false,'name':'[Semifinal] Reset board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                "{'def':'true','multiline':false,'name':'[Semifinal] Shuffle board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}]}",
                quote(settings2.toString()));

        // when
        // для другой комнаты пробуем передавать строковые представления
        assertEquals("", post(200, "/rest/admin/room/name2/settings/second",
                unquote("{'parameters':[" +
                        "{'name':'[Semifinal] Enabled','value':'false'}," +
                        "{'name':'[Semifinal] Timeout','value':'300'}," +
                        "{'name':'[Semifinal] Percentage','value':'true'}," +
                        "{'name':'[Semifinal] Limit','value':'60'}," +
                        "{'name':'[Semifinal] Reset board','value':'true'}," +
                        "{'name':'[Semifinal] Shuffle board','value':'true'}" +
                        "]}")));

        // then
        // тут ничего не поменялось
        settings1 = service.getSettings("name1", "first");
        assertEquals("[[Parameter 1:Integer = multiline[false] def[12] val[15]], " +
                        "[Parameter 2:Boolean = def[true] val[true]], " +
                        "[[Semifinal] Enabled:Boolean = def[false] val[true]], " +
                        "[[Semifinal] Timeout:Integer = multiline[false] def[900] val[500]], " +
                        "[[Semifinal] Percentage:Boolean = def[true] val[false]], " +
                        "[[Semifinal] Limit:Integer = multiline[false] def[50] val[70]], " +
                        "[[Semifinal] Reset board:Boolean = def[true] val[false]], " +
                        "[[Semifinal] Shuffle board:Boolean = def[true] val[false]]]",
                settings1.build().toString());

        // а тут поменялось
        settings2 = new JSONObject(get("/rest/admin/room/name2/settings/second"));
        assertEquals("{'parameters':[" +
                        "{'def':'43','multiline':false,'name':'Parameter 3','options':['43'],'type':'editbox','value':'43','valueType':'Integer'}," +
                        "{'def':'false','multiline':false,'name':'Parameter 4','options':['false','true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'false','multiline':false,'name':'[Semifinal] Enabled','options':['false'],'type':'checkbox','value':'false','valueType':'Boolean'}," +
                        "{'def':'900','multiline':false,'name':'[Semifinal] Timeout','options':['900','300'],'type':'editbox','value':'300','valueType':'Integer'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Percentage','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'50','multiline':false,'name':'[Semifinal] Limit','options':['50','60'],'type':'editbox','value':'60','valueType':'Integer'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Reset board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}," +
                        "{'def':'true','multiline':false,'name':'[Semifinal] Shuffle board','options':['true'],'type':'checkbox','value':'true','valueType':'Boolean'}]}",
                quote(settings2.toString()));
    }
}