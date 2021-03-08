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
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.TimeService;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.room.RoomService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CodenjoyContestApplication.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
@Import(RestChatControllerTest.ContextConfiguration.class)
@WebAppConfiguration
public class RestChatControllerTest extends AbstractRestControllerTest {

    @TestConfiguration
    public static class ContextConfiguration {
        @Bean("gameService")
        public GameServiceImpl gameService() {
            return AbstractRestControllerTest.gameService();
        }
    }

    @Autowired
    private Chat chat;

    @Autowired
    private RoomService roomService;

    @Autowired
    private PlayerService playerService;

    @SpyBean
    private TimeService time;

    @Before
    public void setUp() {
        super.setUp();

        chat.removeAll();
        playerService.removeAll();
        roomService.removeAll();

        register("player", "ip", "validRoom", "first");
        asUser("player", "player");
    }

    @Test
    public void shouldGetAllMessages_whenPostIt() {
        // given
        assertEquals("[]", get("/rest/chat/validRoom/messages"));

        // when
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // then
        assertEquals("[{'id':0,'playerId':'player','roomId':'validRoom','text':'message1','time':12345}]",
                quote(get("/rest/chat/validRoom/messages")));

        // when
        nowIs(23456L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        // then
        assertEquals("[{'id':0,'playerId':'player','roomId':'validRoom','text':'message1','time':12345}," +
                        "{'id':1,'playerId':'player','roomId':'validRoom','text':'message2','time':23456}]",
                quote(get("/rest/chat/validRoom/messages")));
    }

    @Test
    public void shouldDeleteMessages() {
        // given
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        nowIs(23456L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        nowIs(34567L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message3'}"));

        assertEquals("[{'id':0,'playerId':'player','roomId':'validRoom','text':'message1','time':12345}," +
                        "{'id':1,'playerId':'player','roomId':'validRoom','text':'message2','time':23456}," +
                        "{'id':2,'playerId':'player','roomId':'validRoom','text':'message3','time':34567}]",
                quote(get("/rest/chat/validRoom/messages")));

        // when
        delete("/rest/chat/validRoom/messages/0");

        // then
        assertEquals("[{'id':1,'playerId':'player','roomId':'validRoom','text':'message2','time':23456}," +
                "{'id':2,'playerId':'player','roomId':'validRoom','text':'message3','time':34567}]",
                quote(get("/rest/chat/validRoom/messages")));

        // when
        delete("/rest/chat/validRoom/messages/2");

        // then
        assertEquals("[{'id':1,'playerId':'player','roomId':'validRoom','text':'message2','time':23456}]",
                quote(get("/rest/chat/validRoom/messages")));

        // when
        delete("/rest/chat/validRoom/messages/1");

        // then
        assertEquals("[]", quote(get("/rest/chat/validRoom/messages")));
    }

    public void nowIs(long time) {
        when(this.time.now()).thenReturn(time);
    }
}
