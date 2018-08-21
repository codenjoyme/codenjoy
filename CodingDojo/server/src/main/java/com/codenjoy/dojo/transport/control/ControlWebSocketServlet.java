package com.codenjoy.dojo.transport.control;

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


import com.codenjoy.dojo.services.TimerService;
import com.codenjoy.dojo.transport.auth.SecureAuthenticationService;
import com.codenjoy.dojo.transport.ws.PlayerSocket;
import com.codenjoy.dojo.transport.ws.PlayerSocketCreator;
import com.codenjoy.dojo.transport.ws.PlayerTransport;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public class ControlWebSocketServlet extends WebSocketServlet {

    @Autowired
    private TimerService timer;

    @Autowired
    @Qualifier("controlPlayerTransport")
    private PlayerTransport transport;

    @Autowired
    private SecureAuthenticationService authentication;

    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        PlayerSocketCreator creator =
                new PlayerSocketCreator(transport,
                        authentication,
                        PlayerSocket.SERVER_SEND_FIRST);

        webSocketServletFactory.setCreator(creator);

        timer.resume();
    }
}
