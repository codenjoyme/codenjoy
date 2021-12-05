package com.codenjoy.dojo.transport.ws;

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


import com.codenjoy.dojo.transport.auth.AuthenticationService;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PlayerTransportTest {

    private PlayerTransport transport;
    private AuthenticationService authentication;
    private PlayerSocketCreator creator;
    private final LinkedList<ServletUpgradeResponse> responses = new LinkedList<>();

    @Test
    public void shouldSendDataToWebSocketClient_caseClientSendFirst_withUniqueSocketFilter() throws Exception {
        // given
        createServices(PlayerSocket.CLIENT_SENDS_FIRST);
        createServerWebSocket("id");
        PlayerSocket socket = connectWebSocketClient("id");

        // when client answer
        answerClient(socket);

        // when send state
        int requested = transport.sendState("id", data());

        // then
        assertEquals(1, requested);
        verify(socket.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
    }

    private LinkedHashMap<String, Integer> data() {
        return new LinkedHashMap<>(){{
            put("one", 1);
            put("two", 2);
            put("three", 3);
        }};
    }

    private void answerClient(PlayerSocket socket) {
        socket.onWebSocketText("to upper case"); // CLIENT_SEND_FIRST
        transport.setFilterFor(socket,
                data -> data.toString().toUpperCase());
    }

    private void createServices(boolean clientSendsFirst) {
        transport = new PlayerTransportImpl();
        authentication = mock(AuthenticationService.class);
        creator = new PlayerSocketCreator(
                transport,
                authentication,
                PlayerSocket.IN_TURN_COMMUNICATION,
                clientSendsFirst);
    }

    private PlayerSocket connectWebSocketClient(String authId) {
        when(authentication.authenticate(any(HttpServletRequest.class))).thenReturn(authId);

        PlayerSocket socket = createWebSocket();

        if (socket == null) {
            throw new IllegalArgumentException("Socket is null");
        }

        Session session = mock(Session.class);
        when(session.isOpen()).thenReturn(true);
        RemoteEndpoint remote = mock(RemoteEndpoint.class);
        when(session.getRemote()).thenReturn(remote);
        socket.onWebSocketConnect(session);
        return socket;
    }

    private PlayerSocket createWebSocket() {
        ServletUpgradeRequest request = mock(ServletUpgradeRequest.class);
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        when(request.getHttpServletRequest()).thenReturn(httpRequest);
        ServletUpgradeResponse response = mock(ServletUpgradeResponse.class);
        responses.add(response);
        return creator.createWebSocket(request, response);
    }

    @Test
    public void shouldSendDataToWebSocketClient_caseServerSendFirst_withDefaultFilter() throws Exception {
        // given
        createServices(PlayerSocket.SERVER_SENDS_FIRST);
        createServerWebSocket("id");
        PlayerSocket socket = connectWebSocketClient("id");

        // given
        transport.setDefaultFilter(data -> ((Map)data).keySet());

        // when
        int requested = transport.sendState("id", data());

        // then
        assertEquals(1, requested);
        verify(socket.getSession().getRemote()).sendString("[one, two, three]");
    }

    private void createServerWebSocket(String authId) {
        ResponseHandler handler = mock(ResponseHandler.class);
        transport.registerPlayerEndpoint(authId, handler);
    }

    @Test
    public void shouldSendDataToWebSocketClient_caseClientSendFirst_forSeveralClients() throws Exception {
        // given
        createServices(PlayerSocket.CLIENT_SENDS_FIRST);

        createServerWebSocket("id1");
        createServerWebSocket("id2");

        PlayerSocket socket1 = connectWebSocketClient("id1");
        PlayerSocket socket2 = connectWebSocketClient("id2");
        PlayerSocket socket3 = connectWebSocketClient("id1");

        // when client answer
        answerClient(socket1);
        answerClient(socket2);
        answerClient(socket3);

        // when send state
        int requested = transport.sendState("id1", data());

        // then
        assertEquals(2, requested);
        verify(socket1.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
        verifyNoMoreInteractions(socket2.getSession().getRemote());
        verify(socket3.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
    }

    @Test
    public void shouldSendError_whenUserIsNotAuthenticate() throws Exception {
        // given
        createServices(PlayerSocket.SERVER_SENDS_FIRST);
        createServerWebSocket("id");

        // when
        try {
            connectWebSocketClient(null);
            fail("Expected exception");
        } catch (IllegalArgumentException exception) {
            assertEquals("Socket is null", exception.getMessage());
        }

        // then
        verify(responses.get(0)).sendError(401, "Unauthorized access. Please register user and/or write valid EMAIL/CODE in the client.");
    }

    @Test
    public void shouldSendState_whenNoWebSockets() {
        // given
        createServices(PlayerSocket.SERVER_SENDS_FIRST);

        // no socket
        createServerWebSocket("id1");
        PlayerSocket socket = connectWebSocketClient("id1");

        // when
        String anotherId = "id2";
        int requested = transport.sendState(anotherId, data());

        // then
        assertEquals(0, requested);
        verifyNoMoreInteractions(socket.getSession().getRemote());
    }

    @Test
    public void shouldSendDataToWebSocketClient_caseClientConnectedBeforeServerSocketCreated() throws Exception {
        // given
        createServices(PlayerSocket.CLIENT_SENDS_FIRST);
        PlayerSocket socket = connectWebSocketClient("id");
        createServerWebSocket("id");

        // when client answer
        answerClient(socket);

        // when send state
        int requested = transport.sendState("id", data());

        // then
        assertEquals(1, requested);
        verify(socket.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
    }

    @Test
    public void shouldNotSendToClients_whenUnregisterPlayerEndpoint() {
        // given
        createServices(PlayerSocket.CLIENT_SENDS_FIRST);

        createServerWebSocket("id");

        PlayerSocket socket1 = connectWebSocketClient("id");
        PlayerSocket socket2 = connectWebSocketClient("id");
        PlayerSocket socket3 = connectWebSocketClient("id");

        // when client answer
        answerClient(socket1);
        answerClient(socket2);
        answerClient(socket3);

        // when unregister server
        transport.unregisterPlayerEndpoint("id");

        // when send state
        int requested = transport.sendState("id1", data());

        // then
        assertEquals(0, requested);
        verifyNoMoreInteractions(socket1.getSession().getRemote());
        verifyNoMoreInteractions(socket2.getSession().getRemote());
        verifyNoMoreInteractions(socket3.getSession().getRemote());
    }

    @Test
    public void shouldNotSendToClients_whenUnregisterPlayerSockets() throws Exception {
        // given
        createServices(PlayerSocket.CLIENT_SENDS_FIRST);

        createServerWebSocket("id");

        PlayerSocket socket1 = connectWebSocketClient("id");
        PlayerSocket socket2 = connectWebSocketClient("id");
        PlayerSocket socket3 = connectWebSocketClient("id");

        // when client answer
        answerClient(socket1);
        answerClient(socket2);
        answerClient(socket3);

        // when unregister client
        when(socket2.getSession().isOpen()).thenReturn(false);
        transport.unregisterPlayerSocket(socket2);

        // when send state
        int requested = transport.sendState("id", data());

        // then
        assertEquals(2, requested);
        verify(socket1.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
        verifyNoMoreInteractions(socket2.getSession().getRemote());
        verify(socket3.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
    }

    @Test
    public void shouldSendToClients_whenOnlyOneClient() throws Exception {
        // given
        createServices(PlayerSocket.CLIENT_SENDS_FIRST);

        createServerWebSocket("id");

        PlayerSocket socket1 = connectWebSocketClient("id");
        PlayerSocket socket2 = connectWebSocketClient("id");
        PlayerSocket socket3 = connectWebSocketClient("id");

        // when client answer
        answerClient(socket1);
        answerClient(socket2);
        answerClient(socket3);

        // when send state
        int requested = transport.sendState("id", data());

        // then
        assertEquals(3, requested);
        verify(socket1.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
        verify(socket2.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
        verify(socket3.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
    }

    @Test
    public void shouldAllClientsGetMessage_whenSendDataToAllWebSocketClients() throws Exception {
        // given
        createServices(PlayerSocket.CLIENT_SENDS_FIRST);

        createServerWebSocket("id1");
        createServerWebSocket("id2");
        createServerWebSocket("id3");

        PlayerSocket socket1 = connectWebSocketClient("id1");
        PlayerSocket socket2 = connectWebSocketClient("id2");
        PlayerSocket socket3 = connectWebSocketClient("id3");

        // when client answer
        answerClient(socket1);
        answerClient(socket2);
        answerClient(socket3);

        // when send state
        int requested = transport.sendStateToAll(data());

        // then
        assertEquals(3, requested);
        verify(socket1.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
        verify(socket2.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
        verify(socket3.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
    }

    @Test
    public void shouldCollectErrors_whenSendDataToAllWebSocketClients() throws Exception {
        // given
        createServices(PlayerSocket.CLIENT_SENDS_FIRST);

        createServerWebSocket("id1");
        createServerWebSocket("id2");
        createServerWebSocket("id3");

        PlayerSocket socket1 = connectWebSocketClient("id1");
        PlayerSocket socket2 = connectWebSocketClient("id2");
        PlayerSocket socket3 = connectWebSocketClient("id3");

        // when client answer
        answerClient(socket1);
        answerClient(socket2);
        answerClient(socket3);

        // simulate errors for two sockets
        RemoteEndpoint remote1 = socket1.getSession().getRemote();
        doThrow(new IOException("Error1")).when(remote1).sendString(anyString());

        RemoteEndpoint remote2 = socket2.getSession().getRemote();
        doThrow(new IOException("Error2")).when(remote2).sendString(anyString());

        // when send state
        int requested = transport.sendStateToAll(data());

        // then
        assertEquals(1, requested);
    }

    @Test
    public void shouldUnregisterPlayerSocket_whenClientClose() throws Exception {
        // given
        createServices(PlayerSocket.CLIENT_SENDS_FIRST);

        createServerWebSocket("id");

        PlayerSocket socket1 = connectWebSocketClient("id");
        PlayerSocket socket2 = connectWebSocketClient("id");
        PlayerSocket socket3 = connectWebSocketClient("id");

        // when client answer
        answerClient(socket1);
        answerClient(socket2);
        answerClient(socket3);

        // when close websocket
        socket1.onWebSocketClose(123, "close reason");
        when(socket1.getSession().isOpen()).thenReturn(false);

        // when send state
        int requested = transport.sendState("id", data());

        // then
        assertEquals(2, requested);
        verifyNoMoreInteractions(socket1.getSession().getRemote());
        verify(socket2.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
        verify(socket3.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
    }
}
