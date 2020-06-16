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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@AllArgsConstructor
public class PlayerSocketCreator implements WebSocketCreator {

    public static final String UNAUTHORIZED_ACCESS = "Unauthorized access. Please register user and/or write valid EMAIL/CODE in the client.";
    private PlayerTransport transport;
    private AuthenticationService authenticationService;
    private boolean waitForClient;

    @Override
    public PlayerSocket createWebSocket(ServletUpgradeRequest servletRequest, ServletUpgradeResponse response) {
        HttpServletRequest request = servletRequest.getHttpServletRequest();
        String authId = authenticationService.authenticate(request);
        PlayerSocket socket = new PlayerSocket(authId, waitForClient);
        if (authId == null) {
            log.warn("Unauthorized access [{}] from {}", getParameters(request), request.getRemoteAddr());
            try {
                response.sendError(401, UNAUTHORIZED_ACCESS);
            } catch (IOException e) {
                log.warn("Error sending status {}", e.getMessage());
            }
            return null;
        }
        socket.onClose(() -> transport.unregisterPlayerSocket(socket));
        transport.registerPlayerSocket(authId, socket);
        return socket;
    }

    private String getParameters(HttpServletRequest request) {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            return parameters.keySet().stream()
                    .map(key -> String.format("%s=%s", key, Arrays.toString(parameters.get(key))))
                    .collect(toList())
                    .toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
