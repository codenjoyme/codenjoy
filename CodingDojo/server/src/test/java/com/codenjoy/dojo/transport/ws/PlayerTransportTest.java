package com.codenjoy.dojo.transport.ws;

import com.codenjoy.dojo.transport.auth.AuthenticationService;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by indigo on 2017-12-01.
 */
public class PlayerTransportTest {

    private PlayerTransport transport;
    private List<PlayerResponseHandler> handlers = new LinkedList<>();
    private AuthenticationService authentication;
    private PlayerSocketCreator creator;

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
        ServletUpgradeResponse response = null;
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
        PlayerResponseHandler handler = mock(PlayerResponseHandler.class);
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
}
