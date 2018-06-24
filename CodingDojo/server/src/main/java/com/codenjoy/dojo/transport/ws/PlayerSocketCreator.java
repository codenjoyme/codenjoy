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


import com.codenjoy.dojo.transport.auth.AuthenticationService;
import com.codenjoy.dojo.transport.auth.PlayerAuth;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class PlayerSocketCreator implements WebSocketCreator {

    private Logger LOGGER = LoggerFactory.getLogger(PlayerSocketCreator.class);

    private PlayerTransport transport;
    private AuthenticationService authenticationService;
    private boolean waitForClient;

    public PlayerSocketCreator(PlayerTransport transport,
                               AuthenticationService authenticationService,
                               boolean waitForClient)
    {
        this.authenticationService = authenticationService;
        this.transport = transport;
        this.waitForClient = waitForClient;
    }

    @Override
    public PlayerSocket createWebSocket(ServletUpgradeRequest servletRequest, ServletUpgradeResponse response) {
        HttpServletRequest request = servletRequest.getHttpServletRequest();
        PlayerAuth auth = authenticationService.authenticate(request);
        PlayerSocket socket = new PlayerSocket(auth.getAuthId(), auth.isBot(), waitForClient);
        if (auth.getAuthId() == null) {
            LOGGER.warn("Unauthorized access {}", request.getParameterMap().toString());
            try {
                response.sendError(401, "Unauthorized access. Please register user and/or write valid EMAIL/CODE in the client.");
            } catch (IOException e) {
                LOGGER.warn("Error sending status {}", e.getMessage());
            }
            return null;
        }
        socket.onClose(() -> transport.unregisterPlayerSocket(socket));
        transport.registerPlayerSocket(auth.getAuthId(), socket);
        return socket;
    }
}