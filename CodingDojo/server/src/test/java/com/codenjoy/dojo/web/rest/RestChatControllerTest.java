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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.stream.IntStream;

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CodenjoyContestApplication.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
@Import(RestChatControllerTest.ContextConfiguration.class)
@ContextConfiguration(initializers = AbstractRestControllerTest.PropertyOverrideContextInitializer.class)
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
        assertEquals("[]", quote(get("/rest/chat/validRoom/messages")));

        // when
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // then
        assertEquals("[{'id':1,'playerId':'player','roomId':'validRoom','text':'message1','time':12345}]",
                quote(get("/rest/chat/validRoom/messages")));

        // when
        nowIs(23456L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        // then
        assertEquals("[{'id':1,'playerId':'player','roomId':'validRoom','text':'message1','time':12345}," +
                        "{'id':2,'playerId':'player','roomId':'validRoom','text':'message2','time':23456}]",
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

        assertEquals("[{'id':1,'playerId':'player','roomId':'validRoom','text':'message1','time':12345}," +
                        "{'id':2,'playerId':'player','roomId':'validRoom','text':'message2','time':23456}," +
                        "{'id':3,'playerId':'player','roomId':'validRoom','text':'message3','time':34567}]",
                quote(get("/rest/chat/validRoom/messages")));

        // when
        delete("/rest/chat/validRoom/messages/1");

        // then
        assertEquals("[{'id':2,'playerId':'player','roomId':'validRoom','text':'message2','time':23456}," +
                "{'id':3,'playerId':'player','roomId':'validRoom','text':'message3','time':34567}]",
                quote(get("/rest/chat/validRoom/messages")));

        // when
        delete("/rest/chat/validRoom/messages/3");

        // then
        assertEquals("[{'id':2,'playerId':'player','roomId':'validRoom','text':'message2','time':23456}]",
                quote(get("/rest/chat/validRoom/messages")));

        // when
        delete("/rest/chat/validRoom/messages/2");

        // then
        assertEquals("[]", quote(get("/rest/chat/validRoom/messages")));
    }

    public void nowIs(long time) {
        when(this.time.now()).thenReturn(time);
    }

    @Test
    public void shouldGetMessage_whenPostIt() {
        // given
        assertError("java.lang.IllegalArgumentException: There is no message " +
                        "with id: 1 in room with id: validRoom",
            "/rest/chat/validRoom/messages/1");

        // when
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // then
        assertEquals("{'id':1,'playerId':'player','roomId':'validRoom','text':'message1','time':12345}",
                quote(get("/rest/chat/validRoom/messages/1")));

        // when
        nowIs(23456L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        // then
        assertEquals("{'id':2,'playerId':'player','roomId':'validRoom','text':'message2','time':23456}",
                quote(get("/rest/chat/validRoom/messages/2")));
    }

    @Test
    public void shouldGetAllMessages_betweenBeforeAndAfter() {
        // given
        // id = 0
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // id = 1
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        // id = 2
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message3'}"));

        // id = 3
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message4'}"));

        // when then
        // all
        assertEquals("[{'id':1,'playerId':'player','roomId':'validRoom','text':'message1','time':12345}," +
                        "{'id':2,'playerId':'player','roomId':'validRoom','text':'message2','time':12345}," +
                        "{'id':3,'playerId':'player','roomId':'validRoom','text':'message3','time':12345}," +
                        "{'id':4,'playerId':'player','roomId':'validRoom','text':'message4','time':12345}]",
                quote(get("/rest/chat/validRoom/messages")));

        // when then
        // all + count
        assertEquals("[{'id':1,'playerId':'player','roomId':'validRoom','text':'message1','time':12345}," +
                        "{'id':2,'playerId':'player','roomId':'validRoom','text':'message2','time':12345}]",
                quote(get("/rest/chat/validRoom/messages?count=2")));

        // when then
        // between + count (ignored)
        assertEquals("[{'id':2,'playerId':'player','roomId':'validRoom','text':'message2','time':12345}," +
                        "{'id':3,'playerId':'player','roomId':'validRoom','text':'message3','time':12345}]",
                quote(get("/rest/chat/validRoom/messages?afterId=1&beforeId=4&count=1")));

        // when then
        // between
        assertEquals("[{'id':2,'playerId':'player','roomId':'validRoom','text':'message2','time':12345}," +
                        "{'id':3,'playerId':'player','roomId':'validRoom','text':'message3','time':12345}]",
                quote(get("/rest/chat/validRoom/messages?afterId=1&beforeId=4")));

        assertEquals("[]",
                quote(get("/rest/chat/validRoom/messages?afterId=2&beforeId=3")));

        assertEquals("[]",
                quote(get("/rest/chat/validRoom/messages?afterId=3&beforeId=3")));

        // when then
        // after + count
        assertEquals("[{'id':2,'playerId':'player','roomId':'validRoom','text':'message2','time':12345}," +
                        "{'id':3,'playerId':'player','roomId':'validRoom','text':'message3','time':12345}]",
                quote(get("/rest/chat/validRoom/messages?afterId=1&count=2")));

        // when then
        // after
        assertEquals("[{'id':2,'playerId':'player','roomId':'validRoom','text':'message2','time':12345}," +
                        "{'id':3,'playerId':'player','roomId':'validRoom','text':'message3','time':12345}," +
                        "{'id':4,'playerId':'player','roomId':'validRoom','text':'message4','time':12345}]",
                quote(get("/rest/chat/validRoom/messages?afterId=1")));

        assertEquals("[{'id':3,'playerId':'player','roomId':'validRoom','text':'message3','time':12345}," +
                        "{'id':4,'playerId':'player','roomId':'validRoom','text':'message4','time':12345}]",
                quote(get("/rest/chat/validRoom/messages?afterId=2")));

        assertEquals("[]",
                quote(get("/rest/chat/validRoom/messages?afterId=4")));

        // when then
        // before + count
        assertEquals("[{'id':1,'playerId':'player','roomId':'validRoom','text':'message1','time':12345}," +
                        "{'id':2,'playerId':'player','roomId':'validRoom','text':'message2','time':12345}]",
                quote(get("/rest/chat/validRoom/messages?beforeId=4&count=2")));

        // when then
        // before
        assertEquals("[]",
                quote(get("/rest/chat/validRoom/messages?beforeId=0")));

        assertEquals("[{'id':1,'playerId':'player','roomId':'validRoom','text':'message1','time':12345}]",
                quote(get("/rest/chat/validRoom/messages?beforeId=2")));

        assertEquals("[{'id':1,'playerId':'player','roomId':'validRoom','text':'message1','time':12345}," +
                        "{'id':2,'playerId':'player','roomId':'validRoom','text':'message2','time':12345}," +
                        "{'id':3,'playerId':'player','roomId':'validRoom','text':'message3','time':12345}]",
                quote(get("/rest/chat/validRoom/messages?beforeId=4")));
    }

    @Test
    public void shouldGetAllMessages_countByDefault() {
        // given
        int startInclusive = 1;
        int endExclusive = 21;
        IntStream.range(startInclusive, endExclusive).forEach(index -> {
            nowIs(12345L + index);
            post(200, "/rest/chat/validRoom/messages",
                    unquote("{text:'message" + index + "'}"));
        });


        // when then
        assertEquals("[{'id':11,'playerId':'player','roomId':'validRoom','text':'message11','time':12356}," +
                        "{'id':12,'playerId':'player','roomId':'validRoom','text':'message12','time':12357}," +
                        "{'id':13,'playerId':'player','roomId':'validRoom','text':'message13','time':12358}," +
                        "{'id':14,'playerId':'player','roomId':'validRoom','text':'message14','time':12359}," +
                        "{'id':15,'playerId':'player','roomId':'validRoom','text':'message15','time':12360}," +
                        "{'id':16,'playerId':'player','roomId':'validRoom','text':'message16','time':12361}," +
                        "{'id':17,'playerId':'player','roomId':'validRoom','text':'message17','time':12362}," +
                        "{'id':18,'playerId':'player','roomId':'validRoom','text':'message18','time':12363}," +
                        "{'id':19,'playerId':'player','roomId':'validRoom','text':'message19','time':12364}," +
                        "{'id':20,'playerId':'player','roomId':'validRoom','text':'message20','time':12365}]",
                quote(get("/rest/chat/validRoom/messages")));

        assertEquals("[{'id':10,'playerId':'player','roomId':'validRoom','text':'message10','time':12355}," +
                        "{'id':11,'playerId':'player','roomId':'validRoom','text':'message11','time':12356}," +
                        "{'id':12,'playerId':'player','roomId':'validRoom','text':'message12','time':12357}," +
                        "{'id':13,'playerId':'player','roomId':'validRoom','text':'message13','time':12358}," +
                        "{'id':14,'playerId':'player','roomId':'validRoom','text':'message14','time':12359}," +
                        "{'id':15,'playerId':'player','roomId':'validRoom','text':'message15','time':12360}," +
                        "{'id':16,'playerId':'player','roomId':'validRoom','text':'message16','time':12361}," +
                        "{'id':17,'playerId':'player','roomId':'validRoom','text':'message17','time':12362}," +
                        "{'id':18,'playerId':'player','roomId':'validRoom','text':'message18','time':12363}," +
                        "{'id':19,'playerId':'player','roomId':'validRoom','text':'message19','time':12364}]",
                quote(get("/rest/chat/validRoom/messages?beforeId=20")));

        assertEquals("[{'id':2,'playerId':'player','roomId':'validRoom','text':'message2','time':12347}," +
                        "{'id':3,'playerId':'player','roomId':'validRoom','text':'message3','time':12348}," +
                        "{'id':4,'playerId':'player','roomId':'validRoom','text':'message4','time':12349}," +
                        "{'id':5,'playerId':'player','roomId':'validRoom','text':'message5','time':12350}," +
                        "{'id':6,'playerId':'player','roomId':'validRoom','text':'message6','time':12351}," +
                        "{'id':7,'playerId':'player','roomId':'validRoom','text':'message7','time':12352}," +
                        "{'id':8,'playerId':'player','roomId':'validRoom','text':'message8','time':12353}," +
                        "{'id':9,'playerId':'player','roomId':'validRoom','text':'message9','time':12354}," +
                        "{'id':10,'playerId':'player','roomId':'validRoom','text':'message10','time':12355}," +
                        "{'id':11,'playerId':'player','roomId':'validRoom','text':'message11','time':12356}]",
                quote(get("/rest/chat/validRoom/messages?afterId=1")));

        assertEquals("[{'id':2,'playerId':'player','roomId':'validRoom','text':'message2','time':12347}," +
                        "{'id':3,'playerId':'player','roomId':'validRoom','text':'message3','time':12348}," +
                        "{'id':4,'playerId':'player','roomId':'validRoom','text':'message4','time':12349}," +
                        "{'id':5,'playerId':'player','roomId':'validRoom','text':'message5','time':12350}," +
                        "{'id':6,'playerId':'player','roomId':'validRoom','text':'message6','time':12351}," +
                        "{'id':7,'playerId':'player','roomId':'validRoom','text':'message7','time':12352}," +
                        "{'id':8,'playerId':'player','roomId':'validRoom','text':'message8','time':12353}," +
                        "{'id':9,'playerId':'player','roomId':'validRoom','text':'message9','time':12354}," +
                        "{'id':10,'playerId':'player','roomId':'validRoom','text':'message10','time':12355}," +
                        "{'id':11,'playerId':'player','roomId':'validRoom','text':'message11','time':12356}," +
                        "{'id':12,'playerId':'player','roomId':'validRoom','text':'message12','time':12357}," +
                        "{'id':13,'playerId':'player','roomId':'validRoom','text':'message13','time':12358}," +
                        "{'id':14,'playerId':'player','roomId':'validRoom','text':'message14','time':12359}," +
                        "{'id':15,'playerId':'player','roomId':'validRoom','text':'message15','time':12360}," +
                        "{'id':16,'playerId':'player','roomId':'validRoom','text':'message16','time':12361}," +
                        "{'id':17,'playerId':'player','roomId':'validRoom','text':'message17','time':12362}," +
                        "{'id':18,'playerId':'player','roomId':'validRoom','text':'message18','time':12363}," +
                        "{'id':19,'playerId':'player','roomId':'validRoom','text':'message19','time':12364}]",
                quote(get("/rest/chat/validRoom/messages?afterId=1&beforeId=20")));
    }
}
