package com.codenjoy.dojo.transport.ws;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PlayerSocketCreator implements WebSocketCreator {

    private Logger LOGGER = LoggerFactory.getLogger(TransportWebSocketServlet.class);

    private AuthenticationService authenticationService;
    private WebSocketPlayerTransport transport;

    public PlayerSocketCreator() {
        authenticationService = ApplicationContextListener.getContext().getBean(AuthenticationService.class);
        transport = ApplicationContextListener.getContext().getBean(WebSocketPlayerTransport.class);
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse response) {
        String authId = authenticationService.authenticate(servletUpgradeRequest.getHttpServletRequest());
        PlayerSocket playerSocket = new PlayerSocket();
        if (authId == null) {
            try {
                LOGGER.warn("Unregistered user {}", authId);
                playerSocket.sendMessage("Unregistered user");
            } catch (IOException e) {
                LOGGER.warn("Some exception when kicking unregistered user", e);
            }
            return null;
        }
        transport.registerPlayerSocket(authId, playerSocket);
        return playerSocket;
    }
}
