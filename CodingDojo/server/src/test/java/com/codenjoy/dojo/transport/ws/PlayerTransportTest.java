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


import com.codenjoy.dojo.config.AppProperties;
import com.codenjoy.dojo.services.DebugService;
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
    private List<ResponseHandler> handlers = new LinkedList<>();
    private AuthenticationService authentication;
    private PlayerSocketCreator creator;
    private LinkedList<ServletUpgradeResponse> responses = new LinkedList<>();

    @Test
    public void shouldSendDataToWebSocketClient_caseClientSendFirst_withUniqueSocketFilter() throws IOException {
        // given
        createServices(PlayerSocket.CLIENT_SEND_FIRST);
        createServerWebSocket("id");
        PlayerSocket webSocket = connectWebSocketClient("id");

        // when client answer
        answerClient(webSocket);

        // when send state
        transport.sendState("id", new LinkedHashMap<String, Integer>(){{
            put("one", 1);
            put("two", 2);
            put("three", 3);
        }});

        // then
        verify(webSocket.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
    }

    private void answerClient(PlayerSocket webSocket) {
        webSocket.onWebSocketText("to upper case"); // CLIENT_SEND_FIRST
        transport.setFilterFor(webSocket,
                data -> data.toString().toUpperCase());
    }

    private void createServices(boolean waitForClient) {
        transport = new PlayerTransportImpl();
        authentication = mock(AuthenticationService.class);
        creator = new PlayerSocketCreator(transport, authentication, waitForClient);
    }

    private PlayerSocket connectWebSocketClient(String authId) {
        when(authentication.authenticate(any(HttpServletRequest.class))).thenReturn(authId);

        PlayerSocket webSocket = createWebSocket();

        if (webSocket == null) return null;

        Session session = mock(Session.class);
        when(session.isOpen()).thenReturn(true);
        RemoteEndpoint remote = mock(RemoteEndpoint.class);
        when(session.getRemote()).thenReturn(remote);
        webSocket.onWebSocketConnect(session);
        return webSocket;
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
    public void shouldSendDataToWebSocketClient_caseServerSendFirst_withDefaultFilter() throws IOException {
        // given
        createServices(PlayerSocket.SERVER_SEND_FIRST);
        createServerWebSocket("id");
        PlayerSocket webSocket = connectWebSocketClient("id");

        // given
        transport.setDefaultFilter(data -> ((Map)data).keySet());

        // when
        transport.sendState("id", new LinkedHashMap<String, Integer>(){{
            put("one", 1);
            put("two", 2);
            put("three", 3);
        }});

        // then
        verify(webSocket.getSession().getRemote()).sendString("[one, two, three]");
    }

    private void createServerWebSocket(String authId) {
        ResponseHandler handler = mock(ResponseHandler.class);
        transport.registerPlayerEndpoint(authId, handler);
        handlers.add(handler);
    }

    @Test
    public void shouldSendDataToWebSocketClient_caseClientSendFirst_forSeveralClients() throws IOException {
        // given
        createServices(PlayerSocket.CLIENT_SEND_FIRST);

        createServerWebSocket("id1");
        createServerWebSocket("id2");

        PlayerSocket webSocket1 = connectWebSocketClient("id1");
        PlayerSocket webSocket2 = connectWebSocketClient("id2");
        PlayerSocket webSocket3 = connectWebSocketClient("id1");

        // when client answer
        answerClient(webSocket1);
        answerClient(webSocket2);
        answerClient(webSocket3);

        // when send state
        transport.sendState("id1", new LinkedHashMap<String, Integer>(){{
            put("one", 1);
            put("two", 2);
            put("three", 3);
        }});

        // then
        verify(webSocket1.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
        verifyNoMoreInteractions(webSocket2.getSession().getRemote());
        verify(webSocket3.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
    }

    @Test
    public void shouldSendError_whenUserIsNotAuthenticate() throws IOException {
        // given
        createServices(PlayerSocket.SERVER_SEND_FIRST);
        createServerWebSocket("id");

        // when
        PlayerSocket webSocket = connectWebSocketClient(null);

        // then
        // verify(webSocket.getSession().getRemote()).sendString("Unregistered user");
        assertEquals(null, webSocket);
        verify(responses.get(0)).sendError(401, "Unauthorized access. Please register user and/or write valid EMAIL/CODE in the client.");
    }

    @Test
    public void shouldSendState_whenNoWebSockets() throws IOException {
        // given
        createServices(PlayerSocket.SERVER_SEND_FIRST);

        // no webSocket
        createServerWebSocket("id1");
        PlayerSocket webSocket = connectWebSocketClient("id1");

        // when
        String anotherId = "id2";
        transport.sendState(anotherId, new LinkedHashMap<String, Integer>(){{
            put("one", 1);
            put("two", 2);
            put("three", 3);
        }});

        // then
        verifyNoMoreInteractions(webSocket.getSession().getRemote());
    }

    @Test
    public void shouldSendDataToWebSocketClient_caseClientConnectedBeforeServerSocketCreated() throws IOException {
        // given
        createServices(PlayerSocket.CLIENT_SEND_FIRST);
        PlayerSocket webSocket = connectWebSocketClient("id");
        createServerWebSocket("id");

        // when client answer
        answerClient(webSocket);

        // when send state
        transport.sendState("id", new LinkedHashMap<String, Integer>(){{
            put("one", 1);
            put("two", 2);
            put("three", 3);
        }});

        // then
        verify(webSocket.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
    }

    @Test
    public void shouldNotSendToClients_whenUnregisterPlayerEndpoint() throws IOException {
        // given
        createServices(PlayerSocket.CLIENT_SEND_FIRST);

        createServerWebSocket("id");

        PlayerSocket webSocket1 = connectWebSocketClient("id");
        PlayerSocket webSocket2 = connectWebSocketClient("id");
        PlayerSocket webSocket3 = connectWebSocketClient("id");

        // when client answer
        answerClient(webSocket1);
        answerClient(webSocket2);
        answerClient(webSocket3);

        // when unregister server
        transport.unregisterPlayerEndpoint("id");

        // when send state
        transport.sendState("id1", new LinkedHashMap<String, Integer>(){{
            put("one", 1);
            put("two", 2);
            put("three", 3);
        }});

        // then
        verifyNoMoreInteractions(webSocket1.getSession().getRemote());
        verifyNoMoreInteractions(webSocket2.getSession().getRemote());
        verifyNoMoreInteractions(webSocket3.getSession().getRemote());
    }

    @Test
    public void shouldNotSendToClients_whenUnregisterPlayerSockets() throws IOException {
        // given
        createServices(PlayerSocket.CLIENT_SEND_FIRST);

        createServerWebSocket("id");

        PlayerSocket webSocket1 = connectWebSocketClient("id");
        PlayerSocket webSocket2 = connectWebSocketClient("id");
        PlayerSocket webSocket3 = connectWebSocketClient("id");

        // when client answer
        answerClient(webSocket1);
        answerClient(webSocket2);
        answerClient(webSocket3);

        // when unregister client
        when(webSocket2.getSession().isOpen()).thenReturn(false);
        transport.unregisterPlayerSocket(webSocket2);

        // when send state
        transport.sendState("id", new LinkedHashMap<String, Integer>(){{
            put("one", 1);
            put("two", 2);
            put("three", 3);
        }});

        // then
        verify(webSocket1.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
        verifyNoMoreInteractions(webSocket2.getSession().getRemote());
        verify(webSocket3.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
    }

    @Test
    public void shouldAllClientsGetMessage_whenSendDataToAllWebSocketClients() throws IOException {
        // given
        createServices(PlayerSocket.CLIENT_SEND_FIRST);

        createServerWebSocket("id1");
        createServerWebSocket("id2");
        createServerWebSocket("id3");

        PlayerSocket webSocket1 = connectWebSocketClient("id1");
        PlayerSocket webSocket2 = connectWebSocketClient("id2");
        PlayerSocket webSocket3 = connectWebSocketClient("id1");

        // when client answer
        answerClient(webSocket1);
        answerClient(webSocket2);
        answerClient(webSocket3);

        // when send state
        transport.sendStateToAll(new LinkedHashMap<String, Integer>(){{
            put("one", 1);
            put("two", 2);
            put("three", 3);
        }});

        // then
        verify(webSocket1.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
        verify(webSocket2.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
        verify(webSocket3.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
    }

    @Test
    public void shouldCollectErrors_whenSendDataToAllWebSocketClients() throws IOException {
        // given
        createServices(PlayerSocket.CLIENT_SEND_FIRST);

        createServerWebSocket("id1");
        createServerWebSocket("id2");
        createServerWebSocket("id3");

        PlayerSocket webSocket1 = connectWebSocketClient("id1");
        PlayerSocket webSocket2 = connectWebSocketClient("id2");
        PlayerSocket webSocket3 = connectWebSocketClient("id3");

        // when client answer
        answerClient(webSocket1);
        answerClient(webSocket2);
        answerClient(webSocket3);

        // simulate errors
        RemoteEndpoint remote1 = webSocket1.getSession().getRemote();
        doThrow(new IOException("Error1")).when(remote1).sendString(anyString());

        RemoteEndpoint remote2 = webSocket2.getSession().getRemote();
        doThrow(new IOException("Error2")).when(remote2).sendString(anyString());

        RemoteEndpoint remote3 = webSocket3.getSession().getRemote();
        doThrow(new IOException("Error3")).when(remote3).sendString(anyString());

        // when send state
//        try { // TODO мы не ловим больше ошибков а потому надо про другому проверить факт недосылки
            transport.sendStateToAll(new LinkedHashMap<String, Integer>() {{
                put("one", 1);
                put("two", 2);
                put("three", 3);
            }});

//            fail("Expected exception");
//        } catch (Exception e) {
//            // then
//            assertEquals("Error during send state to all players: [Error1, Error2, Error3]",
//                    e.getMessage());
//        }
    }

    @Test
    public void shouldUnregisterPlayerSocket_whenClientClose() throws IOException {
        // given
        createServices(PlayerSocket.CLIENT_SEND_FIRST);

        createServerWebSocket("id");

        PlayerSocket webSocket1 = connectWebSocketClient("id");
        PlayerSocket webSocket2 = connectWebSocketClient("id");
        PlayerSocket webSocket3 = connectWebSocketClient("id");

        // when client answer
        answerClient(webSocket1);
        answerClient(webSocket2);
        answerClient(webSocket3);

        // when close websocket
        webSocket1.onWebSocketClose(123, "close reason");
        when(webSocket1.getSession().isOpen()).thenReturn(false);

        // when send state
        transport.sendState("id", new LinkedHashMap<String, Integer>(){{
            put("one", 1);
            put("two", 2);
            put("three", 3);
        }});

        // then
        verifyNoMoreInteractions(webSocket1.getSession().getRemote());
        verify(webSocket2.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
        verify(webSocket3.getSession().getRemote()).sendString("{ONE=1, TWO=2, THREE=3}");
    }
}
