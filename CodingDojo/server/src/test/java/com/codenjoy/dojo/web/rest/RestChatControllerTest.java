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

import static org.junit.Assert.assertEquals;
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
        registration.removeAll();

        register("player", "ip", "validRoom", "first");
        register("player2", "ip", "validRoom", "first");
        register("otherPlayer", "ip", "otherRoom", "first");

        asUser("player", "player");
    }

    @Test
    public void shouldGetAllMessages_whenPostIt() {
        // given
        assertEquals("[]", fix(get("/rest/chat/validRoom/messages")));

        // when
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // then
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message1','time':12345,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages")));

        // when
        nowIs(23456L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        // then
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message1','time':12345,'topicId':null},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':23456,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages")));
    }

    @Test
    public void shouldDeleteMessages() {
        // given
        // id = 1
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // id = 2
        nowIs(23456L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        // id = 3
        nowIs(34567L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message3'}"));

        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message1','time':12345,'topicId':null},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':23456,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':34567,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages")));

        // when
        delete("/rest/chat/validRoom/messages/1");

        // then
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':23456,'topicId':null},\n" +
                "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':34567,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages")));

        // when
        delete("/rest/chat/validRoom/messages/3");

        // then
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':23456,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages")));

        // when
        delete("/rest/chat/validRoom/messages/2");

        // then
        assertEquals("[]", fix(get("/rest/chat/validRoom/messages")));
    }

    @Test
    public void shouldDeleteMessages_cantDeleteWhenNotMyMessage() {
        // given
        // id = 1
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message1','time':12345,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages")));

        // when then
        asUser("player2", "player2");

        assertDeleteError("java.lang.IllegalArgumentException: " +
                        "Player 'player2' cant delete message with id " +
                        "'1' in room 'validRoom'",
                "/rest/chat/validRoom/messages/1");
    }

    @Test
    public void shouldDeleteMessages_cantDeleteWhenNotExistsMessage() {
        // when then
        assertDeleteError("java.lang.IllegalArgumentException: " +
                        "Player 'player' cant delete message with id " +
                        "'100500' in room 'validRoom'",
                "/rest/chat/validRoom/messages/100500");
    }

    public void nowIs(long time) {
        when(this.time.now()).thenReturn(time);
    }

    @Test
    public void shouldGetMessage_whenPostIt() {
        // given
        assertError("java.lang.IllegalArgumentException: " +
                        "There is no message with id " +
                        "'1' in room 'validRoom'",
            "/rest/chat/validRoom/messages/1");

        // when
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // then
        assertEquals("{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message1','time':12345,'topicId':null}",
                fix(get("/rest/chat/validRoom/messages/1")));

        // when
        nowIs(23456L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        // then
        assertEquals("{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':23456,'topicId':null}",
                fix(get("/rest/chat/validRoom/messages/2")));
    }

    @Test
    public void shouldGetMessage_whenNotExists() {
        // when then
        // вообще нет сообщения
        assertError("java.lang.IllegalArgumentException: " +
                        "There is no message with id " +
                        "'100500' in room 'validRoom'",
                "/rest/chat/validRoom/messages/100500");
    }

    @Test
    public void shouldGetMessage_whenTryGetMessageFromOtherRoom_likeMessageFromMyRoom() {
        // given
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // when then
        // сообщение есть но я не могу его взять, т.к. я плеер из другой комнаты
        asUser("otherPlayer", "otherPlayer");

        assertError("java.lang.IllegalArgumentException: " +
                        "There is no message with id '1' in room 'otherRoom'",
                "/rest/chat/otherRoom/messages/1");
    }

    @Test
    public void shouldGetMessage_whenTryGetMessageFromOtherRoom() {
        // given
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // when then
        // сообщение есть но я не могу его взять, т.к. я плеер из другой комнаты
        asUser("otherPlayer", "otherPlayer");

        assertError("java.lang.IllegalArgumentException: " +
                        "Player 'otherPlayer' is not in room 'validRoom'",
                "/rest/chat/validRoom/messages/1");
    }

    @Test
    public void shouldGetAllMessages_whenTryGetMessageFromOtherRoom() {
        assertError("java.lang.IllegalArgumentException: " +
                        "Player 'player' is not in room 'otherRoom'",
                "/rest/chat/otherRoom/messages");
    }

    @Test
    public void shouldPostMessage_whenPostItForOtherRoom() {
        assertPostError("java.lang.IllegalArgumentException: " +
                        "Player 'player' is not in room 'otherRoom'",
                "/rest/chat/otherRoom/messages",
                unquote("{text:'message1'}"));
    }

    @Test
    public void shouldDeleteMessage_whenDeleteItForOtherRoom() {
        assertDeleteError("java.lang.IllegalArgumentException: " +
                        "Player 'player' is not in room 'otherRoom'",
                "/rest/chat/otherRoom/messages/1");
    }

    @Test
    public void shouldGetAllMessages_betweenBeforeAndAfter() {
        // given
        // id = 1
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // id = 2
        nowIs(12346L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        // id = 3
        nowIs(12347L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message3'}"));

        // id = 4
        nowIs(12348L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message4'}"));

        // when then
        // all
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message1','time':12345,'topicId':null},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12348,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages")));

        // when then
        // all + count
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12348,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?count=2")));

        // when then
        // between + count (ignored)
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1&beforeId=4&count=1")));

        // when then
        // between
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1&beforeId=4")));

        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages?afterId=2&beforeId=3")));

        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages?afterId=3&beforeId=3")));

        // when then
        // after + count
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1&count=2")));

        // when then
        // after
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12348,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1")));

        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12348,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?afterId=2")));

        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages?afterId=4")));

        // when then
        // before + count
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?beforeId=4&count=2")));

        // when then
        // before
        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages?beforeId=0")));

        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message1','time':12345,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?beforeId=2")));

        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message1','time':12345,'topicId':null},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?beforeId=4")));
    }

    @Test
    public void shouldGetAllMessages_betweenBeforeAndAfter_withInclude() {
        // given
        // id = 1
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // id = 2
        nowIs(12346L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        // id = 3
        nowIs(12347L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message3'}"));

        // id = 4
        nowIs(12348L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message4'}"));

        // when then
        // all
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message1','time':12345,'topicId':null},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12348,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?inclusive=true")));

        // when then
        // all + count
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12348,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?count=2&inclusive=true")));

        // when then
        // between + count (ignored)
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message1','time':12345,'topicId':null},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12348,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1&beforeId=4&count=1&inclusive=true")));

        // when then
        // between
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message1','time':12345,'topicId':null},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12348,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1&beforeId=4&inclusive=true")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?afterId=2&beforeId=3&inclusive=true")));

        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?afterId=3&beforeId=3&inclusive=true")));

        // when then
        // after + count
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message1','time':12345,'topicId':null},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1&count=2&inclusive=true")));

        // when then
        // after
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message1','time':12345,'topicId':null},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12348,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1&inclusive=true")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12348,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?afterId=2&inclusive=true")));

        assertEquals("[{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12348,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?afterId=4&inclusive=true")));

        // when then
        // before + count
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12348,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?beforeId=4&count=2&inclusive=true")));

        // when then
        // before
        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages?beforeId=0&inclusive=true")));

        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message1','time':12345,'topicId':null},\n" +
                "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?beforeId=2&inclusive=true")));

        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message1','time':12345,'topicId':null},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':null},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12348,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?beforeId=4&inclusive=true")));
    }

    private String fix(String string) {
        return quote(string).replace("},{", "},\n{");
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
        assertEquals("[{'id':11,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message11','time':12356,'topicId':null},\n" +
                        "{'id':12,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message12','time':12357,'topicId':null},\n" +
                        "{'id':13,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message13','time':12358,'topicId':null},\n" +
                        "{'id':14,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message14','time':12359,'topicId':null},\n" +
                        "{'id':15,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message15','time':12360,'topicId':null},\n" +
                        "{'id':16,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message16','time':12361,'topicId':null},\n" +
                        "{'id':17,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message17','time':12362,'topicId':null},\n" +
                        "{'id':18,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message18','time':12363,'topicId':null},\n" +
                        "{'id':19,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message19','time':12364,'topicId':null},\n" +
                        "{'id':20,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message20','time':12365,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages")));

        assertEquals("[{'id':10,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message10','time':12355,'topicId':null},\n" +
                        "{'id':11,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message11','time':12356,'topicId':null},\n" +
                        "{'id':12,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message12','time':12357,'topicId':null},\n" +
                        "{'id':13,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message13','time':12358,'topicId':null},\n" +
                        "{'id':14,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message14','time':12359,'topicId':null},\n" +
                        "{'id':15,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message15','time':12360,'topicId':null},\n" +
                        "{'id':16,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message16','time':12361,'topicId':null},\n" +
                        "{'id':17,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message17','time':12362,'topicId':null},\n" +
                        "{'id':18,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message18','time':12363,'topicId':null},\n" +
                        "{'id':19,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message19','time':12364,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?beforeId=20")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12347,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12348,'topicId':null},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12349,'topicId':null},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message5','time':12350,'topicId':null},\n" +
                        "{'id':6,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message6','time':12351,'topicId':null},\n" +
                        "{'id':7,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message7','time':12352,'topicId':null},\n" +
                        "{'id':8,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message8','time':12353,'topicId':null},\n" +
                        "{'id':9,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message9','time':12354,'topicId':null},\n" +
                        "{'id':10,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message10','time':12355,'topicId':null},\n" +
                        "{'id':11,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message11','time':12356,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12347,'topicId':null},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12348,'topicId':null},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12349,'topicId':null},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message5','time':12350,'topicId':null},\n" +
                        "{'id':6,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message6','time':12351,'topicId':null},\n" +
                        "{'id':7,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message7','time':12352,'topicId':null},\n" +
                        "{'id':8,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message8','time':12353,'topicId':null},\n" +
                        "{'id':9,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message9','time':12354,'topicId':null},\n" +
                        "{'id':10,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message10','time':12355,'topicId':null},\n" +
                        "{'id':11,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message11','time':12356,'topicId':null},\n" +
                        "{'id':12,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message12','time':12357,'topicId':null},\n" +
                        "{'id':13,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message13','time':12358,'topicId':null},\n" +
                        "{'id':14,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message14','time':12359,'topicId':null},\n" +
                        "{'id':15,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message15','time':12360,'topicId':null},\n" +
                        "{'id':16,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message16','time':12361,'topicId':null},\n" +
                        "{'id':17,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message17','time':12362,'topicId':null},\n" +
                        "{'id':18,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message18','time':12363,'topicId':null},\n" +
                        "{'id':19,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message19','time':12364,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1&beforeId=20")));
    }

    @Test
    public void shouldGetAllTopicMessages() {
        // given
        // message in room, will be topic 1
        // id = 1
        nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // message in room, will be topic 2
        // id = 2
        nowIs(12346L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        // message for topic1
        // id = 3
        nowIs(12347L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message3'}"));

        // just another one message in room
        // id = 4
        nowIs(12348L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message4'}"));

        // message for topic2
        // id = 5
        nowIs(12349L);
        post(200, "/rest/chat/validRoom/messages/2/replies",
                unquote("{text:'message5'}"));

        // message for topic1
        // id = 6
        nowIs(12350L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message6'}"));

        // just another one message in room
        // id = 7
        nowIs(12351L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message7'}"));

        // message for topic1
        // id = 8
        nowIs(12352L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message8'}"));

        // when then
        assertTopicMessages();

    }

    public void assertTopicMessages() {
        // when then
        // all for room
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message1','time':12345,'topicId':null},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message2','time':12346,'topicId':null},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12348,'topicId':null},\n" +
                        "{'id':7,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message7','time':12351,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages")));

        // when then
        // all for topic 1 message in room
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':1},\n" +
                        "{'id':6,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message6','time':12350,'topicId':1},\n" +
                        "{'id':8,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message8','time':12352,'topicId':1}]",
                fix(get("/rest/chat/validRoom/messages/1/replies")));

        // when then
        // all for topic 2 message in room
        assertEquals("[{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message5','time':12349,'topicId':2}]",
                fix(get("/rest/chat/validRoom/messages/2/replies")));

        // when then
        // get topic message like room message
        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages/3/replies")));

        // when then
        // all for non topic message in room
        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages/4/replies")));

        // when then
        // between messages in topic -> room messages between 3 ... 8
        assertEquals("[{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12348,'topicId':null},\n" +
                        "{'id':7,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message7','time':12351,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages?afterId=3&beforeId=8")));
    }

    @Test
    public void shouldPostTopicMessageForAnotherTopicMessage() {
        // given
        shouldGetAllTopicMessages();

        // topic message for topic1 message :)
        // id = 9
        nowIs(12353L);
        post(200, "/rest/chat/validRoom/messages/8/replies",
                unquote("{text:'message9'}"));

        // when then
        // all for topic 8 message in room
        assertEquals("[{'id':9,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message9','time':12353,'topicId':8}]",
                fix(get("/rest/chat/validRoom/messages/8/replies")));

        // when then
        // another the same
        assertTopicMessages();
    }

    @Test
    public void shouldDeleteTopicMessage() {
        // given
        shouldGetAllTopicMessages();

        // when
        // delete topic message
        delete("/rest/chat/validRoom/messages/5");

        // then
        // all for topic 2 message in room
        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages/2/replies")));

        // when
        // delete topic message
        delete("/rest/chat/validRoom/messages/6");

        // then
        // all for topic 1 message in room
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message3','time':12347,'topicId':1},\n" +
                        "{'id':8,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message8','time':12352,'topicId':1}]",
                fix(get("/rest/chat/validRoom/messages/1/replies")));

        // when
        // delete empty topic
        delete("/rest/chat/validRoom/messages/2");

        // then
        // all for topic 2 message in room
        assertError("java.lang.IllegalArgumentException: " +
                        "There is no message with id '2' in room 'validRoom'",
                "/rest/chat/validRoom/messages/2/replies");

        // when
        // delete not empty topic
        delete("/rest/chat/validRoom/messages/1");

        // then
        // all for topic 1 message in room
        assertError("java.lang.IllegalArgumentException: " +
                        "There is no message with id '1' in room 'validRoom'",
                "/rest/chat/validRoom/messages/1/replies");

        // when then
        // all for room
        assertEquals("[{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message4','time':12348,'topicId':null},\n" +
                        "{'id':7,'playerId':'player','playerName':'player-name','room':'validRoom','text':'message7','time':12351,'topicId':null}]",
                fix(get("/rest/chat/validRoom/messages")));
    }
}
