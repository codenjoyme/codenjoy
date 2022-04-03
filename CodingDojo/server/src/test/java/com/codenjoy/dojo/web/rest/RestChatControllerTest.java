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
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.helper.Helpers;
import com.codenjoy.dojo.services.multiplayer.GameField;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.stream.IntStream;

import static com.codenjoy.dojo.client.Utils.split;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_ENABLED;
import static com.codenjoy.dojo.utils.smart.SmartAssert.assertEquals;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

@Import(ThreeGamesConfiguration.class)
public class RestChatControllerTest extends AbstractRestControllerTest {

    @Autowired
    private Helpers with;

    @Before
    public void setup() {
        with.login.asNone();
        super.setup();

        increaseFieldIds();
        createPlayers();

        // should be the last, because other setup stuff can print to field chat
        chat.removeAll();
    }

    private void createPlayers() {
        // given
        with.time.nowIs(12342L);
        with.login.register("player", "ip", "validRoom", "first");
        with.login.asUser("player", "player");

        // then
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'Player joined the field','time':12342,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field")));

        // given
        with.time.nowIs(12343L);
        with.login.register("player2", "ip", "validRoom", "first");
        with.login.asUser("player2", "player2");

        // then
        assertEquals("[{'id':2,'playerId':'player2','playerName':'player2-name','room':'validRoom',\n" + 
                        "    'text':'Player joined the field','time':12343,'topicId':102,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field")));

        // given
        with.time.nowIs(12344L);
        with.login.register("otherPlayer", "ip", "otherRoom", "first");
        with.login.asUser("otherPlayer", "otherPlayer");

        // then
        assertEquals("[{'id':3,'playerId':'otherPlayer','playerName':'otherPlayer-name','room':'otherRoom',\n" + 
                        "    'text':'Player joined the field','time':12344,'topicId':103,'type':3}]",
                fix(get("/rest/chat/otherRoom/messages/field")));

        // given
        with.login.asUser("player", "player");
    }

    // делаю это специально, чтобы не путались в topic_id field и room
    private void increaseFieldIds() {
        for (int index = 0; index < 100; index++) {
            fields.register(mock(GameField.class));
        }
    }

    @Test
    public void shouldGetAllMessages_whenPostIt() {
        // given
        assertEquals("[]", fix(get("/rest/chat/validRoom/messages")));

        // when
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // then
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages")));

        // when
        with.time.nowIs(23456L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        // then
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':23456,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages")));
    }

    @Test
    public void shouldDeleteMessages() {
        // given
        // id = 1
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // id = 2
        with.time.nowIs(23456L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        // id = 3
        with.time.nowIs(34567L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message3'}"));

        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':23456,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':34567,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages")));

        // when
        delete("/rest/chat/validRoom/messages/1");

        // then
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':23456,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':34567,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages")));

        // when
        delete("/rest/chat/validRoom/messages/3");

        // then
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':23456,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages")));

        // when
        delete("/rest/chat/validRoom/messages/2");

        // then
        assertEquals("[]", fix(get("/rest/chat/validRoom/messages")));
    }

    @Test
    public void shouldDeleteMessages_cantDelete_whenNotMyMessage() {
        // given
        // id = 1
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages")));

        // when then
        with.login.asUser("player2", "player2");

        assertDeleteError("java.lang.IllegalArgumentException: " +
                        "Player 'player2' cant delete message with id " +
                        "'1' in room 'validRoom'",
                "/rest/chat/validRoom/messages/1");
    }

    @Test
    public void shouldDeleteMessages_cantDelete_whenNotMyRoom() {
        // given
        // id = 1
        // create message in validRoom
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages")));

        // when
        // rejoin in new room
        with.login.join("player", "otherRoom");

        // then
        // cant delete message from old room
        // (only by id = 1, room = player.room != message.room)
        assertDeleteError("java.lang.IllegalArgumentException: " +
                        "Player 'player' cant delete message with id " +
                        "'1' in room 'otherRoom'",
                "/rest/chat/otherRoom/messages/1");

        // when
        // come back in old room again
        with.login.join("player", "validRoom");

        // message in still there
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages")));
    }

    @Test
    public void shouldDeleteMessages_cantDelete_whenNotExistsMessage() {
        // when then
        assertDeleteError("java.lang.IllegalArgumentException: " +
                        "Player 'player' cant delete message with id " +
                        "'100500' in room 'validRoom'",
                "/rest/chat/validRoom/messages/100500");
    }

    @Test
    public void shouldGetMessage_success_whenPostIt_inRoomChat() {
        // when
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // then
        assertEquals("{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1}",
                fix(get("/rest/chat/validRoom/messages/1")));

        // when
        with.time.nowIs(23456L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        // then
        assertEquals("{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':23456,'topicId':null,'type':1}",
                fix(get("/rest/chat/validRoom/messages/2")));
    }

    @Test
    public void shouldGetMessage_success_whenPostIt_inTopicChat() {
        // given
        // room chat message (it will be an topic message)
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // when
        with.time.nowIs(23456L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message2'}"));

        // then
        assertEquals("{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':23456,'topicId':1,'type':2}",
                fix(get("/rest/chat/validRoom/messages/2")));

        // when
        with.time.nowIs(23457L);
        post(200, "/rest/chat/validRoom/messages/2/replies",
                unquote("{text:'message3'}"));

        // then
        assertEquals("{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':23457,'topicId':2,'type':2}",
                fix(get("/rest/chat/validRoom/messages/3")));
    }

    @Test
    public void shouldGetMessage_success_whenPostIt_inFieldChat() {
        // given
        int fieldId = with.login.fieldId("player");

        // when
        with.time.nowIs(23455L);
        post(200, "/rest/chat/validRoom/messages/field",
                unquote("{text:'message1'}"));

        // then
        assertEquals("{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':23455,'topicId':101,'type':3}",
                fix(get("/rest/chat/validRoom/messages/1")));

        // when
        with.time.nowIs(23456L);
        post(200, "/rest/chat/validRoom/messages/field",
                unquote("{text:'message2'}"));

        // then
        assertEquals("{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':23456,'topicId':101,'type':3}",
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
    public void shouldGetMessage_fail_whenTryGetMessageFromOtherRoom_likeMessageFromMyRoom() {
        // given
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // when then
        // сообщение есть но я не могу его взять, т.к. я плеер из другой комнаты
        with.login.asUser("otherPlayer", "otherPlayer");

        assertError("java.lang.IllegalArgumentException: " +
                        "There is no message with id '1' in room 'otherRoom'",
                "/rest/chat/otherRoom/messages/1");
    }

    @Test
    public void shouldGetMessage_fail_whenTryGetMessageFromOtherRoom() {
        // given
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // when then
        // сообщение есть но я не могу его взять, т.к. я плеер из другой комнаты
        with.login.asUser("otherPlayer", "otherPlayer");

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
    public void shouldPostMessage_fail_whenPostItForOtherRoom() {
        assertPostError("java.lang.IllegalArgumentException: " +
                        "Player 'player' is not in room 'otherRoom'",
                "/rest/chat/otherRoom/messages",
                unquote("{text:'message1'}"));
    }

    @Test
    public void shouldPostMessage_fail_whenThreadTopicIsNotExists() {
        // given
        with.login.assertPlayerInRoom("player", "validRoom");

        // when then
        // try to post reply for non exists topic message
        with.time.nowIs(12345L);
        assertPostError("java.lang.IllegalArgumentException: " +
                        "There is no message with id '100500' in room 'validRoom'",
                "/rest/chat/validRoom/messages/100500/replies",
                unquote("{text:'message1'}"));

        // then
        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages")));

        // then
        // no topic, no replies
        assertGetError("java.lang.IllegalArgumentException: " +
                        "There is no message with id '100500' in room 'validRoom'",
                "/rest/chat/validRoom/messages/100500/replies");
    }

    @Test
    public void shouldPostMessageForField_success() {
        // given
        with.login.assertPlayerInRoom("player", "validRoom");
        int fieldId = with.login.fieldId("player");

        // when then
        // try to post message for exists field
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages/field",
                unquote("{text:'message1'}"));

        // then
        // no messages in main room chat
        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages")));

        // then
        // but we can get field-chat messages
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field")));
    }

    @Test
    public void shouldPostMessageForField_fail_whenThreadTopicInOtherRoom() {
        // given
        with.login.assertPlayerInRoom("player", "validRoom");
        int fieldId = with.login.fieldId("player");

        // when then
        // try to post field message for other room
        assertPostError("java.lang.IllegalArgumentException: " +
                        "There is no player 'player' in room 'otherRoom'",
                "/rest/chat/otherRoom/messages/field",
                unquote("{text:'message1'}"));

        // when then
        // there are no messages here
        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages")));

        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages/field")));

        // when then
        // try to get field message from other room
        assertGetError("java.lang.IllegalArgumentException: " +
                        "There is no player 'player' in room 'otherRoom'",
                "/rest/chat/otherRoom/messages/field");

        // when
        // rejoin in other room
        with.login.join("player", "otherRoom");

        // when then
        // there are no messages here
        assertEquals("[]",
                fix(get("/rest/chat/otherRoom/messages")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'otherRoom',\n" + 
                        "    'text':'Player joined the field','time':12344,'topicId':104,'type':3}]",
                fix(get("/rest/chat/otherRoom/messages/field")));
    }

    @Test
    public void shouldPostMessage_fail_whenThreadTopicInOtherRoom() {
        // given
        with.login.assertPlayerInRoom("player", "validRoom");

        assertEquals("[]", fix(get("/rest/chat/validRoom/messages")));

        // post message that will be a root topic message
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // when
        // rejoin in other room
        with.login.join("player", "otherRoom");

        // try to post reply for topic message in other room
        assertPostError("java.lang.IllegalArgumentException: " +
                        "There is no message with id '1' in room 'otherRoom'",
                "/rest/chat/otherRoom/messages/1/replies",
                unquote("{text:'message1'}"));

        // then
        // rejoin in old room
        with.login.join("player", "validRoom");

        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages")));

        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages/1/replies")));
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
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // id = 2
        with.time.nowIs(12346L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        // id = 3
        with.time.nowIs(12347L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message3'}"));

        // id = 4
        with.time.nowIs(12348L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message4'}"));

        // when then
        // all
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages")));

        // when then
        // all + count
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?count=2")));

        // when then
        // between + count (ignored)
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1&beforeId=4&count=1")));

        // when then
        // between
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1&beforeId=4")));

        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages?afterId=2&beforeId=3")));

        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages?afterId=3&beforeId=3")));

        // when then
        // after + count
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1&count=2")));

        // when then
        // after
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1")));

        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?afterId=2")));

        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages?afterId=4")));

        // when then
        // before + count
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?beforeId=4&count=2")));

        // when then
        // before
        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages?beforeId=0")));

        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?beforeId=2")));

        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?beforeId=4")));
    }

    @Test
    public void shouldGetAllMessages_betweenBeforeAndAfter_forTopic() {
        // given
        // id = 1 will be a topic id
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // id = 2
        with.time.nowIs(12346L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message2'}"));

        // id = 3
        with.time.nowIs(12347L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message3'}"));

        // id = 4
        with.time.nowIs(12348L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message4'}"));

        // id = 5
        with.time.nowIs(12349L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message5'}"));

        // when then
        // all
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':1,'type':2},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies")));

        // when then
        // all + count
        assertEquals("[{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?count=2")));

        // when then
        // between + count (ignored)
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?afterId=2&beforeId=5&count=1")));

        // when then
        // between
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?afterId=2&beforeId=5")));

        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages/1/replies?afterId=3&beforeId=4")));

        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages/1/replies?afterId=4&beforeId=4")));

        // when then
        // after + count
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?afterId=2&count=2")));

        // when then
        // after
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?afterId=2")));

        assertEquals("[{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?afterId=3")));

        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages/1/replies?afterId=5")));

        // when then
        // before + count
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?beforeId=5&count=2")));

        // when then
        // before
        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages/1/replies?beforeId=1")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?beforeId=3")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':1,'type':2},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?beforeId=5")));
    }

    @Test
    public void shouldGetAllMessages_betweenBeforeAndAfter_forField() {
        // given
        // id = 1 topic message
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // id = 2
        with.time.nowIs(12346L);
        post(200, "/rest/chat/validRoom/messages/field",
                unquote("{text:'message3'}"));

        // id = 3
        with.time.nowIs(12347L);
        post(200, "/rest/chat/validRoom/messages/field",
                unquote("{text:'message4'}"));

        // id = 4
        with.time.nowIs(12348L);
        post(200, "/rest/chat/validRoom/messages/field",
                unquote("{text:'message5'}"));

        // id = 5
        with.time.nowIs(12349L);
        post(200, "/rest/chat/validRoom/messages/field",
                unquote("{text:'message6'}"));

        // when then
        // all
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12346,'topicId':101,'type':3},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12347,'topicId':101,'type':3},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12348,'topicId':101,'type':3},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message6','time':12349,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field")));

        // when then
        assertEquals("[{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12348,'topicId':101,'type':3},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message6','time':12349,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?count=2")));

        // when then
        // between + count (ignored)
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12346,'topicId':101,'type':3},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12347,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=1&beforeId=4&count=1")));

        // when then
        // between
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12347,'topicId':101,'type':3},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12348,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=2&beforeId=5")));

        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=2&beforeId=3")));

        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=3&beforeId=3")));

        // when then
        // after + count
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12347,'topicId':101,'type':3},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12348,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=2&count=2")));

        // when then
        // after
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12347,'topicId':101,'type':3},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12348,'topicId':101,'type':3},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message6','time':12349,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=2")));

        assertEquals("[{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12348,'topicId':101,'type':3},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message6','time':12349,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=3")));

        assertEquals("[{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message6','time':12349,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=4")));

        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=5")));

        // when then
        // before + count
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12346,'topicId':101,'type':3},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12347,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?beforeId=4&count=2")));

        // when then
        // before
        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages/field?beforeId=0")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12346,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?beforeId=3")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12346,'topicId':101,'type':3},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12347,'topicId':101,'type':3},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12348,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?beforeId=5")));
    }

    @Test
    public void shouldGetAllMessages_betweenBeforeAndAfter_withInclude() {
        // given
        // id = 1
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // id = 2
        with.time.nowIs(12346L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        // id = 3
        with.time.nowIs(12347L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message3'}"));

        // id = 4
        with.time.nowIs(12348L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message4'}"));

        // when then
        // all
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?inclusive=true")));

        // when then
        // all + count
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?count=2&inclusive=true")));

        // when then
        // between + count (ignored)
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1&beforeId=4&count=1&inclusive=true")));

        // when then
        // between
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1&beforeId=4&inclusive=true")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?afterId=2&beforeId=3&inclusive=true")));

        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?afterId=3&beforeId=3&inclusive=true")));

        // when then
        // after + count
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1&count=2&inclusive=true")));

        // when then
        // after
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1&inclusive=true")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?afterId=2&inclusive=true")));

        assertEquals("[{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?afterId=4&inclusive=true")));

        // when then
        // before + count
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?beforeId=4&count=2&inclusive=true")));

        // when then
        // before
        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages?beforeId=0&inclusive=true")));

        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?beforeId=2&inclusive=true")));

        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':null,'type':1},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?beforeId=4&inclusive=true")));
    }

    @Test
    public void shouldGetAllMessages_betweenBeforeAndAfter_withInclude_forTopic() {
        // given
        // id = 1 will be a topic id
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // id = 2
        with.time.nowIs(12346L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message2'}"));

        // id = 3
        with.time.nowIs(12347L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message3'}"));

        // id = 4
        with.time.nowIs(12348L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message4'}"));

        // id = 5
        with.time.nowIs(12349L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message5'}"));

        // when then
        // all
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':1,'type':2},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?inclusive=true")));

        // when then
        // all + count
        assertEquals("[{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?count=2&inclusive=true")));

        // when then
        // between + count (ignored)
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':1,'type':2},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?afterId=2&beforeId=5&count=1&inclusive=true")));

        // when then
        // between
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':1,'type':2},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?afterId=2&beforeId=5&inclusive=true")));

        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?afterId=3&beforeId=4&inclusive=true")));

        assertEquals("[{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?afterId=4&beforeId=4&inclusive=true")));

        // when then
        // after + count
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':1,'type':2},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?afterId=2&count=2&inclusive=true")));

        // when then
        // after
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':1,'type':2},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?afterId=2&inclusive=true")));

        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?afterId=3&inclusive=true")));

        assertEquals("[{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?afterId=5&inclusive=true")));

        // when then
        // before + count
        assertEquals("[{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?beforeId=5&count=2&inclusive=true")));

        // when then
        // before
        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages/1/replies?beforeId=0&inclusive=true")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':1,'type':2},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?beforeId=3&inclusive=true")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':1,'type':2},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':2},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies?beforeId=5&inclusive=true")));
    }

    @Test
    public void shouldGetAllMessages_betweenBeforeAndAfter_withInclude_forField() {
        // given
        // id = 1 topic message
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // id = 2
        with.time.nowIs(12346L);
        post(200, "/rest/chat/validRoom/messages/field",
                unquote("{text:'message2'}"));

        // id = 3
        with.time.nowIs(12347L);
        post(200, "/rest/chat/validRoom/messages/field",
                unquote("{text:'message3'}"));

        // id = 4
        with.time.nowIs(12348L);
        post(200, "/rest/chat/validRoom/messages/field",
                unquote("{text:'message4'}"));

        // id = 5
        with.time.nowIs(12349L);
        post(200, "/rest/chat/validRoom/messages/field",
                unquote("{text:'message5'}"));

        // when then
        // all
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':101,'type':3},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':101,'type':3},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':101,'type':3},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?inclusive=true")));

        // when then
        assertEquals("[{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':101,'type':3},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?count=2&inclusive=true")));

        // when then
        // between + count (ignored)
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':101,'type':3},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':101,'type':3},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=1&beforeId=4&count=1&inclusive=true")));

        // when then
        // between
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':101,'type':3},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':101,'type':3},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':101,'type':3},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=2&beforeId=5&inclusive=true")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':101,'type':3},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=2&beforeId=3&inclusive=true")));

        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=3&beforeId=3&inclusive=true")));

        // when then
        // after + count
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':101,'type':3},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=2&count=2&inclusive=true")));

        // when then
        // after
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':101,'type':3},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':101,'type':3},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':101,'type':3},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=2&inclusive=true")));

        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':101,'type':3},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':101,'type':3},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=3&inclusive=true")));

        assertEquals("[{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':101,'type':3},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=4&inclusive=true")));

        assertEquals("[{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?afterId=5&inclusive=true")));

        // when then
        // before + count
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':101,'type':3},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?beforeId=4&count=2&inclusive=true")));

        // when then
        // before
        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages/field?beforeId=0&inclusive=true")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':101,'type':3},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?beforeId=3&inclusive=true")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':101,'type':3},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':101,'type':3},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':101,'type':3},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field?beforeId=5&inclusive=true")));
    }

    private String fix(String string) {
        return split(quote(string),
                "},\n{",
                "',\n\t'text':");
    }

    @Test
    public void shouldGetAllMessages_countByDefault() {
        // given
        int startInclusive = 1;
        int endExclusive = 21;
        IntStream.range(startInclusive, endExclusive).forEach(index -> {
            with.time.nowIs(12345L + index);
            post(200, "/rest/chat/validRoom/messages",
                    unquote("{text:'message" + index + "'}"));
        });


        // when then
        assertEquals("[{'id':11,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message11','time':12356,'topicId':null,'type':1},\n" +
                        "{'id':12,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message12','time':12357,'topicId':null,'type':1},\n" +
                        "{'id':13,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message13','time':12358,'topicId':null,'type':1},\n" +
                        "{'id':14,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message14','time':12359,'topicId':null,'type':1},\n" +
                        "{'id':15,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message15','time':12360,'topicId':null,'type':1},\n" +
                        "{'id':16,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message16','time':12361,'topicId':null,'type':1},\n" +
                        "{'id':17,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message17','time':12362,'topicId':null,'type':1},\n" +
                        "{'id':18,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message18','time':12363,'topicId':null,'type':1},\n" +
                        "{'id':19,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message19','time':12364,'topicId':null,'type':1},\n" +
                        "{'id':20,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message20','time':12365,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages")));

        assertEquals("[{'id':10,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message10','time':12355,'topicId':null,'type':1},\n" +
                        "{'id':11,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message11','time':12356,'topicId':null,'type':1},\n" +
                        "{'id':12,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message12','time':12357,'topicId':null,'type':1},\n" +
                        "{'id':13,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message13','time':12358,'topicId':null,'type':1},\n" +
                        "{'id':14,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message14','time':12359,'topicId':null,'type':1},\n" +
                        "{'id':15,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message15','time':12360,'topicId':null,'type':1},\n" +
                        "{'id':16,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message16','time':12361,'topicId':null,'type':1},\n" +
                        "{'id':17,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message17','time':12362,'topicId':null,'type':1},\n" +
                        "{'id':18,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message18','time':12363,'topicId':null,'type':1},\n" +
                        "{'id':19,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message19','time':12364,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?beforeId=20")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12347,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12348,'topicId':null,'type':1},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12349,'topicId':null,'type':1},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12350,'topicId':null,'type':1},\n" +
                        "{'id':6,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message6','time':12351,'topicId':null,'type':1},\n" +
                        "{'id':7,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message7','time':12352,'topicId':null,'type':1},\n" +
                        "{'id':8,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message8','time':12353,'topicId':null,'type':1},\n" +
                        "{'id':9,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message9','time':12354,'topicId':null,'type':1},\n" +
                        "{'id':10,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message10','time':12355,'topicId':null,'type':1},\n" +
                        "{'id':11,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message11','time':12356,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1")));

        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12347,'topicId':null,'type':1},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12348,'topicId':null,'type':1},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12349,'topicId':null,'type':1},\n" +
                        "{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12350,'topicId':null,'type':1},\n" +
                        "{'id':6,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message6','time':12351,'topicId':null,'type':1},\n" +
                        "{'id':7,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message7','time':12352,'topicId':null,'type':1},\n" +
                        "{'id':8,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message8','time':12353,'topicId':null,'type':1},\n" +
                        "{'id':9,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message9','time':12354,'topicId':null,'type':1},\n" +
                        "{'id':10,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message10','time':12355,'topicId':null,'type':1},\n" +
                        "{'id':11,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message11','time':12356,'topicId':null,'type':1},\n" +
                        "{'id':12,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message12','time':12357,'topicId':null,'type':1},\n" +
                        "{'id':13,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message13','time':12358,'topicId':null,'type':1},\n" +
                        "{'id':14,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message14','time':12359,'topicId':null,'type':1},\n" +
                        "{'id':15,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message15','time':12360,'topicId':null,'type':1},\n" +
                        "{'id':16,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message16','time':12361,'topicId':null,'type':1},\n" +
                        "{'id':17,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message17','time':12362,'topicId':null,'type':1},\n" +
                        "{'id':18,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message18','time':12363,'topicId':null,'type':1},\n" +
                        "{'id':19,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message19','time':12364,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?afterId=1&beforeId=20")));
    }

    @Test
    public void shouldGetAllTopicMessages() {
        // given
        // message in room, will be topic 1
        // id = 1
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // message in room, will be topic 2
        // id = 2
        with.time.nowIs(12346L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message2'}"));

        // message for topic1
        // id = 3
        with.time.nowIs(12347L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message3'}"));

        // just another one message in room
        // id = 4
        with.time.nowIs(12348L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message4'}"));

        // message for topic2
        // id = 5
        with.time.nowIs(12349L);
        post(200, "/rest/chat/validRoom/messages/2/replies",
                unquote("{text:'message5'}"));

        // message for topic1
        // id = 6
        with.time.nowIs(12350L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message6'}"));

        // just another one message in room
        // id = 7
        with.time.nowIs(12351L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message7'}"));

        // message for topic1
        // id = 8
        with.time.nowIs(12352L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message8'}"));

        // when then
        assertTopicMessages();
    }

    public void assertTopicMessages() {
        // when then
        // all for room
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':null,'type':1},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':null,'type':1},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':null,'type':1},\n" +
                        "{'id':7,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message7','time':12351,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages")));

        // when then
        // all for topic 1 message in room
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2},\n" +
                        "{'id':6,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message6','time':12350,'topicId':1,'type':2},\n" +
                        "{'id':8,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message8','time':12352,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies")));

        // when then
        // all for topic 2 message in room
        assertEquals("[{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message5','time':12349,'topicId':2,'type':2}]",
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
        assertEquals("[{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':null,'type':1},\n" +
                        "{'id':7,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message7','time':12351,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages?afterId=3&beforeId=8")));
    }

    @Test
    public void shouldGetAllTopicMessages_fail_whenTryToGetItFromOtherRoom() {
        // given
        with.login.assertPlayerInRoom("player", "validRoom");

        // message in room, will be topic 1
        // id = 1
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'message1'}"));

        // message for topic1
        // id = 2
        with.time.nowIs(12346L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message2'}"));

        // just another one message in room
        // id = 3
        with.time.nowIs(12347L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message3'}"));

        // can get all topic messages
        assertEquals("[{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':1,'type':2},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/1/replies")));

        // when
        with.login.join("player", "otherRoom");

        // then
        // cant get topic messages from other room
        assertGetError("java.lang.IllegalArgumentException: " +
                        "There is no message with id '1' in room 'otherRoom'",
                "/rest/chat/otherRoom/messages/1/replies");
    }

    @Test
    public void shouldPostTopicMessageForAnotherTopicMessage() {
        // given
        shouldGetAllTopicMessages();

        // topic message for topic1 message :)
        // id = 9
        with.time.nowIs(12353L);
        post(200, "/rest/chat/validRoom/messages/8/replies",
                unquote("{text:'message9'}"));

        // when then
        // all for topic 8 message in room
        assertEquals("[{'id':9,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message9','time':12353,'topicId':8,'type':2}]",
                fix(get("/rest/chat/validRoom/messages/8/replies")));

        // when then
        // another the same
        assertTopicMessages();
    }

    @Test
    public void shouldPostTopicMessageForFieldMessage() {
        // given
        // first field message
        with.time.nowIs(12345L);
        post(200, "/rest/chat/validRoom/messages/field",
                unquote("{text:'message1'}"));

        // second field message
        with.time.nowIs(12346L);
        post(200, "/rest/chat/validRoom/messages/field",
                unquote("{text:'message2'}"));

        // try to post topic messages for exists field message1
        with.time.nowIs(12347L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message3'}"));

        with.time.nowIs(12348L);
        post(200, "/rest/chat/validRoom/messages/1/replies",
                unquote("{text:'message4'}"));

        // try to post topic messages for exists field message2
        with.time.nowIs(12347L);
        post(200, "/rest/chat/validRoom/messages/2/replies",
                unquote("{text:'message3'}"));

        with.time.nowIs(12348L);
        post(200, "/rest/chat/validRoom/messages/2/replies",
                unquote("{text:'message4'}"));

        // when then
        // field topic messages
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':4},\n" +
                        "{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':1,'type':4}]",
                fix(get("/rest/chat/validRoom/messages/1/replies")));

        // field topic messages
        assertEquals("[{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':2,'type':4},\n" +
                        "{'id':6,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':2,'type':4}]",
                fix(get("/rest/chat/validRoom/messages/2/replies")));

        // room messages
        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages")));

        // field messages
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':101,'type':3},\n" +
                        "{'id':2,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message2','time':12346,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field")));
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
        assertEquals("[{'id':3,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message3','time':12347,'topicId':1,'type':2},\n" +
                        "{'id':8,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message8','time':12352,'topicId':1,'type':2}]",
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
        assertEquals("[{'id':4,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message4','time':12348,'topicId':null,'type':1},\n" +
                        "{'id':7,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message7','time':12351,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages")));
    }

    @Test
    public void shouldDeleteFieldMessage() {
        // given
        shouldPostMessageForField_success();

        int fieldId = with.login.fieldId("player");
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field")));

        // when
        // delete field message
        delete("/rest/chat/validRoom/messages/1");

        // then
        assertEquals("[]",
                fix(get("/rest/chat/validRoom/messages/field")));
    }

    @Test
    public void shouldDeleteFieldMessage_whenChangedField() {
        // given
        shouldPostMessageForField_success();

        int fieldId = with.login.fieldId("player");
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'message1','time':12345,'topicId':101,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field")));

        // switch to another, and come back again
        with.login.join("player", "otherRoom");
        with.login.join("player", "validRoom");
        assertNotEquals(fieldId, with.login.fieldId("player"));

        assertEquals("[{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'Player joined the field','time':12345,'topicId':105,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field")));

        // when
        // delete field message
        delete("/rest/chat/validRoom/messages/1");

        // then
        assertEquals("[{'id':5,'playerId':'player','playerName':'player-name','room':'validRoom',\n" + 
                        "    'text':'Player joined the field','time':12345,'topicId':105,'type':3}]",
                fix(get("/rest/chat/validRoom/messages/field")));
    }

    @Test
    public void shouldPrintJoinLeaveChat_forNewUser_inSameMultiplayerField() {
        // given
        // game type will be a multiple
        with.rooms.settings("multipleRoom", "third")
                .bool(ROUNDS_ENABLED, false);

        with.time.nowIs(12345L);
        with.login.register("player4", "ip", "multipleRoom", "third");
        with.login.asUser("player4", "player4");
        int fieldId = with.login.fieldId("player4");

        with.time.nowIs(12346L);
        with.login.join("player", "multipleRoom");

        // another player is also in the same room
        assertEquals("multipleRoom", deals.get("player").getRoom());
        // another player is also in the same field
        assertEquals(fieldId, with.login.fieldId("player"));

        // then
        // prints that two players joined
        assertEquals("[{'id':1,'playerId':'player4','playerName':'player4-name','room':'multipleRoom',\n" + 
                        "    'text':'Player joined the field','time':12345,'topicId':104,'type':3},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'multipleRoom',\n" + 
                        "    'text':'Player joined the field','time':12346,'topicId':104,'type':3}]",
                fix(get("/rest/chat/multipleRoom/messages/field")));

        // switch to another room
        with.time.nowIs(12347L);
        with.login.join("player4", "otherRoom");
        assertNotEquals(fieldId, with.login.fieldId("player4"));

        assertEquals("[{'id':5,'playerId':'player4','playerName':'player4-name','room':'otherRoom',\n" + 
                        "    'text':'Player joined the field','time':12347,'topicId':105,'type':3}]",
                fix(get("/rest/chat/otherRoom/messages/field")));

        // and come back again
        with.time.nowIs(12348L);
        with.login.join("player4", "multipleRoom");

        // prints that one leaved field and come back again
        assertEquals("[{'id':1,'playerId':'player4','playerName':'player4-name','room':'multipleRoom',\n" + 
                        "    'text':'Player joined the field','time':12345,'topicId':104,'type':3},\n" +
                        "{'id':3,'playerId':'player','playerName':'player-name','room':'multipleRoom',\n" + 
                        "    'text':'Player joined the field','time':12346,'topicId':104,'type':3},\n" +
                        "{'id':4,'playerId':'player4','playerName':'player4-name','room':'multipleRoom',\n" + 
                        "    'text':'Player left the field','time':12347,'topicId':104,'type':3},\n" +
                        "{'id':7,'playerId':'player4','playerName':'player4-name','room':'multipleRoom',\n" + 
                        "    'text':'Player joined the field','time':12348,'topicId':104,'type':3}]",
                fix(get("/rest/chat/multipleRoom/messages/field")));
    }

    @Test
    public void shouldPrintJoinLeaveChat_forNewUser_inDifferentSingleFields() {
        // given
        // game type will be a single
        String game = "first";

        with.time.nowIs(12345L);
        with.login.register("player4", "ip", "singleRoom", game);
        with.login.asUser("player4", "player4");
        int fieldId = with.login.fieldId("player4");

        with.login.join("player", "singleRoom");

        // another player is also in the same room
        assertEquals("singleRoom", deals.get("player").getRoom());
        // another player isn't in the same field
        assertNotEquals(fieldId, with.login.fieldId("player"));

        // then
        // prints only one 'joined' message, because of player is on single field
        assertEquals("[{'id':1,'playerId':'player4','playerName':'player4-name','room':'singleRoom',\n" + 
                        "    'text':'Player joined the field','time':12345,'topicId':104,'type':3}]",
                fix(get("/rest/chat/singleRoom/messages/field")));

        // switch to another room
        with.time.nowIs(12346L);
        with.login.join("player4", "otherRoom");
        assertNotEquals(fieldId, with.login.fieldId("player4"));

        assertEquals("[{'id':5,'playerId':'player4','playerName':'player4-name','room':'otherRoom',\n" + 
                        "    'text':'Player joined the field','time':12346,'topicId':106,'type':3}]",
                fix(get("/rest/chat/otherRoom/messages/field")));

        // and come back again
        with.time.nowIs(12347L);
        with.login.join("player4", "singleRoom");

        // prints only one 'joined' message,
        //      because of new field created
        //      and this is a single field
        assertEquals("[{'id':7,'playerId':'player4','playerName':'player4-name','room':'singleRoom',\n" + 
                        "    'text':'Player joined the field','time':12347,'topicId':107,'type':3}]",
                fix(get("/rest/chat/singleRoom/messages/field")));
    }

    @Test
    public void shouldValidateMessageLength_whenPostLargeMessage() {
        // given
        assertEquals("[]", fix(get("/rest/chat/validRoom/messages")));

        // when
        // valid message with max size length
        with.time.nowIs(12345L);
        String message1 = leftPad("", Chat.MESSAGE_MAX_LENGTH, '*');
        post(200, "/rest/chat/validRoom/messages",
                unquote("{text:'" + message1 + "'}"));

        // then
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" +
                        "    'text':'" + message1 + "','time':12345,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages")));

        // when
        // too long invalid message
        with.time.nowIs(23456L);
        String message2 = message1 + "*";
        assertPostError("java.lang.IllegalArgumentException: Chat message is too long. Max size is: 12000",
                "/rest/chat/validRoom/messages",
                unquote("{text:'" + message2 + "'}"));

        // then
        assertEquals("[{'id':1,'playerId':'player','playerName':'player-name','room':'validRoom',\n" +
                        "    'text':'" + message1 + "','time':12345,'topicId':null,'type':1}]",
                fix(get("/rest/chat/validRoom/messages")));
    }
}
