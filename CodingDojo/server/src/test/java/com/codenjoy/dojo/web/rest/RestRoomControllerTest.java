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
import com.codenjoy.dojo.services.GameServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;

@SpringBootTest(classes = CodenjoyContestApplication.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
@Import(RestRoomControllerTest.ContextConfiguration.class)
@ContextConfiguration(initializers = AbstractRestControllerTest.PropertyOverrideContextInitializer.class)
@WebAppConfiguration
public class RestRoomControllerTest extends AbstractRestControllerTest {

    @TestConfiguration
    public static class ContextConfiguration {
        @Bean("gameService")
        public GameServiceImpl gameService() {
            return AbstractRestControllerTest.gameService();
        }
    }

    @Autowired
    private RestRoomController service;

    @Before
    public void setUp() {
        super.setUp();

        playerService.removeAll();
        registration.removeAll();
        roomService.removeAll();
    }

    // проверяем что для залогиненого пользователя все методы сервиса отрабатывают
    @Test
    public void shouldJoinJoinedAndLeave_whenAuthenticated() {
        // given
        register("validPlayer", "ip", "validRoom", "first");
        asUser("validPlayer", "validPlayer");

        // then
        assertEquals("true", get("/rest/room/validRoom/joined"));
        assertEquals("true", get("/rest/room/validRoom/player/validPlayer/joined"));

        // when
        assertEquals("true", get("/rest/room/validRoom/leave"));

        // then
        assertEquals("false", get("/rest/room/validRoom/joined"));
        assertEquals("false", get("/rest/room/validRoom/player/validPlayer/joined"));

        // when
        assertEquals("{'code':'4020021687627278468','id':'validPlayer'}",
                quote(get("/rest/room/validRoom/game/first/join")));

        // then
        assertEquals("true", get("/rest/room/validRoom/joined"));
        assertEquals("true", get("/rest/room/validRoom/player/validPlayer/joined"));
    }


    // проверяем что если закрыта регистрация в комнате этой, то зарегаться не получится
    @Test
    public void shouldJoinJoinedAndLeave_whenRegistrationIsClosed() {
        // given
        register("validPlayer", "ip", "validRoom", "first");
        asUser("validPlayer", "validPlayer");

        // then
        assertEquals("true", get("/rest/room/validRoom/joined"));
        assertEquals("true", get("/rest/room/validRoom/player/validPlayer/joined"));

        // when
        roomService.setOpened("validRoom", false);
        assertEquals("true", get("/rest/room/validRoom/leave"));

        // then
        assertEquals("false", get("/rest/room/validRoom/joined"));
        assertEquals("false", get("/rest/room/validRoom/player/validPlayer/joined"));

        // then
        // не получается зайти
        assertEquals("", get("/rest/room/validRoom/game/first/join"));

        // then
        assertEquals("false", get("/rest/room/validRoom/joined"));
        assertEquals("false", get("/rest/room/validRoom/player/validPlayer/joined"));

        // when
        roomService.setOpened("validRoom", true);

        // then
        // могу зайти
        assertEquals("{'code':'4020021687627278468','id':'validPlayer'}",
                quote(get("/rest/room/validRoom/game/first/join")));

        assertEquals("true", get("/rest/room/validRoom/joined"));
        assertEquals("true", get("/rest/room/validRoom/player/validPlayer/joined"));
    }

    // проверяем что если закрыта регистрация в комнате этой, то могу зайти в другую, новую
    @Test
    public void shouldJoinJoinedAndLeave_whenRegistrationIsClosed_caseGoToAnotherRoom() {
        // given
        register("validPlayer", "ip", "validRoom", "first");
        asUser("validPlayer", "validPlayer");

        // then
        assertEquals("true", get("/rest/room/validRoom/joined"));
        assertEquals("true", get("/rest/room/validRoom/player/validPlayer/joined"));

        // when
        roomService.setOpened("validRoom", false);
        assertEquals("true", get("/rest/room/validRoom/leave"));

        // then
        assertEquals("false", get("/rest/room/validRoom/joined"));
        assertEquals("false", get("/rest/room/validRoom/player/validPlayer/joined"));

        // then
        // не получается зайти
        assertEquals("{'code':'4020021687627278468','id':'validPlayer'}",
                quote(get("/rest/room/anotherNewRoom/game/first/join")));

        // then
        assertEquals("false", get("/rest/room/validRoom/joined"));
        assertEquals("false", get("/rest/room/validRoom/player/validPlayer/joined"));

        assertEquals("true", get("/rest/room/anotherNewRoom/joined"));
        assertEquals("true", get("/rest/room/anotherNewRoom/player/validPlayer/joined"));
    }

    // в этом тесте все методы отвечают либо false либо null, а все потому что
    // они ожидают что юбзер будет залогинен в системе
    // исключение /rest/room/ROOM/player/PLAYER/joined
    // ему не требуется быть залогиненым
    @Test
    public void shouldJoinJoinedAndLeave_whenNotAuthenticated() {
        // given
        register("validPlayer", "ip", "validRoom", "first");
        asNone();

        // then
        assertEquals("false", get("/rest/room/validRoom/joined"));
        assertEquals("true", get("/rest/room/validRoom/player/validPlayer/joined"));

        // when
        assertEquals("false", get("/rest/room/validRoom/leave"));

        // then
        assertEquals("false", get("/rest/room/validRoom/joined"));
        assertEquals("true", get("/rest/room/validRoom/player/validPlayer/joined"));

        // when
        assertEquals("", get("/rest/room/validRoom/game/first/join"));

        // then
        assertEquals("false", get("/rest/room/validRoom/joined"));
        assertEquals("true", get("/rest/room/validRoom/player/validPlayer/joined"));
    }

    // проверяем на несуществующие юзеры или комнаты
    @Test
    public void shouldJoinJoinedAndLeave_whenUserOrRoomNotFound() {
        // given
        register("validPlayer", "ip", "validRoom", "first");
        asUser("validPlayer", "validPlayer");

        // then
        assertEquals("false", get("/rest/room/badRoom/joined"));
        assertEquals("false", get("/rest/room/badRoom/player/badPlayer/joined"));

        // when
        assertEquals("false", get("/rest/room/badRoom/leave"));

        // then
        assertEquals("false", get("/rest/room/badRoom/joined"));
        assertEquals("false", get("/rest/room/badRoom/player/badPlayer/joined"));

        // when
        // все же зашли, комната может быть любой
        assertEquals("{'code':'4020021687627278468','id':'validPlayer'}",
                quote(get("/rest/room/badRoom/game/first/join")));

        // then
        assertEquals("true", get("/rest/room/badRoom/player/validPlayer/joined"));
        assertEquals("true", get("/rest/room/badRoom/joined"));
        assertEquals("false", get("/rest/room/badRoom/player/badPlayer/joined"));
    }

    // проверяем валидацию room name
    @Test
    public void shouldJoinJoinedAndLeave_whenRoomNameIsInvalid() {
        // given
        register("validPlayer", "ip", "validRoom", "first");
        asUser("validPlayer", "validPlayer");

        // when then
        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/room/$bad$/joined");

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/room/$bad$/player/validPlayer/joined");

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/room/$bad$/leave");

        assertError("java.lang.IllegalArgumentException: Room name is invalid: '$bad$'",
                "/rest/room/$bad$/player/validPlayer/joined");
    }

    // проверяем валидацию player name
    @Test
    public void shouldJoined_whenPlayerNameIsInvalid() {
        // given
        register("validPlayer", "ip", "validRoom", "first");
        asUser("validPlayer", "validPlayer");

        // when then
        assertError("java.lang.IllegalArgumentException: Player id is invalid: '$bad$'",
                "/rest/room/validRoom/player/$bad$/joined");
    }

}
