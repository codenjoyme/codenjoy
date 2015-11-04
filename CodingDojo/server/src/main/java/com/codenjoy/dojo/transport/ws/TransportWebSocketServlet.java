package com.codenjoy.dojo.transport.ws;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TransportWebSocketServlet extends WebSocketServlet {
    private AuthenticationService authenticationService;
    private WebSocketPlayerTransport transport;
    private Logger LOGGER = LoggerFactory.getLogger(TransportWebSocketServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();
        authenticationService = ApplicationContextListener.getContext().getBean(AuthenticationService.class);
        transport = ApplicationContextListener.getContext().getBean(WebSocketPlayerTransport.class);
    }

    @Override
    public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        String authId = authenticationService.authenticate(request);
        PlayerSocket playerSocket = new PlayerSocket(authId, transport);
        if (authId == null) {
            try {
                LOGGER.warn("Unregistered user {}", authId);
                playerSocket.sendMessage("Unregistered user");
                playerSocket.close();
            } catch (IOException e) {
                LOGGER.warn("Some exception when kicking unregistered user", e);
            }
            return null;
        }
        transport.registerPlayerSocket(authId, playerSocket);
        return playerSocket;
    }
}
