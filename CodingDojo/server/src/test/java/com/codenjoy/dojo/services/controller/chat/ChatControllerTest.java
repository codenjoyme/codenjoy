package com.codenjoy.dojo.services.controller.chat;

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


import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.services.FieldService;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.TimeService;
import com.codenjoy.dojo.services.chat.ChatControl;
import com.codenjoy.dojo.services.chat.ChatService;
import com.codenjoy.dojo.services.chat.Filter;
import com.codenjoy.dojo.services.controller.AbstractControllerTest;
import com.codenjoy.dojo.services.controller.Controller;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.helper.ChatHelper;
import com.codenjoy.dojo.services.helper.RoomHelper;
import com.codenjoy.dojo.services.room.RoomService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static com.codenjoy.dojo.services.chat.ChatType.*;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.*;
import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static java.util.stream.Collectors.joining;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ChatControllerTest extends AbstractControllerTest<String, ChatControl> {

    @Autowired
    private ChatController controller;

    @Autowired
    private FieldService fields;

    @Autowired
    private RoomService rooms;

    @Autowired
    private GameService games;

    @SpyBean
    private ChatService chatService;

    @SpyBean
    private TimeService time;

    @Autowired
    private Chat chat;
    
    private ChatHelper messages;
    private RoomHelper roomsSettings;

    @Before
    public void setup() {
        super.setup();

        messages = new ChatHelper(chat);
        roomsSettings = new RoomHelper(rooms, games);

        messages.removeAll();
        fields.removeAll();

        setupChatControl();
    }

    @Override
    protected String endpoint() {
        return "chat-ws";
    }

    // we wrap ChatControl in spy to eavesdrop on how it is being used
    private void setupChatControl() {
        when(chatService.control(anyString(), any(ChatControl.OnChange.class)))
                .thenAnswer(inv -> chatControl((ChatControl) inv.callRealMethod()));
    }

    // TODO if you find in moсkito how to subscribe to all methods at once - super!
    private ChatControl chatControl(ChatControl control) {
        ChatControl spy = spy(control);
        Answer<?> answer = invocation -> {
            serverReceived(String.format("%s(%s)",
                    invocation.getMethod().getName(),
                    Arrays.stream(invocation.getArguments())
                            .map(Object::toString)
                            .collect(joining(", "))));

            return invocation.callRealMethod();
        };
        doAnswer(answer).when(spy).get(anyInt(), anyString());
        doAnswer(answer).when(spy).delete(anyInt(), anyString());
        doAnswer(answer).when(spy).getAllRoom(any(Filter.class));
        doAnswer(answer).when(spy).getAllTopic(anyInt(), any(Filter.class));
        doAnswer(answer).when(spy).getAllField(any(Filter.class));
        doAnswer(answer).when(spy).postRoom(anyString(), anyString());
        doAnswer(answer).when(spy).postTopic(anyInt(), anyString(), anyString());
        doAnswer(answer).when(spy).postField(anyString(), anyString());
        return spy;
    }

    public void nowIs(long time) {
        when(this.time.now()).thenReturn(time);
    }

    @Override
    protected Controller<String, ChatControl> controller() {
        return controller;
    }

    @Test
    public void shouldGet_fail() {
        // given
        createPlayer("player", "room", "first");

        client(0).start();

        // when
        client(0).sendToServer("{'command':'get', " +
                "'data':{'id':1, 'room':'room'}}");
        waitForServerReceived();
        waitForClientReceived(0);

        // then
        assertEquals("[get(1, room)]", receivedOnServer());
        assertEquals("[{'command':'error', 'data':" +
                        "{'error':'IllegalArgumentException'," +
                        "'message':'There is no message with id '1' in room 'room''}}]",
                client(0).messages());
    }
    
    @Test
    public void shouldGet_success() {
        // given
        createPlayer("player", "room", "first");

        client(0).start();

        messages.post("room", "player", null, ROOM); // 1

        // when
        client(0).sendToServer("{'command':'get', " +
                "'data':{'id':1, 'room':'room'}}");
        waitForServerReceived();
        waitForClientReceived(0);

        // then
        assertEquals("[get(1, room)]", receivedOnServer());
        assertEquals("[{'command':'add', 'data':[" +
                        "{'id':1,'text':'message1','room':'room','type':1,'topicId':null," +
                        "'playerId':'player','playerName':'player-name','time':1615231523345}]}]",
                client(0).messages());
    }

    @Test
    public void shouldDelete_success() {
        // given
        createPlayer("player", "room", "first");

        client(0).start();

        messages.post("room", "player", null, ROOM); // 1

        // when
        client(0).sendToServer("{'command':'delete', " +
                "'data':{'id':1, 'room':'room'}}");
        waitForServerReceived();
        waitForClientReceived(0);

        // then
        assertEquals("[delete(1, room)]", receivedOnServer());
        assertEquals("[{'command':'delete', 'data':[" +
                        "{'id':1,'text':'message1','room':'room','type':1,'topicId':null," +
                        "'playerId':'player','playerName':'player-name','time':1615231523345}]}]",
                client(0).messages());

        assertEquals(null, chat.getMessageById(1));
    }

    @Test
    public void shouldDelete_fail() {
        // given
        createPlayer("player", "room", "first");

        client(0).start();

        // when
        client(0).sendToServer("{'command':'delete', " +
                "'data':{'id':1, 'room':'room'}}");
        waitForServerReceived();
        waitForClientReceived(0);

        // then
        assertEquals("[delete(1, room)]", receivedOnServer());
        assertEquals("[{'command':'error', 'data':" +
                        "{'error':'IllegalArgumentException'," +
                        "'message':'Player 'player' cant delete " +
                                    "message with id '1' in room 'room''}}]",
                client(0).messages());
    }

    @Test
    public void shouldGetAllRoom_fail() {
        // given
        createPlayer("player", "room", "first");

        client(0).start();

        // when
        client(0).sendToServer("{'command':'getAllRoom', " +
                "'data':{'room':'otherRoom', 'count':1}}");
        waitForServerReceived();
        waitForClientReceived(0);

        // then
        assertEquals("[getAllRoom(Filter(room=otherRoom, count=1, " +
                        "afterId=null, beforeId=null, inclusive=null))]",
                receivedOnServer());
        assertEquals("[{'command':'error', 'data':" +
                        "{'error':'IllegalArgumentException'," +
                        "'message':'Player 'player' is not in room 'otherRoom''}}]",
                client(0).messages());
    }

    @Test
    public void shouldGetAllRoom_success() {
        // given
        createPlayer("player", "room", "first");

        client(0).start();

        messages.post("room", "player", null, ROOM); // 1
        messages.post("room", "player", null, ROOM); // 2
        messages.post("room", "player", null, ROOM); // 3

        // when
        client(0).sendToServer("{'command':'getAllRoom', " +
                "'data':{'room':'room', 'count':2}}");
        waitForServerReceived();
        waitForClientReceived(0);

        // then
        assertEquals("[getAllRoom(Filter(room=room, count=2, " +
                "afterId=null, beforeId=null, inclusive=null))]",
                receivedOnServer());
        assertEquals("[{'command':'add', 'data':[" +
                        "{'id':2,'text':'message2','room':'room','type':1,'topicId':null," +
                        "'playerId':'player','playerName':'player-name','time':1615231623345}," +
                        "{'id':3,'text':'message3','room':'room','type':1,'topicId':null," +
                        "'playerId':'player','playerName':'player-name','time':1615231723345}]}]",
                client(0).messages());
    }

    @Test
    public void shouldGetAllTopic_fail() {
        // given
        createPlayer("player", "room", "first");

        client(0).start();

        // when
        client(0).sendToServer("{'command':'getAllTopic', " +
                "'data':{'id':1, 'room':'room', 'count':1}}");
        waitForServerReceived();
        waitForClientReceived(0);

        // then
        assertEquals("[getAllTopic(1, Filter(room=room, count=1, " +
                        "afterId=null, beforeId=null, inclusive=null))]",
                receivedOnServer());
        assertEquals("[{'command':'error', 'data':" +
                        "{'error':'IllegalArgumentException'," +
                        "'message':'There is no message with id '1' in room 'room''}}]",
                client(0).messages());
    }

    @Test
    public void shouldGetAllTopic_success() {
        // given
        createPlayer("player", "room", "first");

        client(0).start();

        messages.post("room", "player", null, ROOM); // 1
        messages.post("room", "player", 1, TOPIC); // 2
        messages.post("room", "player", 1, TOPIC); // 3

        // when
        client(0).sendToServer("{'command':'getAllTopic', " +
                "'data':{'id':1, 'room':'room', 'count':2}}");
        waitForServerReceived();
        waitForClientReceived(0);

        // then
        assertEquals("[getAllTopic(1, Filter(room=room, count=2, " +
                        "afterId=null, beforeId=null, inclusive=null))]",
                receivedOnServer());
        assertEquals("[{'command':'add', 'data':[" +
                        "{'id':2,'text':'message2','room':'room','type':2,'topicId':1," +
                        "'playerId':'player','playerName':'player-name','time':1615231623345}," +
                        "{'id':3,'text':'message3','room':'room','type':2,'topicId':1," +
                        "'playerId':'player','playerName':'player-name','time':1615231723345}]}]",
                client(0).messages());
    }

    @Test
    public void shouldGetAllField_fail() {
        // given
        createPlayer("player", "room", "first");

        client(0).start();

        // when
        client(0).sendToServer("{'command':'getAllField', " +
                "'data':{'room':'otherRoom', 'count':1}}");
        waitForServerReceived();
        waitForClientReceived(0);

        // then
        assertEquals("[getAllField(Filter(room=otherRoom, count=1, " +
                        "afterId=null, beforeId=null, inclusive=null))]",
                receivedOnServer());
        assertEquals("[{'command':'error', 'data':" +
                        "{'error':'IllegalArgumentException'," +
                        "'message':'There is no player 'player' in room 'otherRoom''}}]",
                client(0).messages());
    }

    @Test
    public void shouldGetAllField_success() {
        // given
        createPlayer("player", "room", "first");

        client(0).start();

        messages.post("room", "player", null, ROOM); // 1
        messages.post("room", "player", 1, FIELD); // 2
        messages.post("room", "player", 1, FIELD); // 3

        // when
        client(0).sendToServer("{'command':'getAllField', " +
                "'data':{'room':'room', 'count':2}}");
        waitForServerReceived();
        waitForClientReceived(0);

        // then
        assertEquals("[getAllField(Filter(room=room, count=2, " +
                        "afterId=null, beforeId=null, inclusive=null))]",
                receivedOnServer());
        assertEquals("[{'command':'add', 'data':[" +
                        "{'id':2,'text':'message2','room':'room','type':3,'topicId':1," +
                        "'playerId':'player','playerName':'player-name','time':1615231623345}," +
                        "{'id':3,'text':'message3','room':'room','type':3,'topicId':1," +
                        "'playerId':'player','playerName':'player-name','time':1615231723345}]}]",
                client(0).messages());
    }

    @Test
    public void shouldPostRoom_fail() {
        // given
        createPlayer("player", "room", "first");

        client(0).start();

        // when
        client(0).sendToServer("{'command':'postRoom', " +
                "'data':{'room':'otherRoom', 'text':'message'}}");
        waitForServerReceived();
        waitForClientReceived(0);

        // then
        assertEquals("[postRoom(message, otherRoom)]", receivedOnServer());
        assertEquals("[{'command':'error', 'data':" +
                        "{'error':'IllegalArgumentException'," +
                        "'message':'Player 'player' is not in room 'otherRoom''}}]",
                client(0).messages());
    }

    @Test
    public void shouldPostRoom_success() {
        // given
        createPlayer("player", "room", "first");

        client(0).start();

        // when
        nowIs(12345L);
        client(0).sendToServer("{'command':'postRoom', " +
                "'data':{'room':'room', 'text':'message'}}");
        waitForServerReceived();
        waitForClientReceived(0);

        // then
        assertEquals("[postRoom(message, room)]", receivedOnServer());
        assertEquals("[{'command':'add', 'data':[" +
                        "{'id':1,'text':'message','room':'room','type':1,'topicId':null," +
                        "'playerId':'player','playerName':'player-name','time':12345}]}]",
                client(0).messages());
    }

    @Test
    public void shouldPostRoom_success_informAnotherUser() {
        // given
        createPlayer("player", "room", "first");
        createPlayer("player2", "room", "first");

        client(0).start();
        client(1).start();

        // when
        nowIs(12345L);
        client(0).sendToServer("{'command':'postRoom', " +
                "'data':{'room':'room', 'text':'message1'}}");
        waitForServerReceived();
        waitForClientReceived(0);
        waitForClientReceived(1);

        // then
        assertEquals("[postRoom(message1, room)]", receivedOnServer());
        // inform player1
        assertEquals("[{'command':'add', 'data':[" +
                        "{'id':1,'text':'message1','room':'room','type':1,'topicId':null," +
                        "'playerId':'player','playerName':'player-name','time':12345}]}]",
                client(0).messages());

        // inform player2 because of same room
        assertEquals("[{'command':'add', 'data':[" +
                        "{'id':1,'text':'message1','room':'room','type':1,'topicId':null," +
                        "'playerId':'player','playerName':'player-name','time':12345}]}]",
                client(1).messages());
    }

    @Test
    public void shouldPostField_fail() {
        // given
        createPlayer("player", "room", "first");

        client(0).start();

        // when
        client(0).sendToServer("{'command':'postField', " +
                "'data':{'room':'otherRoom', 'text':'message'}}");
        waitForServerReceived();
        waitForClientReceived(0);

        // then
        assertEquals("[postField(message, otherRoom)]", receivedOnServer());
        assertEquals("[{'command':'error', 'data':" +
                        "{'error':'IllegalArgumentException'," +
                        "'message':'There is no player 'player' in room 'otherRoom''}}]",
                client(0).messages());
    }

    @Test
    public void shouldPostField_success() {
        // given
        createPlayer("player", "room", "first");

        client(0).start();

        // when
        nowIs(12345L);
        client(0).sendToServer("{'command':'postField', " +
                "'data':{'room':'room', 'text':'message'}}");
        waitForServerReceived();
        waitForClientReceived(0);

        // then
        assertEquals("[postField(message, room)]", receivedOnServer());
        assertEquals("[{'command':'add', 'data':[" +
                        "{'id':1,'text':'message','room':'room','type':3,'topicId':1," +
                        "'playerId':'player','playerName':'player-name','time':12345}]}]",
                client(0).messages());

        assertEquals("Chat.Message(id=1, topicId=1, type=FIELD(3), room=room, " +
                        "playerId=player, time=12345, text=message)",
                chat.getMessageById(1).toString());
    }

    @Test
    public void shouldPostField_success_informAnotherUser_sameField() {
        // given
        roomsSettings.settings("room", "third")
                .bool(ROUNDS_ENABLED, true)
                .integer(ROUNDS_TEAMS_PER_ROOM, 1)
                .integer(ROUNDS_PLAYERS_PER_ROOM, 2);

        Deal deal1 = createPlayer("player", "room", "third");
        Deal deal2 = createPlayer("player2", "room", "third");

        assertEquals("[1, 1]", Arrays.asList(
                fields.id(deal1.getField()),
                fields.id(deal2.getField())).toString());

        client(0).start();
        client(1).start();

        // when
        nowIs(12345L);
        client(0).sendToServer("{'command':'postField', " +
                "'data':{'room':'room', 'text':'message'}}");
        waitForServerReceived();
        waitForClientReceived(0);
        waitForClientReceived(1);

        // then
        assertEquals("[postField(message, room)]", receivedOnServer());

        assertEquals("Chat.Message(id=1, topicId=1, type=FIELD(3), room=room, " +
                        "playerId=player, time=12345, text=message)",
                chat.getMessageById(1).toString());

        // inform player1
        assertEquals("[{'command':'add', 'data':[" +
                        "{'id':1,'text':'message','room':'room','type':3,'topicId':1," +
                        "'playerId':'player','playerName':'player-name','time':12345}]}]",
                client(0).messages());

        // inform player2 because of same field
        assertEquals("[{'command':'add', 'data':[" +
                        "{'id':1,'text':'message','room':'room','type':3,'topicId':1," +
                        "'playerId':'player','playerName':'player-name','time':12345}]}]",
                client(1).messages());
    }

    @Test
    public void shouldPostField_success_dontInformAnotherUser_differentFields() {
        // given
        Deal deal1 = createPlayer("player", "room", "first");
        Deal deal2 = createPlayer("player2", "room", "first");

        assertEquals("[1, 2]", Arrays.asList(
                fields.id(deal1.getField()),
                fields.id(deal2.getField())).toString());

        client(0).start();
        client(1).start();

        // when
        nowIs(12345L);
        client(0).sendToServer("{'command':'postField', " +
                "'data':{'room':'room', 'text':'message'}}");
        waitForServerReceived();
        waitForClientReceived(0);
        waitForClientReceived(1);

        // then
        assertEquals("[postField(message, room)]", receivedOnServer());

        assertEquals("Chat.Message(id=1, topicId=1, type=FIELD(3), room=room, " +
                        "playerId=player, time=12345, text=message)",
                chat.getMessageById(1).toString());

        // inform player1
        assertEquals("[{'command':'add', 'data':[" +
                        "{'id':1,'text':'message','room':'room','type':3,'topicId':1," +
                        "'playerId':'player','playerName':'player-name','time':12345}]}]",
                client(0).messages());

        // don't inform player2 because of other field
        assertEquals("[]",
                client(1).messages());
    }

    @Test
    public void shouldPostTopic_fail() {
        // given
        createPlayer("player", "room", "first");

        client(0).start();

        // when
        client(0).sendToServer("{'command':'postTopic', " +
                "'data':{'id':1, 'room':'room', 'text':'message'}}");
        waitForServerReceived();
        waitForClientReceived(0);

        // then
        assertEquals("[postTopic(1, message, room)]", receivedOnServer());
        assertEquals("[{'command':'error', 'data':" +
                        "{'error':'IllegalArgumentException'," +
                        "'message':'There is no message with id '1' in room 'room''}}]",
                client(0).messages());
    }

    @Test
    public void shouldPostTopic_success() {
        // given
        createPlayer("player", "room", "first");

        client(0).start();

        messages.post("room", "player", null, ROOM); // 1
        messages.post("room", "player", null, ROOM); // 2

        // when
        nowIs(12345L);
        client(0).sendToServer("{'command':'postTopic', " +
                "'data':{'id':1, 'room':'room', 'text':'message'}}");
        waitForServerReceived();
        waitForClientReceived(0);

        // then
        assertEquals("[postTopic(1, message, room)]", receivedOnServer());
        assertEquals("[{'command':'add', 'data':[" +
                        "{'id':3,'text':'message','room':'room','type':2,'topicId':1," +
                        "'playerId':'player','playerName':'player-name','time':12345}]}]",
                client(0).messages());

        assertEquals("Chat.Message(id=3, topicId=1, type=TOPIC(2), " +
                        "room=room, playerId=player, time=12345, text=message)",
                chat.getMessageById(3).toString());
    }
}